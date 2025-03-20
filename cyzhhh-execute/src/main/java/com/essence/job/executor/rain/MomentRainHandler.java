package com.essence.job.executor.rain;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.RainDateDtoDao;
import com.essence.dao.RainDateOrgDao;
import com.essence.dao.entity.RainDateDto;
import com.essence.dao.entity.RainDateOrgDto;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 分钟雨量入库
 */
@Component
public class MomentRainHandler {

    @Resource
    private RainDateDtoDao rainDateDtoDao;

    @Resource
    private RainDateOrgDao rainDateOrgDao;


    @XxlJob("MomentRainHandler")
    public void demoJobHandler() throws Exception {
        String params = XxlJobHelper.getJobParam();
        if (StrUtil.isNotEmpty(params)) {
            String[] split = params.split(",");
            String start = split[0];
            String end = split[1];
            DateTime startDate = DateUtil.parse(start, "yyyy-MM-dd");
            DateTime endDate = DateUtil.parse(end, "yyyy-MM-dd");
            List<DateTime> dateTimes = DateUtil.rangeToList(startDate, endDate, DateField.DAY_OF_YEAR);
            for (DateTime dateTime : dateTimes) {
                DateTime time1 = DateUtil.beginOfDay(dateTime);
                DateTime time2 = DateUtil.endOfDay(dateTime);
                execute(time1, time2);
            }
        } else {
            Date dateTime1 = new Date();
            DateTime dateTime2 = DateUtil.offsetMinute(dateTime1, -5);
            execute(dateTime2, dateTime1);
        }
        System.out.println("执行完成!");
    }


    public void execute(Date start, Date end) {
        List<RainDateOrgDto> rainOrgList = rainDateOrgDao.selectList(
                new LambdaQueryWrapper<RainDateOrgDto>()
                        .le(RainDateOrgDto::getDate,end)
                        .ge(RainDateOrgDto::getDate,start)
        );

        List<RainDateOrgDto> newRainOrgList = new ArrayList<>();
        if (CollUtil.isNotEmpty(rainOrgList)) {
            Map<String, List<RainDateOrgDto>> rainOrgMap = rainOrgList.parallelStream().collect(Collectors.groupingBy(RainDateOrgDto::getStationID));
            for (String stationId : rainOrgMap.keySet()) {
                System.out.println(stationId + "开始执行!");
                //根据时间点进行排序
                List<RainDateOrgDto> rainList = rainOrgMap.get(stationId).stream().sorted(Comparator.comparing(RainDateOrgDto::getDate)).collect(Collectors.toList());
                for (int i = 0; i < rainList.size(); i++) {
                    RainDateOrgDto rainDateDto = rainList.get(i);
                    String format1 = DateUtil.format(rainDateDto.getDate(), "yyyy-MM-dd HH:mm:ss");
                    //得到 mm:ss
                    String substring = format1.substring(14, format1.length());
                    if (i + 1 >= rainList.size()) {
                        break;
                    }
                    if (substring.equals("00:00")) {
                        //代表整点 等于0
                        rainDateDto.setHhRain("0");
                        newRainOrgList.add(rainDateDto);
                        continue;
                    }
                    if (substring.equals("01:00")) {
                        //表示每小时的 01 分钟点 ；等于他自己
                        newRainOrgList.add(rainDateDto);
                        continue;
                    }
                    //其余全是后一个减去当前数值
                    String beforeRain = rainDateDto.getHhRain();
                    String backRain = rainList.get(i + 1).getHhRain();
                    String value = "0";
                    if (StrUtil.isNotEmpty(backRain) && StrUtil.isNotEmpty(beforeRain)) {
                        backRain = backRain.equals("9999") ? "0" : backRain;
                        beforeRain = beforeRain.equals("9999") ? "0" : beforeRain;
                        value = new BigDecimal(backRain).subtract(new BigDecimal(beforeRain)).toString();
                    }
                    if (Double.valueOf(value) < 0) {
                        value = "0";
                    }
                    rainDateDto.setHhRain(value);
                    newRainOrgList.add(rainDateDto);
                }
                System.out.println(stationId + "执行完成!");
            }
        }

        //对修改的数值进行通过id 进行update
        for (RainDateOrgDto re : newRainOrgList) {
            RainDateDto rainDateDto = new RainDateDto();
            rainDateDto.setDate(re.getDate());
            rainDateDto.setStationID(re.getStationID());
            rainDateDto.setHhRain(re.getHhRain());
            QueryWrapper wrapper1 = new QueryWrapper();
            wrapper1.eq("date", re.getDate());
            wrapper1.eq("station_id", re.getStationID());
            List<RainDateDto> rainDateDtos1 = rainDateDtoDao.selectList(wrapper1);
            if (CollUtil.isNotEmpty(rainDateDtos1)) {
                rainDateDtoDao.update(rainDateDto, wrapper1);
            } else {
                rainDateDtoDao.insert(rainDateDto);
            }
        }
    }

}
