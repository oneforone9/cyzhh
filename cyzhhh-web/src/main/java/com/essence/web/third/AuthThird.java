package com.essence.web.third;

import com.essence.eluban.utils.MD5Util;

/**
 * @author zhy
 * @since 2023/3/14 11:58
 */
public class AuthThird {
//    public static void main(String[] args) {
//
//
//
//        String sign = sign("bjlz","234567");
//
//        System.out.println(sign);
//        System.out.println(auth(sign,"bjlz"));
//    }

    /**
     *
     * @param authKey 令牌
     * @param authKey 模拟六位数
     * @return
     */
    static String sign(String authKey,String sing){
        // 当前时间
        long time = System.currentTimeMillis();
        // 模拟六位随/
        String str = sing ;
        // md5加密
        String encrypt = MD5Util.encrypt(str + time + authKey).substring(0, 32);
        // 生成签名
        return encrypt.substring(6,8) + str +encrypt.substring(8)+ time;
    }

    /**
     *
     * @param fileSign
     * authKey 令牌
     * @return
     */
    static boolean auth(String fileSign,String authKey){
        if (fileSign.length() < 33){
            return false;
        }
        String time = fileSign.substring(32);

        try {
            long timestamp = Long.parseLong(time);
            if (System.currentTimeMillis() - timestamp > 1000 * 60 * 30 * 24 || System.currentTimeMillis() - timestamp < 0){
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }


        String plaintext = fileSign.substring(2, 8);
        String encrypt = MD5Util.encrypt(plaintext + time + authKey).substring(0, 32);


        return fileSign.equals(encrypt.substring(6,8) + plaintext +encrypt.substring(8)+ time );
    }
}
