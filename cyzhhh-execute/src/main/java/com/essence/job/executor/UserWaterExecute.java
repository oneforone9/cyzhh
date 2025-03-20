package com.essence.job.executor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.UserWaterDao;
import com.essence.entity.UserWaterDto;
import com.essence.entity.UserWaterThirdDto;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

/**
 * 用户水
 * 2023-01-01,2023-01-05
 */
@Component
public class UserWaterExecute {
    @Resource
    private UserWaterDao userWaterDao;

    private static final String API = "http://172.26.69.98:8080/beijing_water/report/getLswi";


    @XxlJob("UserWaterExecute")
    public void demoJobHandler() throws Exception {
        String params = XxlJobHelper.getJobParam();
        if (StrUtil.isNotEmpty(params)){
            String[] split = params.split(",");
            String start = split[0];
            String end = split[1];
            DateTime startDate = DateUtil.parse(start, "yyyy-MM-dd");
            DateTime endDate = DateUtil.parse(end, "yyyy-MM-dd");
            List<DateTime> dateTimes = DateUtil.rangeToList(startDate, endDate, DateField.DAY_OF_YEAR);
            for (DateTime dateTime : dateTimes) {
                DateTime offPreDay = DateUtil.offsetDay(dateTime, -1);
                String format =  DateUtil.format(offPreDay, "yyyy-MM-dd");
                doExecute(format,format);
            }
        }else {
            String format = DateUtil.format( DateUtil.offsetDay(new Date(),-1) , "yyyy-MM-dd");
            doExecute(format,format);
        }

    }


    public void doExecute(String time,String stime){
        Map<String,Object> param1 = new HashMap<>();
        param1.put("time",time);
        param1.put("orgCd","北京市朝阳区水务局");
        param1.put("wiunm_guimo",null);
        param1.put("stime",stime);
        param1.put("pageNow",1);
        param1.put("pageSize",1000);
        param1.put("sortname","sortName");
        param1.put("sortorder","asc");

        System.out.println("开始请求行政统计");
        HttpResponse response = HttpRequest.post(API).form(param1).timeout(100000).execute();
        int statusCode = response.getStatus();
        if (statusCode != 200) {
            System.err.println("请求行政统计-，用户水量，HTTP状态码: " + statusCode);
            return;
        }
        String body = response.body();
        System.out.println( "请求到的数据是+======================="+body);
        List<UserWaterDto> list = new ArrayList<>();
        if (StrUtil.isNotEmpty(body)){
            JSONObject jsonObject = JSONObject.parseObject(body);
            JSONArray jsonArray = jsonObject.getJSONArray("Rows");
            System.out.println("获取到的 json 数组 是================"+jsonArray);
            List<UserWaterThirdDto> userWaterThirdDtos = jsonArray.toJavaList(UserWaterThirdDto.class);
            System.out.println("转换java 数据成功 ");

            if (CollUtil.isNotEmpty(userWaterThirdDtos)){
                for (UserWaterThirdDto userWaterThirdDto : userWaterThirdDtos) {
                    UserWaterDto userWaterDto = new UserWaterDto();
                    userWaterDto.setWater(userWaterThirdDto.getWw() == null ? "0" : userWaterThirdDto.getWw().toString());
                    userWaterDto.setMnWater(userWaterThirdDto.getDayw() == null ? "0" : userWaterThirdDto.getDayw().toString());
                    userWaterDto.setDate(time);
                    userWaterDto.setUserName(userWaterThirdDto.getWiuNm());
                    userWaterDto.setOutNum(userWaterThirdDto.getWainNum() == null ? "0" : userWaterThirdDto.getWainNum().toString());
                    userWaterDto.setUpdateTime(DateUtil.format(new Date() , "yyyy-MM-dd"));
                    list.add(userWaterDto);
                }
            }
        }

        //存库
        if (CollUtil.isNotEmpty(list)){
            System.out.println("放入list===================="+list +list.size());
            for (UserWaterDto userWaterDto : list) {
                QueryWrapper wrapper = new QueryWrapper();
                wrapper.eq("user_name",userWaterDto.getUserName());
                wrapper.eq("date",userWaterDto.getDate());
                List<UserWaterDto> list1 = userWaterDao.selectList(wrapper);
                if (CollUtil.isNotEmpty(list1)){
                    userWaterDao.update(userWaterDto,wrapper);
                    System.out.println("更新实体===================="+userWaterDto);
                }else {
                    userWaterDao.insert(userWaterDto);
                    System.out.println("新增实体===================="+userWaterDto);
                }
            }
        }

        System.out.printf("执行成功 ====================");

    }

}
