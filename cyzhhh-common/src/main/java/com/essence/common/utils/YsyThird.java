package com.essence.common.utils;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class YsyThird {
    @Value("${ysy.appid}")
    private String appId;

    @Value("${ysy.secretkey}")
    private String secretKey;

    // 获取AccessToken
    public String getToken() {
        try {
            String url = "https://ezcloud.uniview.com/openapi/user/app/token/get";
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("appId", appId);
            jsonParam.put("secretKey", secretKey);

            HttpResponse response = HttpRequest.post(url)
                    .header("Host", "ezcloud.uniview.com")
                    .header("Content-Type", "application/json")
                    .body(jsonParam.toString())
                    .execute();

            int statusCode = response.getStatus();
            if (statusCode == 200) {
                Map responseMap = JSONUtil.toBean(response.body(), Map.class);
                if (responseMap.get("code").equals(200)) {
                    JSONObject data = (JSONObject) responseMap.get("data");
                    return (String) data.get("accessToken");
                } else {
                    return "Error: " + responseMap.get("msg");
                }
            } else {
                return "HTTP Error: " + statusCode;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Network Error: " + e.getMessage();
        }
    }

    // 查询直播设备列表
    public List<DeviceSerialModel> queryDevices() {
        String url = "https://ezcloud.uniview.com/openapi/live/device/query";
        JSONObject jsonBody = new JSONObject();
//        jsonBody.put("liveEnable", 1);
        jsonBody.put("pageStart", 0);
        jsonBody.put("pageSize", 100);
        try {
            HttpResponse response = HttpRequest.post(url)
                    .header("Host", "ezcloud.uniview.com")
                    .header("Content-Type", "application/json")
                    .header("Authorization", getToken())
                    .body(jsonBody.toString())
                    .execute();
            if (response.isOk()) {
                Map responseMap = JSONUtil.toBean(response.body(), Map.class);
                if (responseMap.get("code").equals(200)) {
                    JSONObject data = (JSONObject) responseMap.get("data");
                    JSONArray deviceList = (JSONArray) data.get("deviceList");
                    return deviceList.stream()
                            .map(p -> JSONUtil.toBean(p.toString(), DeviceSerialModel.class))
                            .collect(Collectors.toList());
                }
            } else {
                System.err.println("HTTP request failed with status code: " + response.getStatus());
            }
        } catch (Exception e) {
            System.err.println("Error occurred during HTTP request or JSON parsing: " + e.getMessage());
        }

        return new ArrayList<>();
    }

    // 批量设置直播配置功能
    public String setLiveConfig(List<String> deviceSerialList) {
        String url = "https://ezcloud.uniview.com/openapi/live/config/batch/set";
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("deviceSerialList", deviceSerialList);
        jsonBody.put("liveEnable", 1);

        HttpResponse response = HttpRequest.post(url)
                .header("Host", "ezcloud.uniview.com")
                .header("Content-Type", "application/json")
                .header("Authorization", getToken())
                .body(jsonBody.toString())
                .execute();
        return response.body();
    }
}
