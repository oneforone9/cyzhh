package com.essence.job.executor.rain;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.essence.dao.RainDateDtoDao;
import com.essence.dao.StRainDateHourDao;
import com.essence.dao.entity.RainDateDto;
import com.essence.dao.entity.StRainDateHour;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 小时统计雨量入库
 */
@Component
public class RainCountHourHandler {

    @Resource
    private RainDateDtoDao rainDateDtoDao;

    @Resource
    private StRainDateHourDao stRainDateHourDao;

    @XxlJob("RainCountHourHandler")
    public void execute() throws UnirestException {
        doRainHourData(XxlJobHelper.getJobParam());
    }

    public void doRainHourData(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime;
        if (StringUtils.isNotBlank(date)) {
            try {
                localDateTime = LocalDateTime.parse(date, formatter).withMinute(0).withSecond(0);
            } catch (DateTimeParseException e) {
                throw new RuntimeException("时间参数格式错误: yyyy-MM-dd HH:mm:ss");
            }
        } else {
            localDateTime = LocalDateTime.now().minusHours(1).withMinute(0).withSecond(0).withNano(0);
        }
        Map<String, List<RainDateDto>> rainDateList = rainDateDtoDao.selectList(new LambdaQueryWrapper<RainDateDto>().ge(RainDateDto::getDate, formatter.format(localDateTime)).le(RainDateDto::getDate, formatter.format(localDateTime.withMinute(59).withSecond(59)))).parallelStream().collect(Collectors.groupingBy(RainDateDto::getStationID));

        List<StRainDateHour> hourList = new ArrayList<>();
        for (String stationId : rainDateList.keySet()) {
            BigDecimal hourNum = rainDateList.get(stationId).parallelStream().map(stRainDateDto -> stRainDateDto.getHhRain().equals("9999") ? "0" : stRainDateDto.getHhRain()).map(BigDecimal::new).reduce(BigDecimal.ZERO, BigDecimal::add);
            StRainDateHour hour = new StRainDateHour();
            hour.setId(UUID.randomUUID().toString().replace("-", ""));
            Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
            hour.setDate(new Date(instant.getEpochSecond() * 1000));
            hour.setStationId(stationId);
            hour.setHourRain(hourNum);
            hourList.add(hour);
        }
        if (CollectionUtils.isNotEmpty(hourList)) {
            stRainDateHourDao.delete(new LambdaQueryWrapper<StRainDateHour>().eq(StRainDateHour::getDate, formatter.format(localDateTime)));
            hourList.forEach(v -> stRainDateHourDao.insert(v));
        }
    }
}
