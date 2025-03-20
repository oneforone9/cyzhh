package com.essence.common.utils;

import com.alibaba.fastjson.JSON;
import com.essence.common.constant.AccessToken;
import com.essence.common.constant.WxMessageparams;

/**
 * @Author: liwy
 * @CreateTime: 2023-08-22  18:08
 */
public class WxMessageUtil {
//    @Value("${file.local.path}")
//    private String AppID;

    /**
     * 微信小程序APPID
     */
    //private final static String AppID = "wxc6d24e9bf7719872";
    /**
     * 微信小程序密钥
     * @return
     */
    //private final static String AppSecret = "846661090d628733f066840b689dba68";


    //private final static String sendUrl = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=";

    //private final static String tokenUrl = "https://api.weixin.qq.com/cgi-bin/token";

    public static String sendMsg(WxMessageparams messageparams, String AppID, String AppSecret, String sendUrl, String tokenUrl) {
        String url = sendUrl + getAccessToken(AppID, AppSecret, tokenUrl);
        String token = HttpUtils.sendPost(url, JSON.toJSONString(messageparams));
        return token;
    }


    public static String getAccessToken(String AppID,String AppSecret,String tokenUrl) {
        //String params = StringUtils.format("appid={}&secret={}&grant_type=client_credential", AppID, AppSecret);
        String params = "appid="+AppID+"&secret="+AppSecret+"&grant_type=client_credential";
        String token = HttpUtils.sendGet(tokenUrl, params);
        AccessToken accessToken = JSON.parseObject(token, AccessToken.class);
        return accessToken.getAccess_token();
    }

}
