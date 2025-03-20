package com.essence.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.dto.CameraPortalDTO;
import com.essence.common.utils.GisUtils;
import com.essence.common.utils.PageUtil;
import com.essence.common.utils.YsyThird;
import com.essence.dao.*;
import com.essence.dao.entity.*;
import com.essence.interfaces.api.VideoInfoBaseService;
import com.essence.interfaces.api.VideoStatusRecordService;
import com.essence.interfaces.dot.VideoStatusRecordHistoryDto;
import com.essence.interfaces.dot.VideoYsYDto;
import com.essence.interfaces.entity.*;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.VideoInfoBaseEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterVideoInfoBaseEtoT;
import com.essence.service.converter.ConverterVideoInfoBaseTtoR;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * @author zhy
 * @since 2022/5/13 15:05
 */
@Service
public class VideoInfoBaseServiceImpl extends BaseApiImpl<VideoInfoBaseEsu, VideoInfoBaseEsp, VideoInfoBaseEsr, VideoInfoBase> implements VideoInfoBaseService {

    @Autowired
    private ReaBaseDao reaBaseDao;
    @Autowired
    private VideoInfoBaseDao videoInfoBaseDao;
    @Autowired
    private VideoStatusRecordService videoStatusRecordService;
    @Autowired
    private ConverterVideoInfoBaseEtoT converterVideoInfoBaseEtoT;
    @Autowired
    private ConverterVideoInfoBaseTtoR converterVideoInfoBaseTtoR;
    @Autowired
    private VideoInfoBaseService videoInfoBaseService;
    @Autowired
    private VideoStatusRecordDao videoStatusRecordDao;
    @Resource
    private VideoStatusRecordHistoryDao videoStatusRecordHistoryDao;
    @Resource
    private EventBaseDao eventBaseDao;
    @Resource
    private VideoWarningInfoDao videoWarningInfoDao;
    @Resource
    private FileBaseDao fileBaseDao;
    @Resource
    private YsyThird ysyThird;

    private static final String SPACE_SPLIT = " ";

    public VideoInfoBaseServiceImpl(VideoInfoBaseDao videoInfoBaseDao, ConverterVideoInfoBaseEtoT converterVideoInfoBaseEtoT, ConverterVideoInfoBaseTtoR converterVideoInfoBaseTtoR) {
        super(videoInfoBaseDao, converterVideoInfoBaseEtoT, converterVideoInfoBaseTtoR);
    }


    @Override
    public List<PieChartDto<VideoInfoBaseDto>> queryVideoByRiverType(Integer type) {
        // 视频最新在新情况
        List<VideoStatusRecordEsr> videoStatusRecordEsrs = videoStatusRecordService.latestStauts();
        Map<Integer, VideoStatusRecordEsr> videoStatusMap;
        if (CollectionUtils.isEmpty(videoStatusRecordEsrs)) {
            videoStatusMap = new HashMap<>();
        } else {
            videoStatusMap = videoStatusRecordEsrs.stream().collect(Collectors.toMap(VideoStatusRecordEsr::getVideoId, Function.identity(), (key1, key2) -> key1));
        }
        List<PieChartDto<VideoInfoBaseDto>> pieChartDtos = new ArrayList<>();
        List<VideoInfoBaseEX> videoInfoBases;
        if (type == 0) {
            videoInfoBases = videoInfoBaseDao.queryAllVideoByRiverType();
        } else {
            videoInfoBases = videoInfoBaseDao.queryVideoByRiverType(type);
        }
        Map<String, List<VideoInfoBaseDto>> collect = videoInfoBases.stream()
                .map(item -> {
                    VideoInfoBaseDto infoBaseDto = new VideoInfoBaseDto();
                    BeanUtil.copyProperties(item, infoBaseDto);
                    VideoStatusRecordEsr videoStatusRecordEsr = videoStatusMap.get(item.getId());
                    infoBaseDto.setStatus(null == videoStatusRecordEsr || StringUtils.isEmpty(videoStatusRecordEsr.getStatus()) ? "0" : videoStatusRecordEsr.getStatus());
                    return infoBaseDto;
                })
                .collect(Collectors.groupingBy(VideoInfoBaseDto::getRiver_name));
        List<VideoInfoBaseDto> videoInfoBaseDtos;
        for (String riverName : collect.keySet()) {
            videoInfoBaseDtos = collect.get(riverName);
            PieChartDto pieChartDto = new PieChartDto();
            pieChartDto.setType(riverName);

            boolean isNoVideoRiver = videoInfoBaseDtos.size() == 1 && StringUtils.isEmpty(videoInfoBaseDtos.get(0).getCode());
            if (isNoVideoRiver) {
                pieChartDto.setNumber(0);
                pieChartDto.setOnlineNum(0);
                pieChartDto.setData(new ArrayList());
            } else {
                pieChartDto.setNumber(videoInfoBaseDtos.size());
                pieChartDto.setOnlineNum(((Long) videoInfoBaseDtos.stream().filter(p -> "1".equals(p.getStatus())).count()).intValue());
                pieChartDto.setData(videoInfoBaseDtos);
            }
            pieChartDtos.add(pieChartDto);
        }

        return pieChartDtos;
    }

    /**
     * 功能类型｜ 1-功能   2-安防  3-井房
     *
     * @param type 功能类型｜ 1-功能   2-安防  3-井房 4-观鸟区   5-专线  6-内网
     * @param name
     * @return
     */
    @Override
    public List<PieChartDto<VideoInfoBaseDto>> queryVideoByFunction(Integer type, String name, String unitId) {
        if ("".equals(unitId)) {
            unitId = null;
        }
        // 视频最新在新情况
        List<VideoStatusRecordEsr> videoStatusRecordEsrs = videoStatusRecordService.latestStauts();
        Map<Integer, VideoStatusRecordEsr> videoStatusMap;
        if (CollectionUtils.isEmpty(videoStatusRecordEsrs)) {
            videoStatusMap = new HashMap<>();
        } else {
            videoStatusMap = videoStatusRecordEsrs.stream().collect(Collectors.toMap(VideoStatusRecordEsr::getVideoId, Function.identity(), (key1, key2) -> key1));
        }
        List<PieChartDto<VideoInfoBaseDto>> pieChartDtos = new ArrayList<>();
        List<PieChartDtoRes> pieChartDtosSort = new ArrayList<>();
        List<VideoInfoBaseEX> videoInfoBases;

        if (type == 6) { //内网的视频
            videoInfoBases = videoInfoBaseDao.queryVideoByFunction6(type, name, unitId);
        } else if (type == 5) { //专线的视频
            videoInfoBases = videoInfoBaseDao.queryVideoByFunction5(type, name, unitId);
        } else if (type == 4) { //观鸟区的视频
            videoInfoBases = videoInfoBaseDao.queryVideoByFunction4(type, name);
        } else if (type == 3) { //井房的
            videoInfoBases = videoInfoBaseDao.queryVideoByFunction2(type, name);
        } else { //1 -功能的  2 -安防的
            videoInfoBases = videoInfoBaseDao.queryVideoByFunction(type, name, unitId);
        }
        Map<String, List<VideoInfoBaseDto>> collect = videoInfoBases.stream()
//                .filter(p -> !p.getRiver_name().equals("通惠灌渠"))
                .map(item -> {
                    VideoInfoBaseDto infoBaseDto = new VideoInfoBaseDto();
                    BeanUtil.copyProperties(item, infoBaseDto);
                    VideoStatusRecordEsr videoStatusRecordEsr = videoStatusMap.get(item.getId());
                    infoBaseDto.setStatus(null == videoStatusRecordEsr || StringUtils.isEmpty(videoStatusRecordEsr.getStatus()) ? "0" : videoStatusRecordEsr.getStatus());
                    return infoBaseDto;
                })
                .collect(Collectors.groupingBy(VideoInfoBaseDto::getRiver_name));
        List<VideoInfoBaseDto> videoInfoBaseDtos;
        for (String riverName : collect.keySet()) {
            videoInfoBaseDtos = collect.get(riverName);
            PieChartDto pieChartDto = new PieChartDto();
            pieChartDto.setType(riverName);

            PieChartDtoRes pieChartDtoSort = new PieChartDtoRes();
            pieChartDtoSort.setType(riverName);
            //判断河流排序
            Integer sort = this.getSort(riverName);
            pieChartDtoSort.setSort(sort);
            //判断河流排序

            boolean isNoVideoRiver = videoInfoBaseDtos.size() == 1 && StringUtils.isEmpty(videoInfoBaseDtos.get(0).getCode());
            if (isNoVideoRiver) {
                pieChartDto.setNumber(0);
                pieChartDto.setOnlineNum(0);
                pieChartDto.setData(new ArrayList());
                pieChartDtoSort.setNumber(0);
                pieChartDtoSort.setOnlineNum(0);
            } else {
                pieChartDto.setNumber(videoInfoBaseDtos.size());
                pieChartDto.setOnlineNum(((Long) videoInfoBaseDtos.stream().filter(p -> "1".equals(p.getStatus())).count()).intValue());
                pieChartDto.setData(videoInfoBaseDtos);
                pieChartDtoSort.setNumber(videoInfoBaseDtos.size());
                pieChartDtoSort.setOnlineNum(((Long) videoInfoBaseDtos.stream().filter(p -> "1".equals(p.getStatus())).count()).intValue());
            }
            pieChartDtos.add(pieChartDto);
            pieChartDtosSort.add(pieChartDtoSort);
        }

        //对功能的摄像头进行排序
        if (type == 1) {
            pieChartDtosSort = pieChartDtosSort.stream().sorted(Comparator.comparing(PieChartDtoRes::getSort)).collect(Collectors.toList());
            List<PieChartDto<VideoInfoBaseDto>> pieChartDtosType1 = this.sortByType(pieChartDtosSort, pieChartDtos);
            return this.reOrder(pieChartDtosType1);
        }

        return this.reOrder(pieChartDtos);
    }

    /**
     * 2024.05.24 需求：要求变更排序按照在线状态在线的放在前面不在线的放在后面
     *
     * @param pieChartDtos
     * @return
     */
    private List<PieChartDto<VideoInfoBaseDto>> reOrder(List<PieChartDto<VideoInfoBaseDto>> pieChartDtos) {
        for (PieChartDto<VideoInfoBaseDto> pieChartDto : pieChartDtos) {
            List<VideoInfoBaseDto> videoInfoBaseDtos = pieChartDto.getData();
            if (null != videoInfoBaseDtos && !videoInfoBaseDtos.isEmpty()) {
                this.reOrderVideoInfoList(videoInfoBaseDtos);
            }
        }
        return pieChartDtos;
    }

    private void reOrderVideoInfoList(List<VideoInfoBaseDto> videoInfoBaseDtos) {
        videoInfoBaseDtos.sort((a, b) -> Integer.parseInt(b.getStatus()) - Integer.parseInt(a.getStatus()));
        {// 2024.05.24:酒仙桥处理厂退水口 (河道) 要排到最后
            for (int i = 0; i < videoInfoBaseDtos.size() - 1; i++) {
                if (videoInfoBaseDtos.get(i).getCode().equals("17485548171315296695")) {
                    VideoInfoBaseDto temp = videoInfoBaseDtos.remove(i);
                    videoInfoBaseDtos.add(temp);
                    break;
                }
            }
        }
    }


    /**
     * 对功能的摄像头进行排序
     *
     * @param pieChartDtosSort
     * @param pieChartDtos
     * @return
     */
    private List<PieChartDto<VideoInfoBaseDto>> sortByType(List<PieChartDtoRes> pieChartDtosSort, List<PieChartDto<VideoInfoBaseDto>> pieChartDtos) {
        Map<String, List<PieChartDto<VideoInfoBaseDto>>> groupBy = pieChartDtos.stream().collect(Collectors.groupingBy(PieChartDto::getType));
        List<PieChartDto<VideoInfoBaseDto>> pieChartDtosType1 = new ArrayList<>();
        for (int i = 0; i < pieChartDtosSort.size(); i++) {
            String type = pieChartDtosSort.get(i).getType();
            List<PieChartDto<VideoInfoBaseDto>> pieChartDtos1 = groupBy.get(type);
            pieChartDtosType1.add(pieChartDtos1.get(0));
        }

        return pieChartDtosType1;
    }

    /**
     * 判断河流排序
     *
     * @param riverName
     * @return
     */
    private Integer getSort(String riverName) {
        Integer sort = 32;

        if ("亮马河".equals(riverName)) {
            sort = 1;
        } else if ("萧太后河".equals(riverName)) {
            sort = 2;
        } else if ("北小河".equals(riverName)) {
            sort = 3;
        } else if ("坝河".equals(riverName)) {
            sort = 4;
        } else if ("清洋河".equals(riverName)) {
            sort = 5;
        } else if ("青年路沟".equals(riverName)) {
            sort = 6;
        } else if ("半壁店沟".equals(riverName)) {
            sort = 7;
        } else if ("小场沟".equals(riverName)) {
            sort = 8;
        } else if ("常营中心沟".equals(riverName)) {
            sort = 9;
        } else if ("观音堂沟".equals(riverName)) {
            sort = 10;
        } else if ("南大沟".equals(riverName)) {
            sort = 11;
        } else if ("大稿沟".equals(riverName)) {
            sort = 12;
        } else if ("大羊坊沟".equals(riverName)) {
            sort = 13;
        } else if ("大柳树沟".equals(riverName)) {
            sort = 14;
        } else if ("横街子沟".equals(riverName)) {
            sort = 15;
        } else if ("望京沟".equals(riverName)) {
            sort = 16;
        } else if ("来广营中心沟".equals(riverName)) {
            sort = 17;
        } else if ("西干沟".equals(riverName)) {
            sort = 18;
        } else if ("曹各庄沟".equals(riverName)) {
            sort = 19;
        } else if ("草场地沟".equals(riverName)) {
            sort = 20;
        } else if ("驹子房沟".equals(riverName)) {
            sort = 21;
        } else if ("大华窑沟".equals(riverName)) {
            sort = 22;
        } else if ("朝阳干渠".equals(riverName)) {
            sort = 23;
        } else if ("小寺干渠".equals(riverName)) {
            sort = 24;
        } else if ("东南郊清水干渠".equals(riverName)) {
            sort = 25;
        } else if ("通惠排干".equals(riverName)) {
            sort = 26;
        } else if ("通惠灌渠".equals(riverName)) {
            sort = 127;
        } else if ("东南郊污水干渠".equals(riverName)) {
            sort = 28;
        } else if ("沈家坟干渠".equals(riverName)) {
            sort = 29;
        } else if ("东直门干渠".equals(riverName)) {
            sort = 30;
        } else if ("两湖连通".equals(riverName)) {
            sort = 31;
        }


        return sort;
    }

    /**
     * 功能类型｜ 1-功能   2-安防  3-井房
     *
     * @param type 功能类型｜ 1-功能   2-安防  3-井房
     * @param name
     * @return
     */
    @Override
    public List<PieChartDto<VideoInfoBaseDto>> findByFunctionAlarm(Integer type, String name, String unitId, String riverId, Double lgtd, Double lttd) {
        if ("".equals(unitId)) {
            unitId = null;
        }
        // 视频最新在新情况
        List<VideoStatusRecordEsr> videoStatusRecordEsrs = videoStatusRecordService.latestStauts();
        Map<Integer, VideoStatusRecordEsr> videoStatusMap;
        if (CollectionUtils.isEmpty(videoStatusRecordEsrs)) {
            videoStatusMap = new HashMap<>();
        } else {
            videoStatusMap = videoStatusRecordEsrs.stream().collect(Collectors.toMap(VideoStatusRecordEsr::getVideoId, Function.identity(), (key1, key2) -> key1));
        }
        List<PieChartDto<VideoInfoBaseDto>> pieChartDtos = new ArrayList<>();
        List<VideoInfoBaseEX> videoInfoBases;
        List<VideoInfoBaseEX> videoInfoBasesRes = new ArrayList<>();

        //1 -功能的
        videoInfoBases = videoInfoBaseDao.findByFunctionAlarm(type, name, unitId, riverId);
        //计算在2公里范围内的
        for (int i = 0; i < videoInfoBases.size(); i++) {
            VideoInfoBaseEX videoInfoBaseEX = videoInfoBases.get(i);
            Double lgtd1 = videoInfoBaseEX.getLgtd();
            Double lttd1 = videoInfoBaseEX.getLttd();
            Boolean save = save(lgtd, lttd, lgtd1, lttd1);
            if (save) { //在2公里范围之内
                videoInfoBasesRes.add(videoInfoBaseEX);
            }
        }


        //计算在2公里范围内的

        Map<String, List<VideoInfoBaseDto>> collect = videoInfoBasesRes.stream()
                .map(item -> {
                    VideoInfoBaseDto infoBaseDto = new VideoInfoBaseDto();
                    BeanUtil.copyProperties(item, infoBaseDto);
                    VideoStatusRecordEsr videoStatusRecordEsr = videoStatusMap.get(item.getId());
                    infoBaseDto.setStatus(null == videoStatusRecordEsr || StringUtils.isEmpty(videoStatusRecordEsr.getStatus()) ? "0" : videoStatusRecordEsr.getStatus());
                    return infoBaseDto;
                })
                .collect(Collectors.groupingBy(VideoInfoBaseDto::getRiver_name));
        List<VideoInfoBaseDto> videoInfoBaseDtos;
        for (String riverName : collect.keySet()) {
            videoInfoBaseDtos = collect.get(riverName);
            PieChartDto pieChartDto = new PieChartDto();
            pieChartDto.setType(riverName);

            boolean isNoVideoRiver = videoInfoBaseDtos.size() == 1 && StringUtils.isEmpty(videoInfoBaseDtos.get(0).getCode());
            if (isNoVideoRiver) {
                pieChartDto.setNumber(0);
                pieChartDto.setOnlineNum(0);
                pieChartDto.setData(new ArrayList());
            } else {
                pieChartDto.setNumber(videoInfoBaseDtos.size());
                pieChartDto.setOnlineNum(((Long) videoInfoBaseDtos.stream().filter(p -> "1".equals(p.getStatus())).count()).intValue());
                pieChartDto.setData(videoInfoBaseDtos);
            }
            pieChartDtos.add(pieChartDto);
        }
        return pieChartDtos;
    }

    /**
     * 根据条件分页查询包含在线状态
     *
     * @param param
     * @return
     */
    @Override
    public Paginator<VideoInfoBaseEsr> searchAll(PaginatorParam param) {
        //先去查询河道数据 id <= 31 则为河道  使用河道id 关联查询设备表中的 rvnm
        QueryWrapper<ReaBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.le("id", 31);
        List<ReaBase> reaBases = reaBaseDao.selectList(queryWrapper);
        Map<String, ReaBase> riverMap = new HashedMap<>();
        if (CollUtil.isNotEmpty(reaBases)) {
            riverMap = reaBases.parallelStream().collect(Collectors.toMap(ReaBase::getId, Function.identity()));
        }
        Paginator<VideoInfoBaseEsr> p = videoInfoBaseService.findByPaginator(param);
        List<VideoInfoBaseEsr> items = p.getItems();
        // 视频最新在新情况
        List<VideoStatusRecordEsr> videoStatusRecordEsrs = videoStatusRecordService.latestStauts();
        Map<Integer, VideoStatusRecordEsr> videoStatusMap;
        if (CollectionUtils.isEmpty(videoStatusRecordEsrs)) {
            videoStatusMap = new HashMap<>();
        } else {
            videoStatusMap = videoStatusRecordEsrs.stream().collect(Collectors.toMap(VideoStatusRecordEsr::getVideoId, Function.identity(), (key1, key2) -> key1));
        }
        //处理在线状态
        Map<String, ReaBase> finalRiverMap = riverMap;
        items.stream().forEach(item -> {
            VideoStatusRecordEsr videoStatusRecordEsr = videoStatusMap.get(item.getId());
            item.setStatus(null == videoStatusRecordEsr || StringUtils.isEmpty(videoStatusRecordEsr.getStatus()) ? "0" : videoStatusRecordEsr.getStatus());
            ReaBase reaBase = finalRiverMap.get(item.getReaId());
            if (item.getUnitId() == null) {
                item.setUnitId(reaBase == null ? null : reaBase.getUnitId());
            }
            item.setUnitName(reaBase == null ? null : reaBase.getUnitName());
        });
        return p;
    }

    @Override
    public PageUtil<VideoYsYDto> getYinShi(PaginatorParam param, String unitId) {
        List<VideoYsYDto> list = new ArrayList<>();
        // 登陆 获取 登陆信息
        Map loginInfo = getLoginInfo();
        String accessToken = (String) loginInfo.get("accessToken");
        Integer expireTime = (Integer) loginInfo.get("expireTime");
        list = getActVideo(accessToken, unitId);
//        VideoYsYDto videoYsYDto = new VideoYsYDto();
//
//        // 发送 请求 获取视频连接
//        String id = "J86039145";
//        Map<String,Object> paramMap = new HashMap<>();
//        paramMap.put("accessToken",accessToken);
////        paramMap.put("expireTime",expireTime);
//        paramMap.put("quality",2);
//        paramMap.put("deviceSerial",id);
//        System.out.println("开始请求登陆接口");
//        String body = HttpRequest.post("https://open.ys7.com/api/lapp/v2/live/address/get")
////                .header("Content-Type", "application/x-www-form-urlencoded")
//                .form(paramMap)
//                .timeout(90000)
//                .execute().body();
//        JSONObject jsonObject = JSONObject.parseObject(body);
//
//        if (jsonObject != null){
//            System.out.println("请求响应参数为:========>"+jsonObject);
//            String status = jsonObject.getString("code");
//            if (StrUtil.isEmpty(status) || status.equals("10001") || jsonObject.getString("msg").contains("不在线")){
//                Integer online = 2;
//                videoYsYDto.setCode(id);
//                videoYsYDto.setOnlineStatus(2);
//            }
//            if (status.equals("200")){
//                JSONObject result = jsonObject.getJSONObject("data");
//
//                String url = result.getString("url");
//                Integer online = 1;
//                videoYsYDto.setId(id);
//                videoYsYDto.setUrl(url);
//                videoYsYDto.setOnlineStatus(online);
//            }
//            list.add(videoYsYDto);
//        }
        PageUtil pageUtil = new PageUtil(list, param.getCurrentPage(), param.getPageSize(), null, null);
        return pageUtil;
    }


    @Override
    public PageUtil<VideoYsYDto> getYinShiNew(PaginatorParam param, String unitId, String name) {
        List<VideoYsYDto> list = new ArrayList<>();
        // 登陆 获取 登陆信息
        Map loginInfo = getLoginInfo();
        String accessToken = (String) loginInfo.get("accessToken");
        Integer expireTime = (Integer) loginInfo.get("expireTime");
        list = getActVideoNew(accessToken, unitId, param, name);
        PageUtil pageUtil = new PageUtil(list, param.getCurrentPage(), param.getPageSize(), null, null);
        return pageUtil;
    }

    @Override
    public List<VideoStatusRecordHistoryDto> getVideoStatusHistory(String start, String end) {
        List<VideoStatusRecordHistoryDto> res = new ArrayList<>();
        List<VideoInfoBase> videoInfoBases = videoInfoBaseDao.selectList(new QueryWrapper<>());
        Map<Integer, VideoInfoBase> videoMap = new HashMap<>();
        if (CollUtil.isNotEmpty(videoInfoBases)) {
            videoMap = videoInfoBases.parallelStream().filter(videoInfoBase -> {
                return videoInfoBase.getId() != null;
            }).collect(Collectors.toMap(VideoInfoBase::getId, Function.identity(), (o1, o2) -> o1));
        }
        QueryWrapper wrapper = new QueryWrapper();
        if (StrUtil.isEmpty(start) || StrUtil.isEmpty(end)) {
            start = DateUtil.format(DateUtil.beginOfDay(new Date()), "yyyy-MM-dd HH:mm:ss");
            end = DateUtil.format(DateUtil.beginOfDay(new Date()), "yyyy-MM-dd HH:mm:ss");
        }
        wrapper.le("tm", end);
        wrapper.ge("tm", start);
        List<VideoStatusRecordHistory> videoStatusRecordHistories = videoStatusRecordHistoryDao.selectList(wrapper);
        if (CollUtil.isNotEmpty(videoStatusRecordHistories)) {
            for (VideoStatusRecordHistory videoStatusRecordHistory : videoStatusRecordHistories) {
                VideoInfoBase videoInfoBase = videoMap.get(videoStatusRecordHistory.getVideoId());
                if (videoInfoBase == null) {
                    continue;
                }
                VideoStatusRecordHistoryDto videoStatusRecordHistoryDto = new VideoStatusRecordHistoryDto();
                videoStatusRecordHistoryDto.setVideoName(videoInfoBase.getName());
                if (videoStatusRecordHistory.getStatus().equals("0")) {
                    videoStatusRecordHistoryDto.setStatus("离线");
                } else {
                    videoStatusRecordHistoryDto.setStatus("在线");
                }
                videoStatusRecordHistoryDto.setTm(videoStatusRecordHistory.getTm());
                res.add(videoStatusRecordHistoryDto);
            }
        }
        return res;
    }

    @Override
    public List<CameraPortalDTO> getCameraPortal(String start, String end) {

        List<CameraPortalDTO> res = new ArrayList<>();
        Date endDate = new Date();
        Date startDate = DateUtil.beginOfDay(endDate);
        if (StrUtil.isNotEmpty(start) && StrUtil.isNotEmpty(end)) {
            startDate = DateUtil.parse(start);
            endDate = DateUtil.parse(end);
        }


        //查询 eventBase 表中的数据 摄像头抓拍事件
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.le("event_time", endDate);
        queryWrapper.ge("event_time", startDate);
        queryWrapper.eq("event_channel", 6);
        List<EventBase> eventBases = eventBaseDao.selectList(queryWrapper);
        Map<String, VideoInfoBase> collect = new HashMap<>();
        List<VideoInfoBase> videoInfoBases = videoInfoBaseDao.selectList(new QueryWrapper<>());
        if (CollUtil.isNotEmpty(videoInfoBases)) {
            collect = videoInfoBases.parallelStream().collect(Collectors.toMap(VideoInfoBase::getIpAddress, Function.identity(), (o1, o2) -> o2));
        }

        String endStr = DateUtil.format(endDate, "yyyy-MM-dd HH:MM:ss").replace(" ", "T");
        String startStr = DateUtil.format(startDate, "yyyy-MM-dd HH:MM:ss").replace(" ", "T");
        QueryWrapper query = new QueryWrapper();
        query.le("ManagerStartTime", endStr);
        query.ge("ManagerStartTime", startStr);
        List<VideoWarningInfoEntity> videoWarningInfoEntities = videoWarningInfoDao.selectList(query);
        Map<Integer, List<VideoWarningInfoEntity>> dbalarmIdMap = new HashMap<>();
        if (CollUtil.isNotEmpty(videoWarningInfoEntities)) {
            dbalarmIdMap = videoWarningInfoEntities.parallelStream().collect(Collectors.groupingBy(VideoWarningInfoEntity::getDBAlarmID));

        }
        if (CollUtil.isNotEmpty(eventBases)) {
            for (EventBase eventBase : eventBases) {
                CameraPortalDTO cameraPortalDTO = new CameraPortalDTO();
                BeanUtil.copyProperties(eventBase, cameraPortalDTO);
                //可以找到唯一的一条摄像头报警事件
                int dbAlarmID = eventBase.getDbAlarmID();
                //通过这个id 去摄像头报警表中 关联摄像头的基本信息

                List<VideoWarningInfoEntity> videoWarningInfoEntitys = dbalarmIdMap.get(dbAlarmID);
                if (CollUtil.isEmpty(videoWarningInfoEntitys)) {
                    continue;
                }
                for (VideoWarningInfoEntity videoWarningInfoEntity : videoWarningInfoEntitys) {
                    if (videoWarningInfoEntity != null) {
                        String scannerIP = videoWarningInfoEntity.getScannerIP();
                        //摄像头的位置基本信息
                        VideoInfoBase videoInfoBase = collect.get(scannerIP);
                        String thumbnailPath = videoWarningInfoEntity.getThumbnailPath();
                        BeanUtil.copyProperties(videoInfoBase, cameraPortalDTO);
                        cameraPortalDTO.setFileUrl(thumbnailPath);
                    }
                    res.add(cameraPortalDTO);
                }

            }
        }
        return res;
    }

    @Override
    public List<String> getCameraWarningInfo() {
        List<String> res = new ArrayList<>();
        Date end = new Date();
        DateTime start = DateUtil.beginOfDay(end);
        String endStr = DateUtil.format(end, "yyyy-MM-dd HH:MM:ss").replace(" ", "T");
        String startStr = DateUtil.format(start, "yyyy-MM-dd HH:MM:ss").replace(" ", "T");
        QueryWrapper query = new QueryWrapper();
        query.le("ManagerStartTime", endStr);
        query.ge("ManagerStartTime", startStr);
        List<VideoWarningInfoEntity> videoWarningInfoEntities = videoWarningInfoDao.selectList(query);
        List<VideoInfoBase> videoInfoBases = videoInfoBaseDao.selectList(new QueryWrapper<>());
        Map<String, VideoInfoBase> videoIpAddrMap = new HashMap();
        if (CollUtil.isNotEmpty(videoInfoBases)) {
            videoIpAddrMap = videoInfoBases.parallelStream().collect(Collectors.toMap(VideoInfoBase::getIpAddress, Function.identity(), (o1, o2) -> o2));
        }
        //先去查询河道数据 id <= 31 则为河道  使用河道id 关联查询设备表中的 rvnm
        QueryWrapper<ReaBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.le("id", 31);
        List<ReaBase> reaBases = reaBaseDao.selectList(queryWrapper);
        Map<String, ReaBase> riverMap = new HashedMap<>();
        if (CollUtil.isNotEmpty(reaBases)) {
            riverMap = reaBases.parallelStream().collect(Collectors.toMap(ReaBase::getId, Function.identity()));
        }
        for (VideoWarningInfoEntity videoWarningInfoEntity : videoWarningInfoEntities) {
            String scannerIP = videoWarningInfoEntity.getScannerIP();
            //报警描述
            String ruleNameStr = videoWarningInfoEntity.getRuleNameStr();
            VideoInfoBase videoInfoBase = videoIpAddrMap.get(scannerIP);
            if (videoInfoBase == null) {
                continue;
            }
            //河道id
            String managerStartTime = videoWarningInfoEntity.getManagerStartTime();
            String stBRiverId = videoInfoBase.getStBRiverId();
            ReaBase reaBase = riverMap.get(stBRiverId);
            String address = videoInfoBase.getAddress();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(managerStartTime.replace("T", SPACE_SPLIT) + SPACE_SPLIT);
            stringBuilder.append(videoInfoBase.getName() + SPACE_SPLIT);
            stringBuilder.append(reaBase.getReaName() + SPACE_SPLIT);
//            stringBuilder.append(address);
            stringBuilder.append(ruleNameStr + SPACE_SPLIT);
            stringBuilder.append("报警" + SPACE_SPLIT);
            String string = stringBuilder.toString();
            res.add(string);
        }
        return res;
    }

    @Override
    public List<VideoInfoBaseDto> getYinShiCode() {
        List<VideoStatusRecordEsr> videoStatusRecordEsrs = videoStatusRecordService.latestYSYStauts();
        Map<Integer, VideoStatusRecordEsr> videoStatusMap;
        if (CollectionUtils.isEmpty(videoStatusRecordEsrs)) {
            videoStatusMap = new HashMap<>();
        } else {
            videoStatusMap = videoStatusRecordEsrs.stream().collect(Collectors.toMap(VideoStatusRecordEsr::getVideoId, Function.identity(), (key1, key2) -> key1));
        }

        List<VideoInfoBase> videoInfoBases = videoInfoBaseDao.selectList(new LambdaQueryWrapper<VideoInfoBase>()
                .eq(VideoInfoBase::getFunctionType, 7)
                .orderByAsc(VideoInfoBase::getSort));
        List<VideoInfoBaseDto> result = videoInfoBases.stream().map(item -> {
            VideoInfoBaseDto infoBaseDto = new VideoInfoBaseDto();
            BeanUtil.copyProperties(item, infoBaseDto);
            VideoStatusRecordEsr videoStatusRecordEsr = videoStatusMap.get(item.getId());
            infoBaseDto.setStatus(null == videoStatusRecordEsr || StringUtils.isEmpty(videoStatusRecordEsr.getStatus()) ? "0" : videoStatusRecordEsr.getStatus());
            return infoBaseDto;
        }).collect(Collectors.toList());

        this.reOrderVideoInfoList(result);
        return result;
    }

    @Override
    @Transactional
    public VideoInfoBaseEsr insertItem(VideoInfoBaseEsu videoInfoBaseEsu) {
        QueryWrapper<VideoInfoBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(VideoInfoBase::getId);
        queryWrapper.lambda().orderByDesc(VideoInfoBase::getId);
        queryWrapper.last("limit 1");
        VideoInfoBase videoInfoBase = videoInfoBaseDao.selectOne(queryWrapper);
        if (null != videoInfoBase.getId()) {
            videoInfoBaseEsu.setId(videoInfoBase.getId() + 1);
            VideoInfoBase bean = converterVideoInfoBaseEtoT.toBean(videoInfoBaseEsu);
            videoInfoBaseDao.insert(bean);
            return converterVideoInfoBaseTtoR.toBean(bean);
        }
        return null;
    }

    @Override
    public VideoInfoBaseDto getByCode(String code) {
        VideoInfoBase videoInfoBase = videoInfoBaseDao.selectOne(new LambdaQueryWrapper<VideoInfoBase>()
                .eq(VideoInfoBase::getCode, code));
        return BeanUtil.copyProperties(videoInfoBase, VideoInfoBaseDto.class);
    }

    @Override
    public List<VideoInfoBaseDto> getYuShiCode() {
        List<VideoStatusRecordEsr> videoStatusRecordEsrs = videoStatusRecordService.latestYUSYStauts();
        Map<Integer, VideoStatusRecordEsr> videoStatusMap;
        if (CollectionUtils.isEmpty(videoStatusRecordEsrs)) {
            videoStatusMap = new HashMap<>();
        } else {
            videoStatusMap = videoStatusRecordEsrs.stream().collect(Collectors.toMap(VideoStatusRecordEsr::getVideoId, Function.identity(), (key1, key2) -> key1));
        }

        List<VideoInfoBase> videoInfoBases = videoInfoBaseDao.selectList(new LambdaQueryWrapper<VideoInfoBase>()
                .eq(VideoInfoBase::getFunctionType, 8)
                .orderByAsc(VideoInfoBase::getSort));
        List<VideoInfoBaseDto> result = videoInfoBases.stream().map(item -> {
            VideoInfoBaseDto infoBaseDto = new VideoInfoBaseDto();
            BeanUtil.copyProperties(item, infoBaseDto);
            VideoStatusRecordEsr videoStatusRecordEsr = videoStatusMap.get(item.getId());
            infoBaseDto.setStatus(null == videoStatusRecordEsr || StringUtils.isEmpty(videoStatusRecordEsr.getStatus()) ? "0" : videoStatusRecordEsr.getStatus());
            return infoBaseDto;
        }).collect(Collectors.toList());

//        this.reOrderVideoInfoList(result);
        return result.stream().filter(p->p.getId().equals(2000) || p.getId().equals(2001)).collect(Collectors.toList());
    }

    @Override
    public Object getYuSYUrl(YuShiCodeModel yuShiCodeModel) {
        if(yuShiCodeModel.getMethod().equals("post")){
            HttpResponse response = HttpRequest.post("https://ezcloud.uniview.com"+yuShiCodeModel.getUrl())
                    .header("Host", "ezcloud.uniview.com")
                    .header("Content-Type", "application/json")
                    .header("Authorization", ysyThird.getToken())
                    .body(JSONUtil.toJsonStr(yuShiCodeModel))
                    .execute();
            return JSONUtil.toBean(response.body(), Map.class);
        }
        if(yuShiCodeModel.getMethod().equals("get")){
            HttpResponse response = HttpRequest.get("https://ezcloud.uniview.com"+yuShiCodeModel.getUrl())
                    .header("Host", "ezcloud.uniview.com")
                    .header("Content-Type", "application/json")
                    .header("Authorization", ysyThird.getToken())
                    .body(JSONUtil.toJsonStr(yuShiCodeModel))
                    .execute();
            return JSONUtil.toBean(response.body(), Map.class);
        }
        return new HashedMap<>();
    }

    @Override
    public String getYuShiToken() {
        return ysyThird.getToken();
    }

    /**
     * 获取 数据库中的YSY 摄像头数据
     *
     * @param accessToken
     * @return
     */
    public List<VideoYsYDto> getActVideo(String accessToken, String unitId) {
        List<VideoYsYDto> list = new ArrayList<>();
        // 获取 萤石云 摄像头id
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("source", "YSY");
        if (!"".equals(unitId) && unitId != null) {
            wrapper.eq("unit_id", unitId);
        }
        List<VideoInfoBase> videoInfoBases = videoInfoBaseDao.selectList(wrapper);
        if (CollUtil.isNotEmpty(videoInfoBases)) {
            for (VideoInfoBase videoInfoBase : videoInfoBases) {
                VideoYsYDto videoYsYDto = new VideoYsYDto();
                // 发送 请求 获取视频连接
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("accessToken", accessToken);
//                paramMap.put("expireTime",expireTime);
                paramMap.put("quality", 2);
                paramMap.put("deviceSerial", videoInfoBase.getCode());
                System.out.println("开始请求获取视频接口");
                String body = HttpRequest.post("https://open.ys7.com/api/lapp/v2/live/address/get")
//                .header("Content-Type", "application/x-www-form-urlencoded")
                        .form(paramMap)
                        .timeout(90000)
                        .execute().body();
                JSONObject jsonObject = JSONObject.parseObject(body);
                String code1 = videoInfoBase.getCode();
                String reaId = videoInfoBase.getReaId();
                if (jsonObject != null) {
                    System.out.println("请求响应参数为:========>" + jsonObject);
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
                    } else {

                    }
                    videoYsYDto.setCode(code1);
                    videoYsYDto.setName(videoInfoBase.getName());
                    videoYsYDto.setReaId(reaId);
                    videoYsYDto.setAccessToken(accessToken);
                    String cameraType = videoInfoBase.getCameraType();
                    videoYsYDto.setCameraType(StrUtil.isEmpty(cameraType) ? null : cameraType);
                    list.add(videoYsYDto);
                }
            }
        }
        return list;
    }


    /**
     * 获取 数据库中的YSY 摄像头数据
     *
     * @param accessToken
     * @return
     */
    public List<VideoYsYDto> getActVideoNew(String accessToken, String unitId, PaginatorParam param, String name) {
        List<VideoYsYDto> list = new ArrayList<>();
        // 获取 萤石云 摄像头id
        QueryWrapper wrappe2 = new QueryWrapper();
        wrappe2.eq("source", "YSY");

        // 获取 萤石云 摄像头id
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("source", "YSY");
        if (!"".equals(unitId) && unitId != null) {
            wrapper.eq("unit_id", unitId);
        }
        if (!"".equals(name) && unitId != name) {
            wrapper.eq("name", name);
        }

        wrapper.orderByAsc("sort");
        //20230710
        String value = null;
        List<Criterion> conditions = param.getConditions();
        if (conditions != null && !"".equals(conditions)) {
            for (int i = 0; i < conditions.size(); i++) {
                String fieldName = conditions.get(i).getFieldName();
                if ("code".equals(fieldName)) {
                    value = (String) conditions.get(i).getValue();
                    break;
                }
            }
        }
        if (!"".equals(value) && unitId != null) {
            wrapper.eq("code", value);
        }
        //20230710
        List<VideoInfoBase> videoInfoBases = videoInfoBaseDao.selectList(wrapper);
        if (CollUtil.isNotEmpty(videoInfoBases)) {
            // 视频最新在新情况
            List<VideoStatusRecordEsr> videoStatusRecordEsrs = videoStatusRecordService.latestStauts();
            Map<Integer, VideoStatusRecordEsr> videoStatusMap;
            if (CollectionUtils.isEmpty(videoStatusRecordEsrs)) {
                videoStatusMap = new HashMap<>();
            } else {
                videoStatusMap = videoStatusRecordEsrs.stream().collect(Collectors.toMap(VideoStatusRecordEsr::getVideoId, Function.identity(), (key1, key2) -> key1));
            }
            for (VideoInfoBase videoInfoBase : videoInfoBases) {
                VideoYsYDto videoYsYDto = new VideoYsYDto();
                Integer id = videoInfoBase.getId();
                videoYsYDto.setId(null != id ? id.toString() : "");
                videoYsYDto.setCode(videoInfoBase.getCode());
                videoYsYDto.setName(videoInfoBase.getName());
                videoYsYDto.setUrl(videoInfoBase.getRemark());
                videoYsYDto.setReaId(videoInfoBase.getReaId());
                videoYsYDto.setAccessToken(accessToken);
                String cameraType = videoInfoBase.getCameraType();
                videoYsYDto.setCameraType(StrUtil.isEmpty(cameraType) ? null : cameraType);
                //摄像头的在线状态从状态表中获取
                VideoStatusRecordEsr videoStatusRecordEsr = videoStatusMap.get(videoInfoBase.getId());
                videoYsYDto.setStatus(null == videoStatusRecordEsr || StringUtils.isEmpty(videoStatusRecordEsr.getStatus()) ? 0 : Integer.valueOf(videoStatusRecordEsr.getStatus()));
                list.add(videoYsYDto);

            }
        }
        return list;
    }


    public Map getLoginInfo() {
        Map<String, Object> map = new HashMap<>();
        //登陆获取 appKey
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("appKey", "056898dbe3d34a068f9c10148855a700");
        paramMap.put("appSecret", "26170abb0fd97a6fde9ed6d2a3fbfbed");

        System.out.println("开始请求登陆接口");
        String body = HttpRequest.post("https://open.ys7.com/api/lapp/token/get")
//                .header("Content-Type", "application/x-www-form-urlencoded")
                .form(paramMap)
                .timeout(90000)
                .execute().body();
        JSONObject jsonObject = JSONObject.parseObject(body);

        if (Objects.nonNull(jsonObject)) {
            System.out.println("请求响应参数为:========>" + jsonObject);
            String code = jsonObject.getString("code");
            if (code.equals("200")) {
                JSONObject result = jsonObject.getJSONObject("data");
                String accessToken = result.getString("accessToken");
                Integer expireTime = result.getInteger("expireTime");
                map.put("accessToken", accessToken);
                map.put("expireTime", expireTime);
            } else {
                System.out.println("请求失败 切状态为====================>" + jsonObject);
                throw new RuntimeException("请求失败 切状态为====================>" + jsonObject);
            }
        }
        return map;
    }

    public Boolean save(double longitudeFrom, double latitudeFrom, double longitudeTo,
                        double latitudeTo) {
        Double distance = GisUtils.distance(longitudeFrom, latitudeFrom, longitudeTo, latitudeTo);
        if (distance < 2000) {
            return true;
        } else {
            return false;
        }
    }


}
