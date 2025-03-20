package com.essence.user;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.UserWaterBaseInfoDao;
import com.essence.dao.UserWaterDao;
import com.essence.dao.entity.UserWaterBaseInfoDto;
import com.essence.dao.entity.UserWaterDto;
import com.essence.dto.UserWaterThirdDto;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户水
 * 2023-01-01,2023-01-05
 */
@Component
public class UserWaterExecute {
    @Resource
    private UserWaterDao userWaterDao;
    @Resource
    private UserWaterBaseInfoDao userWaterBaseInfoDao;

    private static final String API = "http://172.26.69.98:8080/beijing_water/report/getLswi";

    @Scheduled(cron = "0 0/40 * * * ?")
    @PostConstruct
    public void demoJobHandler() throws Exception {

        String format = DateUtil.format(DateUtil.offsetMonth(new Date(), -1), "yyyy-MM-dd");
        doExecute(format, format);

    }


    public void doExecute(String time, String stime) throws UnirestException {
        List<UserWaterBaseInfoDto> userWaterBaseInfoDtos = userWaterBaseInfoDao.selectList(new QueryWrapper<>());
        //电子号码
        Map<String, List<UserWaterBaseInfoDto>> eleNumMapList = userWaterBaseInfoDtos.parallelStream().filter(p -> null != p.getEleNum()).collect(Collectors.groupingBy(UserWaterBaseInfoDto::getEleNum));
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.get("http://82.157.41.91:8787/cyjj/api/getMonthWaterRecord.do?dt=" + time.substring(0, 7)).header("Authorization", "Bearer 4503D9F3-D2BE-4172-A0A7-6BA5A3B0DA1B").asString();
        int statusCode = response.getStatus();
        if (statusCode != 200) {
            System.err.println("请求用户水- 82. HTTP状态码: " + statusCode);
            return;
        }
        String body = response.getBody();
        extracted(time, body, eleNumMapList);
    }

    private void extracted(String time, String body, Map<String, List<UserWaterBaseInfoDto>> eleNumMap) {
        List<UserWaterDto> list = new ArrayList<>();
        if (StrUtil.isNotEmpty(body)) {
            JSONObject jsonObject = JSONObject.parseObject(body);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            List<UserWaterThirdDto> userWaterThirdDtos = jsonArray.toJavaList(UserWaterThirdDto.class);
            if (CollUtil.isNotEmpty(userWaterThirdDtos)) {
                for (UserWaterThirdDto userWaterThirdDto : userWaterThirdDtos) {
                    String monthW = userWaterThirdDto.getMonthW();
                    String mpcd = userWaterThirdDto.getMpCd();
                    List<UserWaterBaseInfoDto> userWaterBaseInfoDtos1 = eleNumMap.get(mpcd);
                    if (CollUtil.isNotEmpty(userWaterBaseInfoDtos1)) {
                        UserWaterBaseInfoDto userWaterBaseInfoDto = userWaterBaseInfoDtos1.get(0);
                        String userName = userWaterBaseInfoDto.getUserName();
                        String promiseQuantity = userWaterBaseInfoDto.getPromiseQuantity();
                        UserWaterDto userWaterDto = new UserWaterDto();
                        userWaterDto.setWater(promiseQuantity);
                        userWaterDto.setMnWater(new BigDecimal(monthW).divide(new BigDecimal(10000), 4, BigDecimal.ROUND_HALF_UP).toString());
                        userWaterDto.setDate(time.substring(0, 7));
                        userWaterDto.setUserName(userName);
                        userWaterDto.setOutNum(String.valueOf(userWaterBaseInfoDtos1.size()));
                        userWaterDto.setUpdateTime(DateUtil.format(new Date(), "yyyy-MM-dd"));
                        list.add(userWaterDto);
                    }
                }
            }
        }
        //存库
        if (CollUtil.isNotEmpty(list)) {
            System.out.println("放入list====================" + list + list.size());
            for (UserWaterDto userWaterDto : list) {
                QueryWrapper wrapper = new QueryWrapper();
                wrapper.eq("user_name", userWaterDto.getUserName());
                wrapper.eq("date", userWaterDto.getDate());
                wrapper.eq("water", userWaterDto.getWater());
                List<UserWaterDto> list1 = userWaterDao.selectList(wrapper);
                if (CollUtil.isNotEmpty(list1)) {
                    userWaterDao.update(userWaterDto, wrapper);
                    System.out.println("更新实体====================" + userWaterDto);
                } else {
                    userWaterDao.insert(userWaterDto);
                    System.out.println("新增实体====================" + userWaterDto);
                }
            }
        }
        System.out.printf("执行成功 ====================");
    }

}
