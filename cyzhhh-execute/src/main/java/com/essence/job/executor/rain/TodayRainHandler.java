package com.essence.job.executor.rain;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.StRainDailyDao;
import com.essence.dao.StRainDateDao;
import com.essence.dao.entity.StRainDailyDto;
import com.essence.dao.entity.StRainDateDto;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TodayRainHandler {

    @Autowired
    private StRainDateDao stRainDateDao;

    @Resource
    private StRainDailyDao stRainDailyDao;

    @XxlJob("TodayRainHandler")
    public void execute() throws UnirestException {
        getTodayRainHandler();
    }

    public void getTodayRainHandler() {
        //当日雨量 如果在早上8点之前 则查询 前一天的早上8点到现在
        //如果在早上8点后 则查询今天早上8点到现在时间雨量累加

        //现在的时间
        Date date = new Date();
        //当前时间
        String format1 = DateUtil.format(date, "yyyy-MM-dd HH:mm:ss");
        String substring = format1.substring(0, format1.indexOf(":") - 2);
        //今天的8点时间 定点
        String dailyPosition = substring+"08:00:00";
        DateTime parse = DateUtil.parse(dailyPosition);

        //如果 当前时间 小于 今天的早上8点 则查询时间是 往前推一天的早上8点 到现在的时间
        if (date.getTime() < parse.getTime()) {
            //开始时间往前推1天的早上8 点 到现在的时间
            DateTime dateTime = DateUtil.offsetDay(date, -1);
            String format2 = DateUtil.format(dateTime, "yyyy-MM-dd HH:mm:ss");
            String ymdh = format2.substring(0, format1.indexOf(":") - 2) + " " + "08:00:00";
            DateTime startPre = DateUtil.parse(ymdh);
            getDailyRain(startPre, date);

        } else {
            //否则 则是今天早上8点到现在的时间点
            DateTime dailyStart = DateUtil.parse(dailyPosition);
            getDailyRain(dailyStart, date);
        }
    }

    public void getDailyRain(Date start ,Date end){
        QueryWrapper queryWrapper = new QueryWrapper();
        Date date = new Date();
        queryWrapper.le("Date",end);
        queryWrapper.ge("Date",start);
        List<StRainDateDto> list = stRainDateDao.selectList(queryWrapper);
        List<StRainDailyDto> res = new ArrayList<>();
        if (CollUtil.isNotEmpty(list)){
            Map<String, List<StRainDateDto>> listMap = list.parallelStream().collect(Collectors.groupingBy(StRainDateDto::getStationId));
            for (String s : listMap.keySet()) {
                List<StRainDateDto> stRainDateDtos = listMap.get(s);
                BigDecimal rainValue = new BigDecimal(0);
                for (StRainDateDto stRainDateDto : stRainDateDtos) {
                    String value = stRainDateDto.getHhRain().equals("9999") ? "0" : stRainDateDto.getHhRain();
                    rainValue = rainValue.add(new BigDecimal(value));
                }
                StRainDailyDto stRainDailyDto = new StRainDailyDto();
                stRainDailyDto.setStationId(s);
                stRainDailyDto.setValue(rainValue.toString());
                stRainDailyDto.setDate(DateUtil.format(date,"yyyy-MM-dd"));
                res.add(stRainDailyDto);
            }
        }

        if (CollUtil.isNotEmpty(res)){
            for (StRainDailyDto re : res) {
                String stationId = re.getStationId();
                String date1 = re.getDate();
                QueryWrapper wrapper = new QueryWrapper();
                wrapper.eq("station_id",stationId);
                wrapper.eq("date",date1);
                List<StRainDailyDto> stRainDailyDtos = stRainDailyDao.selectList(wrapper);
                if (CollUtil.isNotEmpty(stRainDailyDtos)){
                    stRainDailyDao.update(re,wrapper);
                }else {
                    stRainDailyDao.insert(re);
                }
            }
        }
    }
}
