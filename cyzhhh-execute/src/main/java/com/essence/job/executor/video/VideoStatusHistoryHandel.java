package com.essence.job.executor.video;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.utils.HttpUtils;
import com.essence.dao.VideoInfoBaseDao;
import com.essence.dao.VideoStatusRecordHistoryDao;
import com.essence.dao.entity.VideoInfoBase;
import com.essence.dto.CameraStatusBean;
import com.essence.dto.HikApiParam;
import com.essence.entity.VideoStatusRecordHistory;
import com.google.common.collect.Lists;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 视屏历史状态 定时任务
 */
@Component
public class VideoStatusHistoryHandel {
    @Resource
    private VideoStatusRecordHistoryDao videoStatusRecordHistoryDao;

    @Autowired
    private VideoInfoBaseDao videoInfoBaseDao;

    @Value("${hik.execute.path}")
    private String path;

    @XxlJob("VideoHistoryHandel")
    public void VideoHistoryHandel() throws Exception {
        String params = XxlJobHelper.getJobParam();
        if (StrUtil.isNotEmpty(params)) {
            String[] split = params.split(",");
            String start = split[0];
            String end = split[1];
            DateTime startDate = DateUtil.parse(start, "yyyy-MM-dd");
            DateTime endDate = DateUtil.parse(end, "yyyy-MM-dd");
            List<DateTime> dateTimes = DateUtil.rangeToList(startDate, endDate, DateField.DAY_OF_YEAR);
            for (DateTime dateTime : dateTimes) {
                DateTime offPreDay = DateUtil.offsetDay(dateTime, -1);
                String format = DateUtil.format(offPreDay, "yyyy-MM-dd");
                doExecute(format, format);
            }
        } else {
            String format = DateUtil.format(DateUtil.offsetDay(new Date(), -1), "yyyy-MM-dd");
            doExecute(format, format);
            doYinShiHistory();
        }


    }


    public void doExecute(String start ,String end) {
        System.out.println("获取视频在线状态开始....");
        // 1 获取视频编码列表


        List<String> list2 = new ArrayList<>();
        list2.add("HUAWEI");
        list2.add("HIV");
        list2.add("ZX");
        list2.add("NW");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.in("source",list2);
        List<VideoInfoBase> videoList = videoInfoBaseDao.selectList(wrapper);

        if (CollUtil.isEmpty(videoList)) {
            System.out.println("视频列表信息为空-结束");
            return;
        }
        // 2 获取视频状态结果
        // 提取编码
        List<String> videoCodeList = videoList.stream().map(VideoInfoBase::getCode).collect(Collectors.toList());
        // 该接口只支持500个编码
        List<List<String>> partition = Lists.partition(videoCodeList, 500);
        List<CameraStatusBean> cameraStatusList = new ArrayList<>();
        for (List<String> list : partition) {
            HikApiParam hikApiParam = new HikApiParam();
            hikApiParam.setApiPath("/api/nms/v1/online/camera/get");
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (String s : list) {

                sb.append("\"");
                sb.append(s);
                sb.append("\",");

            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("]");
            hikApiParam.setParamBody(String.format("{\"pageNo\": 1,\"pageSize\": 1000,\"indexCodes\": %s}", sb.toString()));
            System.out.println("path: " + path);
            System.out.println("hikApiParam: " + JSON.toJSONString(hikApiParam));
            try {
                String s = HttpUtils.doPost(path, JSON.toJSONString(hikApiParam));
                System.out.println("数据： {}"+ s);
                List<CameraStatusBean> cameraStatusBeanList = analysisData(s);

                cameraStatusList.addAll(cameraStatusBeanList);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        Map<String, CameraStatusBean> cameraStatusMap;
        if (CollUtil.isEmpty(cameraStatusList)) {
            cameraStatusMap = new HashMap<>();
        }else{
            cameraStatusMap = cameraStatusList.stream().filter(p -> !StrUtil.isEmpty(p.getIndexCode())).collect(Collectors.toMap(CameraStatusBean::getIndexCode, Function.identity(), (key1, key2) -> key2));
        }
        Date date = new Date();
        for (VideoInfoBase videoInfoBaseEsr : videoList) {
            CameraStatusBean cameraStatusBean = cameraStatusMap.get(videoInfoBaseEsr.getCode());
            if (null == cameraStatusBean){
                continue;
            }


            //保存历史
            VideoStatusRecordHistory videoStatusRecordHistory1 = new VideoStatusRecordHistory();
            videoStatusRecordHistory1.setVideoId(videoInfoBaseEsr.getId());
            videoStatusRecordHistory1.setStatus(null == cameraStatusBean.getOnline() ||cameraStatusBean.getOnline().length() > 1 ? "0" : cameraStatusBean.getOnline());
            videoStatusRecordHistory1.setTm(DateUtil.format(date,"yyyy-MM-dd HH:mm:ss") );
            videoStatusRecordHistory1.setSource(videoInfoBaseEsr.getSource()); //增加来源
            videoStatusRecordHistoryDao.insert(videoStatusRecordHistory1);


        }

        // 3 状态入库
        System.out.println("获取历史视频在线状态完成");
    }


    // 解析结果
    private List<CameraStatusBean> analysisData(String str) {

        JSONObject jsonObject = JSONObject.parseObject(str);
        Object data1 = jsonObject.get("result");
        JSONObject jsonObject0 = JSONObject.parseObject(data1.toString());
        Object data = jsonObject0.get("data");
        JSONObject jsonObject1 = JSONObject.parseObject(JSON.toJSONString(data));
        return JSONArray.parseArray(JSON.toJSONString(jsonObject1.get("list")), CameraStatusBean.class);
    }


    public void doYinShiHistory() {
        // 登陆 获取 登陆信息
        Map loginInfo = getLoginInfo();
        String accessToken =(String) loginInfo.get("accessToken");
        getActVideo(accessToken);


    }

    public Map getLoginInfo(){
        Map<String,Object> map = new HashMap<>();
        //登陆获取 appKey
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("appKey","056898dbe3d34a068f9c10148855a700");
        paramMap.put("appSecret","26170abb0fd97a6fde9ed6d2a3fbfbed");

        System.out.println("开始请求登陆接口");
        String body = HttpRequest.post("https://open.ys7.com/api/lapp/token/get")
//                .header("Content-Type", "application/x-www-form-urlencoded")
                .form(paramMap)
                .timeout(90000)
                .execute().body();
        JSONObject jsonObject = JSONObject.parseObject(body);

        if (Objects.nonNull(jsonObject)){
            System.out.println("请求响应参数为:========>"+jsonObject);
            String code = jsonObject.getString("code");
            if (code.equals("200")){
                JSONObject result = jsonObject.getJSONObject("data");
                String accessToken = result.getString("accessToken");
                Integer expireTime = result.getInteger("expireTime");
                map.put("accessToken",accessToken);
                map.put("expireTime",expireTime);
            }else {
                System.out.println("请求失败 切状态为====================>"+jsonObject);
                throw new RuntimeException("请求失败 切状态为====================>"+jsonObject);
            }
        }
        return map;
    }

    /**
     * 获取 数据库中的YSY 摄像头数据
     * @param accessToken
     * @return
     */
    public  void getActVideo( String accessToken ){

        // 获取 萤石云 摄像头id
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("source","YSY");
        List<VideoInfoBase> videoInfoBases = videoInfoBaseDao.selectList(wrapper);
        if (CollUtil.isNotEmpty(videoInfoBases)){
            for (VideoInfoBase videoInfoBase : videoInfoBases) {
                // 发送 请求 获取视频连接
                Map<String,Object> paramMap = new HashMap<>();
                paramMap.put("accessToken",accessToken);
                paramMap.put("quality",2);
                paramMap.put("deviceSerial",videoInfoBase.getCode());
                System.out.println("开始请求获取视频接口");
                String body = HttpRequest.post("https://open.ys7.com/api/lapp/v2/live/address/get")
                        .form(paramMap)
                        .timeout(90000)
                        .execute().body();
                JSONObject jsonObject = JSONObject.parseObject(body);
                String code1 = videoInfoBase.getCode();
                String reaId = videoInfoBase.getReaId();
                if (jsonObject != null){
                    System.out.println("请求响应参数为:========>"+jsonObject);
                    String status = jsonObject.getString("code");
                    if (StrUtil.isEmpty(status) || status.equals("10001") || jsonObject.getString("msg").contains("不在线")){
                        VideoStatusRecordHistory videoStatusRecordHistory1 = new VideoStatusRecordHistory();
                        videoStatusRecordHistory1.setVideoId(videoInfoBase.getId());
                        videoStatusRecordHistory1.setStatus("0");
                        videoStatusRecordHistory1.setSource("YSY");
                        videoStatusRecordHistory1.setTm(DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
                        videoStatusRecordHistoryDao.insert(videoStatusRecordHistory1);
                        continue;
                    }
                    else if (status.equals("200")){
                        JSONObject result = jsonObject.getJSONObject("data");
                        String id = result.getString("id");
                        String url = result.getString("url");
                        Integer online = 1;
                        VideoStatusRecordHistory videoStatusRecordHistory1 = new VideoStatusRecordHistory();
                        videoStatusRecordHistory1.setVideoId(videoInfoBase.getId());
                        videoStatusRecordHistory1.setStatus("1");
                        videoStatusRecordHistory1.setSource("YSY");
                        videoStatusRecordHistory1.setTm(DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
                        videoStatusRecordHistoryDao.insert(videoStatusRecordHistory1);
                        continue;
                    }else {
                        VideoStatusRecordHistory videoStatusRecordHistory1 = new VideoStatusRecordHistory();
                        videoStatusRecordHistory1.setVideoId(videoInfoBase.getId());
                        videoStatusRecordHistory1.setStatus("0");
                        videoStatusRecordHistory1.setSource("YSY");
                        videoStatusRecordHistory1.setTm(DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
                        videoStatusRecordHistoryDao.insert(videoStatusRecordHistory1);
                        System.out.println("该摄像头未能捕获任何消息"+jsonObject);
                        continue;
                    }
                }
            }
        }
    }


}