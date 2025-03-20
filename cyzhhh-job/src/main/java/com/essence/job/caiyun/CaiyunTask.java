package com.essence.job.caiyun;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.StCaiyunMeshDao;
import com.essence.dao.StCaiyunPrecipitationRealDao;
import com.essence.dao.entity.caiyun.StCaiyunMeshDto;
import com.essence.dao.entity.caiyun.StCaiyunPrecipitationRealDto;
import com.essence.interfaces.api.StCaiyunPrecipitationHistoryService;
import com.essence.interfaces.api.StCaiyunPrecipitationRealService;
import com.essence.interfaces.model.StCaiyunPrecipitationHistoryEsu;
import com.essence.interfaces.model.StCaiyunPrecipitationRealEsu;
import com.essence.service.utils.MapReduceUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@Log4j2
public class CaiyunTask {

    @Autowired
    private StCaiyunPrecipitationHistoryService stCaiyunPrecipitationHistoryService;
    @Autowired
    private StCaiyunPrecipitationRealService stCaiyunPrecipitationRealService;
    @Autowired
    private StCaiyunMeshDao stCaiyunMeshDao;
    @Autowired
    private StCaiyunPrecipitationRealDao stCaiyunPrecipitationRealDao;

    @Transactional
    @Scheduled(cron = "0 0 0/2 * * ? ")
    public void syncCreateCaiyunTask() {
        StCaiyunMeshDto caiYunEnable = stCaiyunMeshDao.kvList("caiYunEnable");
        if (null != caiYunEnable) {
            String value = caiYunEnable.getLttd();
            log.info("彩云天气定时任务准备执行,setting表中的value={}", value);
            if (value.equals("false")) {
                return;
            }
        } else {
            stCaiyunMeshDao.insertKeyValue("caiYunEnable", "false");
            return;
        }
        log.info("彩云天气执行开始,时间是" + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        long start = System.currentTimeMillis();
        Date date = new Date();
        date.setHours(date.getHours() - 1);
        date.setMinutes(30);
        List<StCaiyunMeshDto> stCaiyunMeshDtos = stCaiyunMeshDao.selectList(new QueryWrapper<>());
        List<StCaiyunPrecipitationHistoryEsu> stCaiyunPrecipitationHistoryEsuList = new CopyOnWriteArrayList<>();
        List<StCaiyunPrecipitationRealEsu> stCaiyunPrecipitationRealEsuList = new CopyOnWriteArrayList<>();
        MapReduceUtil.handle(stCaiyunMeshDtos, stCaiyunMeshDtoUint -> {
            stCaiyunMeshDtoUint.forEach(stCaiyunMeshDto -> {
                String meshId = stCaiyunMeshDto.getMeshId();
                String lgtdGcj = stCaiyunMeshDto.getLgtdGcj();
                String lttdGcj = stCaiyunMeshDto.getLttdGcj();
                // 2024-06-06  变更之前的链接
                // 变更前   https://api.caiyunapp.com/v2.6/hOMxAh9haO7XuvBA/
                // 变更后  https://api.caiyunapp.com/v2.5/SP0FYDHsJOdya10d/
                String url = "https://api.caiyunapp.com/v2.5/SP0FYDHsJOdya10d/" + lgtdGcj + "," + lttdGcj + "/hourly?hourlysteps=48";
                process(meshId, url, stCaiyunPrecipitationHistoryEsuList, stCaiyunPrecipitationRealEsuList);
            });
            return null;
        });
        stCaiyunPrecipitationRealService.saveOrUpdate(stCaiyunPrecipitationRealEsuList);
        log.info("stCaiyunPrecipitationReal保存成功" + new Date());
        stCaiyunPrecipitationHistoryService.saveOrUpdate(stCaiyunPrecipitationHistoryEsuList);
        log.info("stCaiyunPrecipitationHistory保存成功" + new Date());
        stCaiyunPrecipitationRealDao.delete(new QueryWrapper<StCaiyunPrecipitationRealDto>().le("drp_time", date));
        log.info("彩云天气执耗时：" + (System.currentTimeMillis() - start) + "ms");
        log.info("彩云天气执行结束,时间是" + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
    }

    private void process(String meshid, String url, List<StCaiyunPrecipitationHistoryEsu> stCaiyunPrecipitationHistoryEsuList, List<StCaiyunPrecipitationRealEsu> stCaiyunPrecipitationRealEsuList) {
        OkHttpUtils.syncGet(url, body -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmXXX");
            JSONArray jsonArray = JSONObject.parseObject(body).getJSONObject("result").getJSONObject("hourly").getJSONArray("precipitation");
            Random random = new Random();
            for (Object json : jsonArray) {
                if (json instanceof JSONObject) {
                    String value = ((JSONObject) json).get("value").toString();
                    String datetime = ((JSONObject) json).get("datetime").toString();
                    try {
                        Date parse = simpleDateFormat.parse(datetime);
                        stCaiyunPrecipitationHistoryEsuList.add(new StCaiyunPrecipitationHistoryEsu().setId(UUID.randomUUID().toString().replace("-", "")).setMeshId(meshid)
                                .setDrp(value).setDrpTime(parse).setCreateTime(new Date()));
                        stCaiyunPrecipitationRealEsuList.add(new StCaiyunPrecipitationRealEsu().setId(UUID.randomUUID().toString().replace("-", "")).setMeshId(meshid)
                                .setDrp(value).setDrpTime(parse).setCreateTime(new Date()));
//                        stCaiyunPrecipitationRealEsuList.add(new StCaiyunPrecipitationRealEsu().setId(UUID.randomUUID().toString().replace("-", "")).setMeshId(meshid)
//                                .setDrp(random.nextInt(21)+"."+ random.nextInt(9)).setDrpTime(parse).setCreateTime(new Date()));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        });
    }


}
