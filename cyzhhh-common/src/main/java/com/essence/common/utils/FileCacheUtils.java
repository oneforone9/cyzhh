package com.essence.common.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;

import java.io.File;
import java.util.List;
@Slf4j
public class FileCacheUtils {

    public static final String Dir  = "E:\\model\\filecache\\";

    public static void set(String key,Object value){
        FileUtil.writeString(value.toString(), Dir+ File.separator+key+".txt","UTF-8");
    }

    public static <T> List<T> get(String key, Class<T>  t){
        String s = null;
        try {
            s = FileUtil.readString(Dir+ File.separator+key + ".txt", "UTF-8");
        } catch (IORuntimeException e) {
            log.info("文件没找到");
            return null;

        }

        List<T> list = JSONObject.parseArray(s,t);
        return list;
    }
}
