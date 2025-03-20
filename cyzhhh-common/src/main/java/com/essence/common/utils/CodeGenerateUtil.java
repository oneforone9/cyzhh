package com.essence.common.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhy
 * @since 2022/10/27 16:10
 */
public class CodeGenerateUtil {
    private static Map<String, Integer> map = new HashMap<>();

    public synchronized static int get(String code) {
        Integer sequenceCode = map.get(code);
        if (null == sequenceCode){
            sequenceCode = 0;
        }
        sequenceCode++;
        map.put(code, sequenceCode);
        return sequenceCode;
    }


    public static void clear() {
        map.clear();
    }

    public static synchronized void put(String key, Integer value){
        Integer v = map.get(key);
        if (null == v || v < value){
            map.put(key, value);
        }

    }
}
