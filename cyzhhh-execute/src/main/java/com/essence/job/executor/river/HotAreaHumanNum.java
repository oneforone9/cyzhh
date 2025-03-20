package com.essence.job.executor.river;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.StCrowdDateDao;
import com.essence.dto.HumanHotsDto;
import com.essence.entity.StCrowdDateDto;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@Component
public class HotAreaHumanNum {
    @Resource
    private StCrowdDateDao stCrowdDateDao;
    private static final String API = "http://sw.swj.beijing.gov.cn/uacp/datainterface/getdata//bootstraptable/-1/DRSYZDQYRS-202061717913";

    @XxlJob("HotAreaHumanNum")
    public void demoJobHandler() throws Exception {
        String params = XxlJobHelper.getJobParam();
        if (StrUtil.isNotEmpty(params)){
            String[] split = params.split(",");
            String start = split[0];
            String end = split[1];
            DateTime startDate = DateUtil.parse(start, "yyyyMMdd");
            DateTime endDate = DateUtil.parse(end, "yyyyMMdd");
            List<DateTime> dateTimes = DateUtil.rangeToList(startDate, endDate, DateField.DAY_OF_YEAR);
            for (DateTime dateTime : dateTimes) {
                DateTime offPreDay = DateUtil.offsetDay(dateTime, -1);
                String format =  DateUtil.format(offPreDay, "yyyy-MM-dd");
                String reqTime =  DateUtil.format(offPreDay, "yyyyMMdd");
                doExecute(reqTime);
            }
        }else {
            String reqTime = DateUtil.format( new Date() , "yyyyMMdd");
            doExecute(reqTime);
        }

    }

    public void doExecute(String reqTime){
        Map<String,Object> param1 = new HashMap<>();

        param1.put("ssq","朝阳区");
        param1.put("times",reqTime);

        System.out.println("开始请求热点数据");
        String body = HttpRequest.get(API).form(param1).timeout(90000).execute().body();
        List<StCrowdDateDto> stCrowdDateDtos = new ArrayList<>();
        if (StrUtil.isNotEmpty(body)){
            JSONObject jsonObject = JSONObject.parseObject(body);
            JSONArray rows = jsonObject.getJSONArray("rows");
            List<HumanHotsDto> humanHotDtos = rows.toJavaList(HumanHotsDto.class);
            for (HumanHotsDto humanHotDto : humanHotDtos) {
                StCrowdDateDto  stCrowdDateDto = new StCrowdDateDto();
                stCrowdDateDto.setDate(DateUtil.format(new Date(),"yyyy-MM-dd"));
                stCrowdDateDto.setArea(humanHotDto.getVALUE1());
                stCrowdDateDto.setNum(Integer.valueOf(humanHotDto.getVALUE7()) );
                stCrowdDateDto.setWaterUnit(humanHotDto.getVALUE2());
                stCrowdDateDto.setRvnm(humanHotDto.getVALUE());
                stCrowdDateDtos.add(stCrowdDateDto);
            }
        }

        if (CollUtil.isNotEmpty(stCrowdDateDtos)){
            for (StCrowdDateDto stCrowdDateDto : stCrowdDateDtos) {
                String date = stCrowdDateDto.getDate();
                String area = stCrowdDateDto.getArea();
                QueryWrapper wrapper = new QueryWrapper();
                wrapper.eq("date",date);
                wrapper.eq("area",area);
                List<StCrowdDateDto> stCrowdDateDtos1 = stCrowdDateDao.selectList(wrapper);
                if (CollUtil.isNotEmpty(stCrowdDateDtos1)){
                    stCrowdDateDao.update(stCrowdDateDto,wrapper);
                }else {
                    stCrowdDateDao.insert(stCrowdDateDto);
                }
            }
        }
        System.out.println("请求热点数据完成");

    }


}
