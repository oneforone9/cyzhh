package com.essence.job.executor.rain;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.RainDateDtoDao;
import com.essence.dao.RainDayCountDao;
import com.essence.dao.entity.RainDateDto;
import com.essence.entity.RainDayCountDto;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 一天统计雨量入库
 */
@Component
public class RainCountDayHandler {

    @Resource
    private RainDateDtoDao rainDateDtoDao;

    @Resource
    private RainDayCountDao rainDayCountDao;

    @XxlJob("RainCountDayHandler")
    public void execute() throws UnirestException {
        doRainHourData();
    }

    public  void doRainHourData(){
        Date date = new Date();
        String dateStr = DateUtil.format(date, "yyyy-MM-dd");
        DateTime beginOfDay = DateUtil.beginOfDay(date);
        DateTime endOfDay = DateUtil.endOfDay(date);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.le("date",endOfDay);
        wrapper.ge("date",beginOfDay);
        List<RainDateDto> rainDateDtos = rainDateDtoDao.selectList(wrapper);
        List<RainDayCountDto> list = new ArrayList<>();
        if (CollUtil.isNotEmpty(rainDateDtos)){
            Map<String, List<RainDateDto>> collect = rainDateDtos.parallelStream().collect(Collectors.groupingBy(RainDateDto::getStationID));
            for (String stationId : collect.keySet()) {
                List<RainDateDto> rainDateDtos1 = collect.get(stationId);
                BigDecimal hourNum = rainDateDtos1.parallelStream().map(stRainDateDto -> {
                    if (stRainDateDto.getHhRain().equals("9999")) {
                        return "0";
                    } else {
                        return stRainDateDto.getHhRain();
                    }
                }).map(s -> new BigDecimal(s)).reduce(BigDecimal.ZERO,BigDecimal::add);
                RainDayCountDto rainDayCountDto = new RainDayCountDto();
                rainDayCountDto.setStationID(stationId);
                rainDayCountDto.setDayCount(hourNum);
                rainDayCountDto.setDate(dateStr);
                list.add(rainDayCountDto);
            }
        }
        if (CollUtil.isNotEmpty(list)){
            for (RainDayCountDto rainDayCountDto : list) {
                QueryWrapper wrapper1 = new QueryWrapper();
                wrapper1.eq("date",rainDayCountDto.getDate());
                wrapper1.eq("station_id",rainDayCountDto.getStationID());
                List<RainDayCountDto> list1 = rainDayCountDao.selectList(wrapper1);
                if (CollUtil.isEmpty(list1)){
                    rainDayCountDao.insert(rainDayCountDto);
                }else {
                    rainDayCountDao.update(rainDayCountDto,wrapper1);
                }
            }
        }

    }
}
