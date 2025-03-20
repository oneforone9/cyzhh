package com.essence.job.executor.health;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.dto.health.DataInfoDto;
import com.essence.common.dto.health.HealthRiverBelongDto;
import com.essence.common.dto.health.HealthRiverReciveDto;
import com.essence.common.dto.health.ManageBelongDto;
import com.essence.dao.AreaHealthDataInfoDao;
import com.essence.entity.AreaHealthDataInfoEntity;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Component
public class HealthRiverJob {
    @Resource
    private AreaHealthDataInfoDao areaHealthDataInfoDao;
    //登陆
    private String loginUrl = "http://203.86.54.189:8081/hzz/api/login/login.do";
    //行政统计
    private String areaStatisticApi ="https://hzz1.bjchy.gov.cn/HTRCService/api/api-pda/rvhealth/statlist";
    //管属统计
    private String manageAreaApi =   "https://hzz1.bjchy.gov.cn/HTRCService/api/api-pda/rvhealth/statlist";

    /**
     * 只会健康码
     */

    @XxlJob("HealthRiverJob")
    public void demoJobHandler() throws Exception {
        String params = XxlJobHelper.getJobParam();
        if (StrUtil.isNotEmpty(params)){
            String[] split = params.split("-");
            String start = split[0];
            String end = split[1];
            DateTime startDate = DateUtil.parse(start, "yyyyMMdd");
            DateTime endDate = DateUtil.parse(end, "yyyyMMdd");
            List<DateTime> dateTimes = DateUtil.rangeToList(startDate, endDate, DateField.MONTH);
            for (DateTime dateTime : dateTimes) {
                syncHealthStatus(dateTime);
                System.out.println("健康河湖 健康码采集完成"+dateTime);
                log.info("健康河湖 健康码采集完成"+dateTime);
            }
        }else {
            syncHealthStatus(null);
            System.out.println("健康河湖 健康码采集完成");
            log.info("健康河湖 健康码采集完成");
        }

    }


//    @Scheduled(cron = "0 15 1 * * ?")
    public void syncHealthStatus(DateTime dateTime) {//如果 场站警戒状态不为空 则
        //登陆获取 appKey
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("account","cyqhzb");
        paramMap.put("password","CHYhzz123!");
        paramMap.put("module",3);
        log.info("开始请求登陆接口");
        String body = HttpRequest.post(loginUrl)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .form(paramMap)
                .timeout(90000)
                .execute().body();
        JSONObject jsonObject = JSONObject.parseObject(body);
        String apiKey = "";
        String userId = "";
        if (Objects.nonNull(jsonObject)){
            log.info("请求响应参数为:========>"+jsonObject);
            Integer status = jsonObject.getInteger("status");
            if (status.equals(1)){
                log.info("请求成功 切状态为===================>"+status);
                JSONObject result = jsonObject.getJSONObject("result");
                apiKey = result.getString("apiKey");
                userId = result.getString("userId");
            }else {
                log.info("请求失败 切状态为====================>"+status);
                return;
            }
        }

        //请求 行政统计

        DateTime monthDate = dateTime == null ? DateUtil.offsetMonth(new Date(), -1) :DateUtil.offsetMonth(dateTime, -1);
        String ym = DateUtil.format(monthDate, "yyyy-MM-dd").substring(0, 7);
        long start = DateUtil.beginOfMonth(monthDate).getTime();
        long end = DateUtil.endOfMonth(monthDate).getTime();
        Map<String,Object> param1 = new HashMap<>();
        param1.put("apiKey",apiKey);
        param1.put("userId",userId);
        param1.put("stm",start);
        param1.put("etm",end);
        param1.put("type",1);
        log.info("开始请求行政统计");
        String areaStatisticBody = HttpRequest.get(areaStatisticApi).form(param1).timeout(90000).execute().body();
        if (StrUtil.isNotEmpty(areaStatisticBody)){
            HealthRiverReciveDto healthRiverReciveDto = null;
            try {
                healthRiverReciveDto = JSONObject.parseObject(areaStatisticBody, HealthRiverReciveDto.class);
                log.info("正确  接收到的 行政统计数据为 =======================》"+healthRiverReciveDto);
                List<DataInfoDto> result = healthRiverReciveDto.getResult();
                for (DataInfoDto dataInfoDto : result) {
                    AreaHealthDataInfoEntity dataInfoEntity = new AreaHealthDataInfoEntity();
                    dataInfoEntity.setAdnm(dataInfoDto.getAdnm());
                    dataInfoEntity.setCntGreenCode(dataInfoDto.getCntGreenCode());
                    dataInfoEntity.setCntRedCode(dataInfoDto.getCntRedCode());
                    dataInfoEntity.setCntYellowCode(dataInfoDto.getCntYellowCode());
                    log.info("遍历入库 区政"+dataInfoDto);
                    QueryWrapper wrapper = new QueryWrapper();
                    wrapper.eq("type",1);
                    wrapper.eq("adnm",dataInfoDto.getAdnm());
                    wrapper.eq("ym",ym);
                    AreaHealthDataInfoEntity areaHealthDataInfoEntity = areaHealthDataInfoDao.selectOne(wrapper);
                    dataInfoEntity.setYm(ym);
                    dataInfoEntity.setType(1);

                    log.info("入库的数据是-============================》"+dataInfoEntity);
                    if (areaHealthDataInfoEntity != null){
                        areaHealthDataInfoDao.update(dataInfoEntity,wrapper);
                    }else {
                        areaHealthDataInfoDao.insert(dataInfoEntity);
                    }
                }
            } catch (Exception e) {
                log.info("报错  接收到的 行政统计数据为 =======================》"+healthRiverReciveDto);
                throw new RuntimeException(e);
            }
        }

        //开始请求 管区
        Map<String,Object> param2 = new HashMap<>();
        param2.put("apiKey",apiKey);
        param2.put("userId",userId);
        param2.put("stm",start);
        param2.put("etm",end);
        param2.put("type",2);
        log.info("开始请求管属统计");
        String manageBody = HttpRequest.get(manageAreaApi).form(paramMap).timeout(90000).execute().body();
        if (StrUtil.isNotEmpty(manageBody)){
            HealthRiverBelongDto healthRiverReciveDto = null;
            try {
                healthRiverReciveDto = JSONObject.parseObject(manageBody, HealthRiverBelongDto.class);
                log.info("正确  接收到的 管属统计数据为 =======================》"+healthRiverReciveDto);
                List<ManageBelongDto> result = healthRiverReciveDto.getResult();
                for (ManageBelongDto dataInfoDto : result) {
                    AreaHealthDataInfoEntity dataInfoEntity = new AreaHealthDataInfoEntity();
                    dataInfoEntity.setAdnm(dataInfoDto.getPrjadminnm());
                    dataInfoEntity.setCntGreenCode(dataInfoDto.getCntGreenCode());
                    dataInfoEntity.setCntRedCode(dataInfoDto.getCntRedCode());
                    dataInfoEntity.setCntYellowCode(dataInfoDto.getCntYellowCode());
                    log.info("遍历入库 管属"+dataInfoDto);
                    QueryWrapper wrapper = new QueryWrapper();
                    wrapper.eq("type",2);
                    wrapper.eq("adnm",dataInfoDto.getPrjadminnm());
                    wrapper.eq("ym",ym);
                    AreaHealthDataInfoEntity areaHealthDataInfoEntity = areaHealthDataInfoDao.selectOne(wrapper);
                    dataInfoEntity.setYm(ym);
                    dataInfoEntity.setType(2);

                    log.info("入库的管属数据是-============================》"+dataInfoEntity);
                    if (areaHealthDataInfoEntity != null){
                        areaHealthDataInfoDao.update(dataInfoEntity,wrapper);
                    }else {
                        areaHealthDataInfoDao.insert(dataInfoEntity);
                    }
                }
            } catch (Exception e) {
                log.info("报错  接收到的 管属统计数据为 =======================》"+healthRiverReciveDto);
                throw new RuntimeException(e);
            }
        }
    }
}
