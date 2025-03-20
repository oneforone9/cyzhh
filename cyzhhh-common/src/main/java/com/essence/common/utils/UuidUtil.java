/**
 * @Title: UuidUtil.java
 * @Package: com.zhurong.utils.general
 * @author LZG, liuzhongguochn@gmail.com
 * Copyright (c) 2018 https://onezg.cnblogs.com
 */
package com.essence.common.utils;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

/**
 * @description: uuid工具类
 * @author LZG
 * @date 2018/8/24
 */
public class UuidUtil {

    /**
     * @Description: 生成uuid唯一字符串(32位)
     * @return java.lang.String
     * @author LZG
     * @date 2018/8/24
     */
    public static String get32UUIDStr() {
        String uuidStr = "";
        UUID uuid = UUID.randomUUID();
        uuidStr = uuid.toString().replace("-", "");
        return uuidStr;
    }

    public static Integer get10UUIDIntStr() {
        StringBuffer sb = new StringBuffer();
        Random r = new Random();
        sb.append(r.nextInt(99999)).append(r.nextInt(9999));
        return Integer.valueOf(sb.toString());
    }

    /**
     * @Description: 测试方法
     * @param args
     * @return void
     * @author LZG
     * @date 2018/8/24
     */
    public static void main(String[] args) {
        System.out.println("开始时间");
        Long beginTime = System.currentTimeMillis();

        Set<Integer> s = new HashSet<>();
        for (int i = 0; i < 10000; i++) {

            s.add(get10UUIDIntStr());


        }
        System.out.println(s.size());
        Long opetime = System.currentTimeMillis() - beginTime;
        System.out.println("运行时间" + opetime);


    }

}