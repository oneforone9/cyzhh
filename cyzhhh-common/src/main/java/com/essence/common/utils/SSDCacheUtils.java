package com.essence.common.utils;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import lombok.extern.slf4j.Slf4j;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/*
        <dependency>
            <groupId>org.rocksdb</groupId>
            <artifactId>rocksdbjni</artifactId>
            <version>7.7.3</version>
        </dependency>

        <dependency>
            <groupId>com.dyuproject.protostuff</groupId>
            <artifactId>protostuff-core</artifactId>
            <version>1.0.10</version>
            <optional>true</optional>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>com.dyuproject.protostuff</groupId>
            <artifactId>protostuff-runtime</artifactId>
            <version>1.0.10</version>
            <optional>true</optional>
            <scope>provided</scope>
        </dependency>


*/
@Slf4j
public class SSDCacheUtils {


    static {
        RocksDB.loadLibrary();
    }

    private static volatile RocksDB rocksDB;


    public static RocksDB getRocksDB() throws RocksDBException {
        if (rocksDB == null) {
            synchronized (SSDCacheUtils.class) {
                if (rocksDB == null) {
                    rocksDB = RocksDB.open(new Options().setCreateIfMissing(true), "D:\\project\\bjby\\rocksdb");
                }
            }
        }
        return rocksDB;
    }

    public static Boolean set(String key, Object t) {

        try {
            byte[] bytes1 = serializeProtoStuffObj(t);
            getRocksDB().put(key.getBytes(), bytes1);
            return true;
        } catch (RocksDBException e) {
            return false;
        }
    }

    public static <T> T get(String key, Class<T> cls) {

        try {
            long start = System.currentTimeMillis();

            byte[] bytes = getRocksDB().get(key.getBytes());
            T t = deserializeProtoStuffDataToObj(bytes, cls);
            long end = System.currentTimeMillis();
            if(t!=null)
            log.info("--------getSSDCache------"+key+"     耗时"+(end - start)+"ms");

            return t;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    public <T> List<byte[]> serializeProtoStuffObjList(List<T> pList) {
        if (pList == null || pList.size() <= 0) {
            return null;
        }
        Class<T> cls = (Class<T>) pList.get(0).getClass();
        long start = System.currentTimeMillis();
        List<byte[]> bytes = new ArrayList<byte[]>();
        Schema<T> schema = RuntimeSchema.getSchema(cls);
        LinkedBuffer buffer = LinkedBuffer.allocate(4096);
        byte[] protostuff = null;
        for (T p : pList) {
            try {
                protostuff = ProtostuffIOUtil.toByteArray(p, schema, buffer);
                bytes.add(protostuff);
            } finally {
                buffer.clear();
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        return bytes;
    }


    public static <T> byte[] serializeProtoStuffObj(T t) {
        if (t == null) {
            return null;
        }
        Class<T> cls = (Class<T>) t.getClass();
//        long start = System.currentTimeMillis() ;
        Schema<T> schema = RuntimeSchema.getSchema(cls);
        LinkedBuffer buffer = LinkedBuffer.allocate(4096);
        byte[] bytes = null;
        try {
            bytes = ProtostuffIOUtil.toByteArray(t, schema, buffer);

        } finally {
            buffer.clear();
        }

//        long end = System.currentTimeMillis() ;
//        System.out.println(end-start);
        return bytes;
    }

    public static <T> T deserializeProtoStuffDataToObj(byte[] bytes, Class<T> t) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (bytes == null) {
            return null;
        }
        long start = System.currentTimeMillis();
        Schema<T> schema = RuntimeSchema.getSchema(t);
        List<T> list = new ArrayList<T>();
        T instance = t.getDeclaredConstructor().newInstance();
        ProtostuffIOUtil.mergeFrom(bytes, instance, schema);

        long end = System.currentTimeMillis();
        System.out.println(end - start);
        return instance;
    }


    public <T> List<T> deserializeProtoStuffDataListToObjList(List<byte[]> bytesList, Class<T> t) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (bytesList == null || bytesList.size() <= 0) {
            return null;
        }
        long start = System.currentTimeMillis();
        Schema<T> schema = RuntimeSchema.getSchema(t);
        List<T> list = new ArrayList<T>();
        for (byte[] bs : bytesList) {
            T instance = t.getDeclaredConstructor().newInstance();
            ProtostuffIOUtil.mergeFrom(bs, instance, schema);
            list.add(instance);
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        return list;
    }
}
