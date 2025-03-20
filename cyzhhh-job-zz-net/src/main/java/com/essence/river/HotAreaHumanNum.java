//package com.essence.river;
//
//import cn.hutool.core.collection.CollUtil;
//
//import cn.hutool.core.date.DateUtil;
//import cn.hutool.core.util.StrUtil;
//import cn.hutool.http.HttpRequest;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.essence.dao.StCrowdDateDao;
//
//import com.essence.dao.entity.StCrowdDateDto;
//import com.essence.dto.HumanHotDto;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.util.*;
//
//@Component
//public class HotAreaHumanNum {
//    @Resource
//    private StCrowdDateDao stCrowdDateDao;
//    private static final String API = "http://sw.swj.beijing.gov.cn/uacp/datainterface/getdata//bootstraptable/-1/DRSYZDQYRS-202061717913";
//
//    @Scheduled(cron = "0 30 * * * ?")
//    public void demoJobHandler() throws Exception {
//        String reqTime = DateUtil.format( DateUtil.offsetDay(new Date(),-1) , "yyyyMMdd");
//        doExecute(reqTime);
//
//
//    }
//
//    public void doExecute(String reqTime){
//        Map<String,Object> param1 = new HashMap<>();
//
//        param1.put("ssq","朝阳区");
//        param1.put("times",reqTime);
//
//        System.out.println("开始请求热点数据");
//        String body = HttpRequest.get(API).form(param1).timeout(90000).execute().body();
//        List<StCrowdDateDto> stCrowdDateDtos = new ArrayList<>();
//        if (StrUtil.isNotEmpty(body)){
//            JSONObject jsonObject = JSONObject.parseObject(body);
//            JSONArray rows = jsonObject.getJSONArray("rows");
//            List<HumanHotDto> humanHotDtos = rows.toJavaList(HumanHotDto.class);
//            for (HumanHotDto humanHotDto : humanHotDtos) {
//                StCrowdDateDto  stCrowdDateDto = new StCrowdDateDto();
//                stCrowdDateDto.setDate(DateUtil.format(new Date(),"yyyy-MM-dd"));
//                stCrowdDateDto.setArea(humanHotDto.getVALUE1());
//                stCrowdDateDto.setNum(Integer.valueOf(humanHotDto.getVALUE3()) );
//                stCrowdDateDto.setWaterUnit(humanHotDto.getVALUE2());
//                stCrowdDateDto.setRvnm(humanHotDto.getVALUE());
//                stCrowdDateDtos.add(stCrowdDateDto);
//            }
//        }
//
//        if (CollUtil.isNotEmpty(stCrowdDateDtos)){
//            for (StCrowdDateDto stCrowdDateDto : stCrowdDateDtos) {
//                String date = stCrowdDateDto.getDate();
//                String area = stCrowdDateDto.getArea();
//                QueryWrapper wrapper = new QueryWrapper();
//                wrapper.eq("date",date);
//                wrapper.eq("area",area);
//                List<StCrowdDateDto> stCrowdDateDtos1 = stCrowdDateDao.selectList(wrapper);
//                if (CollUtil.isNotEmpty(stCrowdDateDtos1)){
//                    stCrowdDateDao.update(stCrowdDateDto,wrapper);
//                }else {
//                    stCrowdDateDao.insert(stCrowdDateDto);
//                }
//            }
//        }
//        System.out.println("请求热点数据完成");
//
//    }
//
//
//}
