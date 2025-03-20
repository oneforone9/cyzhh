package com.essence.well;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.WaterMeterDataDao;
import com.essence.dao.entity.WaterMeterDataEntity;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@Component
@Slf4j
public class WellJobHandler {
    @Resource
    private WaterMeterDataDao waterMeterDataDao;

    static final String URL = "http://82.157.41.91:8787/cyjj/api/getWaterRecord.do";
    @Scheduled(cron = "0 20 5 * * ?")
    public void dealWellDate() throws IOException, UnirestException {
        Date date = new Date();
        String format = DateUtil.format(date, "yyyy-MM-dd");

        log.info("开始请求获取机井数据接口");
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.post("http://82.157.41.91:8787/cyjj/api/getWaterRecord.do?dt="+format)
                .header("Content-Type", "text/xml;charset=utf-8")
                .header("Authorization", "Bearer 4503D9F3-D2BE-4172-A0A7-6BA5A3B0DA1B")
                .body("<soap:Envelope \n    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n    xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" \n    xmlns:targetNamespace=\"http://WebXml.com.cn/\"\n    xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n\n    <soap:Body>\n        <targetNamespace:getSupportCityString xmlns=\"http://WebXml.com.cn/\">\n            <theRegionCode>3113</theRegionCode>\n        </targetNamespace:getSupportCityString>\n    </soap:Body>\n</soap:Envelope>\n")
                .asString();
        String body = response.getBody();

        JSONObject jsonObject = JSONObject.parseObject(body);
        if (jsonObject != null){
            String type = jsonObject.getString("type");
            if (type.equals("1")){
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                List<WaterMeterDataEntity> waterMeterDataDaos = new ArrayList<>();
                System.out.println(waterMeterDataDaos);
                for (Object o : jsonArray) {
                    WaterMeterDataEntity waterMeterData = new WaterMeterDataEntity();
                    JSONObject jsonObject1 = JSONObject.parseObject(o.toString());
                    String wiuCd = jsonObject1.getString("wiuCd");
                    Date dt = jsonObject1.getDate("dt");
                    String mpCd = jsonObject1.getString("mpCd");
                    Integer dayW = jsonObject1.getInteger("dayW");
                    waterMeterData.setWiuCd(wiuCd);
                    waterMeterData.setDayW(dayW);
                    waterMeterData.setMpCd(mpCd);
                    waterMeterData.setDt(dt);
                    waterMeterDataDaos.add(waterMeterData);
                }
                for (WaterMeterDataEntity meterDataEntity : waterMeterDataDaos) {
                    String wiuCd = meterDataEntity.getWiuCd();
                    Date dt = meterDataEntity.getDt();
                    meterDataEntity.setUpdateDate(new Date());
                    QueryWrapper queryWrapper = new QueryWrapper();
                    queryWrapper.eq("wiu_cd",wiuCd);
                    queryWrapper.eq("dt",dt);
                    List<WaterMeterDataEntity> waterMeterDataEntities = waterMeterDataDao.selectList(queryWrapper);
                    if (CollUtil.isNotEmpty(waterMeterDataEntities)){

                        waterMeterDataDao.update(meterDataEntity,queryWrapper);
                    }else {
                        waterMeterDataDao.insert(meterDataEntity);
                    }
                }
            }
        }
    }


}
