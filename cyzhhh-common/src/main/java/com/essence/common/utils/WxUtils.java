package com.essence.common.utils;


import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.zhxu.okhttps.HttpResult;
import cn.zhxu.okhttps.OkHttps;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class WxUtils {


    public static final String APPID = "wx623ddf6e7043a2bd";
    public static final String APPSERCET = "aa1646ec321155a109d6a10f8792cc6b";

    public static String getOpenid(String wxlogincode) {
        HttpResult httpResult = OkHttps.sync("https://api.weixin.qq.com/sns/jscode2session").addUrlPara("appid", APPID)
                .addUrlPara("secret", APPSERCET)
                .addUrlPara("grant_type", "authorization_code")
                .addUrlPara("js_code", wxlogincode).bodyType(OkHttps.JSON).get();
        JSONObject parse = JSONObject.parseObject(httpResult.getBody().toString());



//        Map<String,Object> param1 = new HashMap<>();
//        param1.put("appid",APPID);
//        param1.put("secret",APPSERCET);
//        param1.put("grant_type","authorization_code");
//        param1.put("js_code",wxlogincode);
//        log.info("开始请求getOpenid");
//        String areaStatisticBody = HttpRequest.get("https://api.weixin.qq.com/sns/jscode2session").form(param1).timeout(90000).execute().body();
//        JSONObject parse = JSONObject.parseObject(areaStatisticBody);
        return parse.get("openid").toString();

    }


    public static String getAccessToken() {


//        HttpResult httpResult = OkHttps.sync("https://api.weixin.qq.com/cgi-bin/token").addUrlPara("appid", APPID)
//                .addUrlPara("secret", APPSERCET)
//                .addUrlPara("grant_type", "client_credential")
//               .bodyType(OkHttps.JSON).get();
//        JSONObject parse = JSONObject.parseObject(httpResult.getBody().toString());


        Map<String,Object> param1 = new HashMap<>();
        param1.put("appid",APPID);
        param1.put("secret",APPSERCET);
        param1.put("grant_type","client_credential");

        log.info("开始请求getAccessToken");
        String areaStatisticBody = HttpRequest.get("https://api.weixin.qq.com/cgi-bin/token").form(param1).timeout(90000).execute().body();
        JSONObject parse = JSONObject.parseObject(areaStatisticBody);

        return parse.get("access_token").toString();

    }

    public static Object getPhone(String code) {
        HttpResult httpResult = OkHttps.sync("https://api.weixin.qq.com/wxa/business/getuserphonenumber")
                .addUrlPara("access_token", getAccessToken())
                .addBodyPara("code", code)
                .bodyType(OkHttps.JSON)
                .post();
        JSONObject parse = JSONObject.parseObject(httpResult.getBody().toString());

//        Map<String,Object> param1 = new HashMap<>();
//        param1.put("access_token",getAccessToken());
//        param1.put("code",code);
//
//
//        System.out.println("开始请求getPhone");
//        String body = HttpRequest.post("https://api.weixin.qq.com/wxa/business/getuserphonenumber").header("Content-Type","application/json").body(JSONObject.toJSONString(param1)  ).timeout(100000).execute().body();
//        JSONObject parse = JSONObject.parseObject(body);
        Object phone_info = parse.get("phone_info");
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(phone_info));
        String phoneNumber =(String) jsonObject.get("phoneNumber");
        if (StrUtil.isEmpty(phoneNumber)){
            throw new RuntimeException("");
        }
        return phoneNumber;
    }


//    public static Object getPhone(String code) {
//

//        return null;
//    }

    public static void main(String[] args) {
        Long start = System.currentTimeMillis();
        WxUtils wxUtils = new WxUtils();
        String accessToken = wxUtils.getAccessToken();
        System.out.println(accessToken);
        System.out.println(System.currentTimeMillis() - start + "ms");
    }
}
