package com.essence.job.backjob.video;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.utils.HttpUtils;
import com.essence.common.utils.YsyThird;
import com.essence.dao.VideoInfoBaseDao;
import com.essence.dao.VideoStatusRecordDao;
import com.essence.dao.VideoStatusRecordHistoryDao;
import com.essence.dao.entity.VideoInfoBase;
import com.essence.dao.entity.VideoStatusRecord;
import com.essence.interfaces.api.VideoInfoBaseService;
import com.essence.interfaces.api.VideoStatusRecordService;
import com.essence.interfaces.dot.VideoYsYDto;
import com.essence.interfaces.entity.Criterion;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.common.utils.DeviceSerialModel;
import com.essence.interfaces.model.VideoInfoBaseEsr;
import com.essence.interfaces.model.VideoStatusRecordEsu;
import com.essence.job.validate.CronJobIdentifierProvider;
import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.essence.common.constant.ItemConstant.DEV;
import static com.essence.common.constant.JobConstant.BACK_END;

/**
 * @author zhy
 * @since 2022/10/21 15:27
 */
@Component
@Log4j2
public class CameraStatusTask implements ApplicationRunner {

    @Autowired
    private VideoInfoBaseService videoInfoBaseService;

    @Autowired
    private VideoStatusRecordService videoStatusRecordService;
    @Resource
    private VideoStatusRecordDao videoStatusRecordDao;
    @Resource
    private VideoStatusRecordHistoryDao videoStatusRecordHistoryDao;
    @Autowired
    private VideoInfoBaseDao videoInfoBaseDao;

    @Autowired
    private CronJobIdentifierProvider crdJobIdentifierProvider;

    @Autowired
    private YsyThird ysyThird;

    @Value("${spring.profiles.active}")
    private String env;

    @Value("${hik.execute.path}")
    private String path;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.syncCameraStatus();
    }

    /**
     * 定时更新视频在线状态
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void syncCameraStatus() {
        if (DEV.equals(env)) {
            return;
        }
        if (!BACK_END.equals(crdJobIdentifierProvider.getCronJobIdentifier())) {
            log.debug("只有{}环境才执行order-获取视频在线状态任务,结束.", BACK_END);
            return;
        }
        log.info("获取视频在线状态开始....");
        // 1 获取视频编码列表
        PaginatorParam param = new PaginatorParam();
        param.setPageSize(0);
        param.setCurrentPage(0);
        List<Criterion> conditions = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        list2.add("HUAWEI");
        list2.add("HIV");
        list2.add("ZX");
        list2.add("NW");
        list2.add("YSYC");// 添加萤石云转code后在线状态查询类型
        Criterion criterion = new Criterion("source", list2, Criterion.IN);
        conditions.add(criterion);
        param.setConditions(conditions);
        Paginator<VideoInfoBaseEsr> paginator = videoInfoBaseService.findByPaginator(param);
        List<VideoInfoBaseEsr> videoList = paginator.getItems();
        if (CollectionUtils.isEmpty(videoList)) {
            log.info("视频列表信息为空-结束");
            return;
        }
        // 2 获取视频状态结果
        // 提取编码
        List<String> videoCodeList = videoList.stream().map(VideoInfoBaseEsr::getCode).collect(Collectors.toList());
        // 该接口只支持500个编码
        List<List<String>> partition = Lists.partition(videoCodeList, 100);
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
            try {
                String s = HttpUtils.doPost(path, JSON.toJSONString(hikApiParam));
                List<CameraStatusBean> cameraStatusBeanList = analysisData(s);
                cameraStatusList.addAll(cameraStatusBeanList);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("doPost：");
                return;

            }
        }
        Map<String, CameraStatusBean> cameraStatusMap;
        if (CollectionUtils.isEmpty(cameraStatusList)) {
            cameraStatusMap = new HashMap<>();
        } else {
            cameraStatusMap = cameraStatusList.stream().filter(p -> !StringUtils.isEmpty(p.getIndexCode())).collect(Collectors.toMap(CameraStatusBean::getIndexCode, Function.identity(), (key1, key2) -> key2));
        }
        Date date = new Date();
        for (VideoInfoBaseEsr videoInfoBaseEsr : videoList) {
            CameraStatusBean cameraStatusBean = cameraStatusMap.get(videoInfoBaseEsr.getCode());
            if (null == cameraStatusBean) {
                QueryWrapper<VideoStatusRecord> queryWrapper = new QueryWrapper();
                queryWrapper.eq("video_id", videoInfoBaseEsr.getId());
                List<VideoStatusRecord> videoStatusRecords = videoStatusRecordDao.selectList(queryWrapper);

                VideoStatusRecordEsu videoStatusRecord = new VideoStatusRecordEsu();
                videoStatusRecord.setVideoId(videoInfoBaseEsr.getId());
                videoStatusRecord.setStatus("0");
                videoStatusRecord.setTm(date);
                //增加来源 为了统计 新和利旧的视频在线状态
                videoStatusRecord.setSource(videoInfoBaseEsr.getSource());
                if (CollUtil.isNotEmpty(videoStatusRecords)) {
                    VideoStatusRecord videoStatus = new VideoStatusRecord();
                    BeanUtil.copyProperties(videoStatusRecord, videoStatus);
                    videoStatusRecordDao.update(videoStatus, queryWrapper);
                } else {
                    videoStatusRecordService.insert(videoStatusRecord);
                }
            } else {
                VideoStatusRecordEsu videoStatusRecord = new VideoStatusRecordEsu();
                videoStatusRecord.setVideoId(videoInfoBaseEsr.getId());
                videoStatusRecord.setStatus(null == cameraStatusBean.getOnline() || cameraStatusBean.getOnline().length() > 1 ? "0" : cameraStatusBean.getOnline());
                videoStatusRecord.setTm(date);
                //增加来源 为了统计 新和利旧的视频在线状态
                videoStatusRecord.setSource(videoInfoBaseEsr.getSource());

                QueryWrapper<VideoStatusRecord> queryWrapper = new QueryWrapper();
                queryWrapper.eq("video_id", videoInfoBaseEsr.getId());
                List<VideoStatusRecord> videoStatusRecords = videoStatusRecordDao.selectList(queryWrapper);
                if (CollUtil.isNotEmpty(videoStatusRecords)) {
                    VideoStatusRecord videoStatus = new VideoStatusRecord();
                    BeanUtil.copyProperties(videoStatusRecord, videoStatus);
                    videoStatusRecordDao.update(videoStatus, queryWrapper);
                } else {
                    videoStatusRecordService.insert(videoStatusRecord);
                }

            }
            //保存历史i
//
//            QueryWrapper wrapper = new QueryWrapper();
//            wrapper.eq("video_id",videoInfoBaseEsr.getCode());
//            wrapper.eq("tm", DateUtil.format(date,"yyyy-MM-dd"));
//            VideoStatusRecordHistory videoStatusRecordHistory = videoStatusRecordHistoryDao.selectOne(wrapper);
//            VideoStatusRecordHistory videoStatusRecordHistory1 = new VideoStatusRecordHistory();
//            videoStatusRecordHistory1.setVideoId(videoInfoBaseEsr.getCode());
//            videoStatusRecordHistory1.setStatus(null == cameraStatusBean.getOnline() ||cameraStatusBean.getOnline().length() > 1 ? "0" : cameraStatusBean.getOnline());
//            videoStatusRecordHistory1.setTm(DateUtil.format(date,"yyyy-MM-dd") );
//            if (videoStatusRecordHistory != null){
//                videoStatusRecordHistoryDao.update(videoStatusRecordHistory1,wrapper);
//            }else {
//                videoStatusRecordHistoryDao.insert(videoStatusRecordHistory1);
//            }

        }

        // 3 状态入库
        log.info("获取视频在线状态完成");
    }


    /**
     * 定时更新视频在线状态更新萤石云的在线状态
     */
    @Scheduled(cron = "0 0/20 * * * ?")
    public void syncCameraStatusYsy() {
        if (DEV.equals(env)) {
            return;
        }
        if (!BACK_END.equals(crdJobIdentifierProvider.getCronJobIdentifier())) {
            log.debug("只有{}环境才执行order-获取萤石云视频在线状态任务,结束.", BACK_END);
            return;
        }
        // 3 状态入库
        log.info("获取萤石云视频在线状态开始");
        // 1登陆 获取 登陆信息
        Map loginInfo = getLoginInfo();
        String accessToken = (String) loginInfo.get("accessToken");
        Integer expireTime = (Integer) loginInfo.get("expireTime");

        // 获取 萤石云 摄像头id
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("source", "YSY");

        // 2 获取视频编码列表
        List<VideoInfoBase> videoInfoBases = videoInfoBaseDao.selectList(wrapper);
        if (CollUtil.isNotEmpty(videoInfoBases)) {
            Date date = new Date();
            for (VideoInfoBase videoInfoBase : videoInfoBases) {
                VideoYsYDto videoYsYDto = new VideoYsYDto();
                // 发送 请求 获取视频连接
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("accessToken", accessToken);
//                paramMap.put("expireTime",expireTime);
                paramMap.put("quality", 2);
                paramMap.put("deviceSerial", videoInfoBase.getCode());
                log.info("开始请求获取视频接口");
                String body = HttpRequest.post("https://open.ys7.com/api/lapp/v2/live/address/get")
//                .header("Content-Type", "application/x-www-form-urlencoded")
                        .form(paramMap)
                        .timeout(90000)
                        .execute().body();
                JSONObject jsonObject = JSONObject.parseObject(body);
                String code1 = videoInfoBase.getCode();
                String reaId = videoInfoBase.getReaId();
                if (jsonObject != null) {
                    log.info("请求响应参数为:========>" + jsonObject);
                    String status = jsonObject.getString("code");
                    if (StrUtil.isEmpty(status) || status.equals("10001") || jsonObject.getString("msg").contains("不在线")
                            || jsonObject.getString("msg").contains("该用户不拥有该设备")) {
                        Integer online = 1;
                        // 0 离线
                        videoYsYDto.setStatus(0);
                    }
                    if (status.equals("200")) {
                        JSONObject result = jsonObject.getJSONObject("data");
                        String id = result.getString("id");
                        String url = result.getString("url");
                        Integer online = 1;
                        videoYsYDto.setId(id);
                        videoYsYDto.setUrl(url);
                        videoYsYDto.setStatus(online);
                    }

                }
                VideoStatusRecordEsu videoStatusRecord = new VideoStatusRecordEsu();
                videoStatusRecord.setVideoId(videoInfoBase.getId());
                videoStatusRecord.setStatus(videoYsYDto.getStatus() != null ? videoYsYDto.getStatus().toString() : "0");
                videoStatusRecord.setTm(date);
                //增加来源 为了统计 新和利旧的视频在线状态
                videoStatusRecord.setSource(videoInfoBase.getSource());

                QueryWrapper<VideoStatusRecord> queryWrapper = new QueryWrapper();
                queryWrapper.eq("video_id", videoInfoBase.getId());
                List<VideoStatusRecord> videoStatusRecords = videoStatusRecordDao.selectList(queryWrapper);
                if (CollUtil.isNotEmpty(videoStatusRecords)) {
                    VideoStatusRecord videoStatus = new VideoStatusRecord();
                    BeanUtil.copyProperties(videoStatusRecord, videoStatus);
                    videoStatusRecordDao.update(videoStatus, queryWrapper);
                } else {
                    videoStatusRecordService.insert(videoStatusRecord);
                }
            }


        } else {
            log.info("视频列表信息为空-结束");
        }
        // 3 状态入库
        log.info("获取萤石云视频在线状态完成");

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


    public Map getLoginInfo() {
        Map<String, Object> map = new HashMap<>();
        //登陆获取 appKey
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("appKey", "056898dbe3d34a068f9c10148855a700");
        paramMap.put("appSecret", "26170abb0fd97a6fde9ed6d2a3fbfbed");

        log.info("开始请求登陆接口");
        String body = HttpRequest.post("https://open.ys7.com/api/lapp/token/get")
//                .header("Content-Type", "application/x-www-form-urlencoded")
                .form(paramMap)
                .timeout(90000)
                .execute().body();
        JSONObject jsonObject = JSONObject.parseObject(body);

        if (Objects.nonNull(jsonObject)) {
            log.info("登陆接口请求响应参数为:========>" + jsonObject);
            String code = jsonObject.getString("code");
            if (code.equals("200")) {
                JSONObject result = jsonObject.getJSONObject("data");
                String accessToken = result.getString("accessToken");
                Integer expireTime = result.getInteger("expireTime");
                map.put("accessToken", accessToken);
                map.put("expireTime", expireTime);
            } else {
                log.info("请求失败 切状态为====================>" + jsonObject);
                throw new RuntimeException("请求失败 切状态为====================>" + jsonObject);
            }
        }
        return map;
    }

    /**
     * controller手动调用
     *
     * @param code
     */
    public void syncCameraStatus(String code) {
        if (DEV.equals(env)) {
            return;
        }
        log.info("获取视频在线状态开始....");
        // 1 获取视频编码列表
        PaginatorParam param = new PaginatorParam();
        param.setPageSize(0);
        param.setCurrentPage(0);
        List<Criterion> conditions = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        list2.add("HUAWEI");
        list2.add("HIV");
        list2.add("ZX");
        list2.add("NW");
        list2.add("YSYC");// 添加萤石云转code后在线状态查询类型
        Criterion criterion = new Criterion("source", list2, Criterion.IN);
        conditions.add(criterion);
        param.setConditions(conditions);
        Paginator<VideoInfoBaseEsr> paginator = videoInfoBaseService.findByPaginator(param);
        List<VideoInfoBaseEsr> videoList = paginator.getItems();
        if (CollectionUtils.isEmpty(videoList)) {
            log.info("视频列表信息为空-结束");
            return;
        }
        // 2 获取视频状态结果
        // 提取编码
        List<String> videoCodeList = videoList.stream().map(VideoInfoBaseEsr::getCode).collect(Collectors.toList());
        // 该接口只支持500个编码
        List<List<String>> partition = Lists.partition(videoCodeList, 100);
        List<CameraStatusBean> cameraStatusList = new ArrayList<>();
        for (List<String> list : partition) {
            HikApiParam hikApiParam = new HikApiParam();
            hikApiParam.setApiPath("/api/nms/v1/online/camera/get");
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            if (null != code) {
                sb.append("\"");
                sb.append(code);
                sb.append("\",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("]");
            hikApiParam.setParamBody(String.format("{\"pageNo\": 1,\"pageSize\": 1000,\"indexCodes\": %s}", sb.toString()));
            try {
                String s = HttpUtils.doPost(path, JSON.toJSONString(hikApiParam));
                log.info("数据： {}", s);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("doPost：");
                return;
            }
            if (null != code) {
                break;
            }
        }
        // 3 状态入库
        log.info("获取视频在线状态完成");
    }


    /**
     * 定时更新视频在线状态
     */
    @Scheduled(cron = "0 0/1 * * * ?")
//    @PostConstruct
    public void syncYuCameraStatus() {
        if (DEV.equals(env)) {
            return;
        }
        if (!BACK_END.equals(crdJobIdentifierProvider.getCronJobIdentifier())) {
            log.debug("只有{}环境才执行order-获取视频在线状态任务,结束.", BACK_END);
            return;
        }
        log.info("获取视频在线状态开始....");
        // 1 获取视频编码列表
        PaginatorParam param = new PaginatorParam();
        param.setPageSize(0);
        param.setCurrentPage(0);
        List<Criterion> conditions = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        list2.add("YUSY");
        Criterion criterion = new Criterion("source", list2, Criterion.IN);
        conditions.add(criterion);
        param.setConditions(conditions);
        Paginator<VideoInfoBaseEsr> paginator = videoInfoBaseService.findByPaginator(param);
        List<VideoInfoBaseEsr> videoList = paginator.getItems();
        if (CollectionUtils.isEmpty(videoList)) {
            log.info("视频列表信息为空-结束");
            return;
        }
        // 2 获取视频状态结果
        List<DeviceSerialModel> deviceSerialModels = ysyThird.queryDevices();
        if (null != deviceSerialModels && !deviceSerialModels.isEmpty()) {
            List<CameraStatusBean> cameraStatusList = deviceSerialModels.stream().map(p -> {
                CameraStatusBean cameraStatusBean = new CameraStatusBean();
                cameraStatusBean.setIndexCode(p.getDeviceSerial());
                cameraStatusBean.setOnline(p.getStatus());
                return cameraStatusBean;
            }).collect(Collectors.toList());

            Map<String, CameraStatusBean> cameraStatusMap;
            if (CollectionUtils.isEmpty(cameraStatusList)) {
                cameraStatusMap = new HashMap<>();
            } else {
                cameraStatusMap = cameraStatusList.stream().filter(p -> !StringUtils.isEmpty(p.getIndexCode())).collect(Collectors.toMap(CameraStatusBean::getIndexCode, Function.identity(), (key1, key2) -> key2));
            }
            Date date = new Date();
            for (VideoInfoBaseEsr videoInfoBaseEsr : videoList) {
                CameraStatusBean cameraStatusBean = cameraStatusMap.get(videoInfoBaseEsr.getCode());
                if (null == cameraStatusBean) {
                    QueryWrapper<VideoStatusRecord> queryWrapper = new QueryWrapper();
                    queryWrapper.eq("video_id", videoInfoBaseEsr.getId());
                    List<VideoStatusRecord> videoStatusRecords = videoStatusRecordDao.selectList(queryWrapper);

                    VideoStatusRecordEsu videoStatusRecord = new VideoStatusRecordEsu();
                    videoStatusRecord.setVideoId(videoInfoBaseEsr.getId());
                    videoStatusRecord.setStatus("0");
                    videoStatusRecord.setTm(date);
                    //增加来源 为了统计 新和利旧的视频在线状态
                    videoStatusRecord.setSource(videoInfoBaseEsr.getSource());
                    if (CollUtil.isNotEmpty(videoStatusRecords)) {
                        VideoStatusRecord videoStatus = new VideoStatusRecord();
                        BeanUtil.copyProperties(videoStatusRecord, videoStatus);
                        videoStatusRecordDao.update(videoStatus, queryWrapper);
                    } else {
                        videoStatusRecordService.insert(videoStatusRecord);
                    }
                } else {
                    VideoStatusRecordEsu videoStatusRecord = new VideoStatusRecordEsu();
                    videoStatusRecord.setVideoId(videoInfoBaseEsr.getId());
                    videoStatusRecord.setStatus(null == cameraStatusBean.getOnline() || cameraStatusBean.getOnline().length() > 1 ? "0" : cameraStatusBean.getOnline());
                    videoStatusRecord.setTm(date);
                    //增加来源 为了统计 新和利旧的视频在线状态
                    videoStatusRecord.setSource(videoInfoBaseEsr.getSource());

                    QueryWrapper<VideoStatusRecord> queryWrapper = new QueryWrapper();
                    queryWrapper.eq("video_id", videoInfoBaseEsr.getId());
                    List<VideoStatusRecord> videoStatusRecords = videoStatusRecordDao.selectList(queryWrapper);
                    if (CollUtil.isNotEmpty(videoStatusRecords)) {
                        VideoStatusRecord videoStatus = new VideoStatusRecord();
                        BeanUtil.copyProperties(videoStatusRecord, videoStatus);
                        videoStatusRecordDao.update(videoStatus, queryWrapper);
                    } else {
                        videoStatusRecordService.insert(videoStatusRecord);
                    }

                }
            }
        }
        // 3 状态入库
        log.info("获取视频在线状态完成");
    }
}
