package com.essence.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.essence.common.dto.*;
import com.essence.common.utils.PageUtil;
import com.essence.common.utils.ResponseResult;
import com.essence.dao.*;
import com.essence.dao.entity.VideoInfoBase;
import com.essence.entity.*;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author cuirx
 * @Classname StationInfoServiceImpl
 * @Description TODO
 * @Date 2022/10/14 16:14
 * @Created by essence
 */

@Service
public class StationInfoServiceImpl implements StationService {
    @Resource
    private DeviceDataStatisticDao deviceDataStatisticDao;
    @Resource
    private WaterPositionStatisticDao waterPositionStatisticDao;

    @Resource
    private StSnConvertDao stSnConvertDao;

    @Autowired
    private StStbprpBDao stStbprpBDao;
    @Resource
    private StWaterRateDao stWaterRateDao;
    @Resource
    private DeviceQualifiedDao deviceQualifiedDao;
    @Resource
    private DeviceStatusDao deviceStatusDao;
    @Resource
    private OnlineCheckDeviceDao onlineCheckDeviceDao;
    @Resource
    private DeviceFellDao deviceFellDao;
    @Resource
    private VideoInfoBaseDao videoInfoBaseDao;
    @Autowired
    private ReaBaseDao reaBaseDao;


    @Override
    public ResponseResult getStationInfoList(StStbprpBEntityDTO requestStStbprpBEntityDTO) {
        //先去查询河道数据 id <= 31 则为河道  使用河道id 关联查询设备表中的 rvnm
        QueryWrapper<ReaBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.le("id", 31);
        List<ReaBase> reaBases = reaBaseDao.selectList(queryWrapper);
        Map<String, ReaBase> riverMap = new HashedMap<>();
        if (CollUtil.isNotEmpty(reaBases)){
            riverMap = reaBases.parallelStream().collect(Collectors.toMap(ReaBase::getId, Function.identity()));
        }
        QueryWrapper<StStbprpBEntity> wrapper = new QueryWrapper<>();
        if (StrUtil.isNotEmpty(requestStStbprpBEntityDTO.getSttp()) ){
            wrapper.eq("sttp",requestStStbprpBEntityDTO.getSttp());
        }
        if (StrUtil.isNotEmpty(requestStStbprpBEntityDTO.getStnm()) ){
            wrapper.like("stnm",requestStStbprpBEntityDTO.getStnm());

        }
        if (StrUtil.isNotEmpty(requestStStbprpBEntityDTO.getArea()) ){
            wrapper.like("area",requestStStbprpBEntityDTO.getArea());
        }
        if (StrUtil.isNotEmpty(requestStStbprpBEntityDTO.getRvnm()) ){
            wrapper.like("rvnm",requestStStbprpBEntityDTO.getRvnm());
        }
        if (StrUtil.isNotEmpty(requestStStbprpBEntityDTO.getStlc()) ){
            wrapper.like("stlc",requestStStbprpBEntityDTO.getStlc());
        }
        if (StrUtil.isNotEmpty(requestStStbprpBEntityDTO.getSttp()) ){
            wrapper.eq("sttp",requestStStbprpBEntityDTO.getSttp());
        }
        if (CollUtil.isNotEmpty(requestStStbprpBEntityDTO.getSttps()) ){
            wrapper.in("sttp",requestStStbprpBEntityDTO.getSttps());
        }
        Date toDay = new Date();
//        IPage pageInfo = new Page(requestStStbprpBEntityDTO.getCurrent(),requestStStbprpBEntityDTO.getSize());
        List<StStbprpBEntity> stStbprpBEntities = stStbprpBDao.selectList(wrapper);
        List<StStbprpBEntityDTO> list =  new ArrayList<>();
        if (!CollectionUtils.isEmpty(stStbprpBEntities)){
            List<DeviceStatusEntity> deviceStatusEntities = deviceStatusDao.selectList(new QueryWrapper<>());
            Map<String, DeviceStatusEntity> stcdMap = new HashedMap<>();
            if (CollUtil.isNotEmpty(deviceStatusEntities)){
                stcdMap = deviceStatusEntities.parallelStream().collect(Collectors.toMap(DeviceStatusEntity::getStcd, Function.identity()));
            }
            for (StStbprpBEntity stStbprpBEntity : stStbprpBEntities) {
                StStbprpBEntityDTO stStbprpBEntityDTO = new StStbprpBEntityDTO();
                BeanUtil.copyProperties(stStbprpBEntity,stStbprpBEntityDTO);
                DeviceStatusEntity deviceStatusEntity = stcdMap.get(stStbprpBEntity.getStcd());
                if (StrUtil.isNotEmpty(stStbprpBEntity.getRvnm())){
                    ReaBase reaBase = riverMap.get(stStbprpBEntityDTO.getRvnm());
                    stStbprpBEntityDTO.setRvnm(reaBase.getReaName());
                }

                if (deviceStatusEntity != null){
                    BeanUtil.copyProperties(deviceStatusEntity,stStbprpBEntityDTO);
                }else {
                    stStbprpBEntityDTO.setOnlineStatus("2");
                }
                list.add(stStbprpBEntityDTO);
            }

        }
        //如果 场站警戒状态不为空 则
        if (requestStStbprpBEntityDTO.getWarningStatus() != null){
            list = list.parallelStream().filter(stStbprpBEntityDTO -> {
                return stStbprpBEntityDTO.getWarningStatus() != null && stStbprpBEntityDTO.getWarningStatus().equals(requestStStbprpBEntityDTO.getWarningStatus()) ;
            }).collect(Collectors.toList());
        }

        PageUtil pages = new PageUtil(list,requestStStbprpBEntityDTO.getCurrent(),requestStStbprpBEntityDTO.getSize(),null,null);
        return ResponseResult.success("获取成功",pages);
    }

    @Override
    public List<StWaterRateEntityDTO> getStationDataList(StStbprpBEntityDTO stStbprpBEntityDTO) throws ParseException {
        List<StWaterRateEntityDTO> list = new ArrayList<>();
        List<StStbprpBEntity> stStbprpBEntities = stStbprpBDao.selectList(new QueryWrapper<>());
        Map<String, StStbprpBEntity> stationMap = stStbprpBEntities.parallelStream().collect(Collectors.toMap(StStbprpBEntity::getStcd, Function.identity()));
        StStbprpBEntity stStbprpBEntity = stationMap.get(stStbprpBEntityDTO.getStcd());
        //1.通过前端传递的stcd 去表中获取详细的 监测数据  分为两种情况 水位流量需要找对应的关系 进行转换一下sn码再进行查询  ,流量则不需要
        if (stStbprpBEntityDTO.getSttp().equals("ZZ")){
            Map<String, String> stcdMap = stStbprpBEntityDTO.getStcdMap();
            if (CollUtil.isEmpty(stcdMap)){
                List<StSnConvertEntity> list1 = stSnConvertDao.selectList(new QueryWrapper<>());
                stcdMap = list1.parallelStream().collect(Collectors.toMap(StSnConvertEntity::getStcd, StSnConvertEntity::getSn));
            }
            if (stcdMap != null){
                QueryWrapper<StWaterRateEntity>  wrapper= new QueryWrapper<>();
                wrapper.eq("did",stcdMap.get(stStbprpBEntityDTO.getStcd()));
                if (StrUtil.isNotEmpty(stStbprpBEntityDTO.getEndTime()) ){
                    DateTime dateTime = DateUtil.parseDate(stStbprpBEntityDTO.getEndTime());
                    String format = DateUtil.format(dateTime, "yyyy/MM/dd HH:mm:ss");
                    wrapper.le("ctime",format);
                }
                if (StrUtil.isNotEmpty(stStbprpBEntityDTO.getStartTime()) ){
                    DateTime dateTime = DateUtil.parseDate(stStbprpBEntityDTO.getStartTime());
                    String format = DateUtil.format(dateTime, "yyyy/MM/dd HH:mm:ss");
                    wrapper.ge("ctime",format);
                }
                List<StWaterRateEntity> stWaterRateEntities = stWaterRateDao.selectList(wrapper);
                if (!CollectionUtils.isEmpty(stWaterRateEntities)){
                    // 以小时为分组 取最后一个小时的数据
                    Map<String, List<StWaterRateEntity>> ctTimeMapList = stWaterRateEntities.parallelStream().collect(Collectors.groupingBy(stWaterRateEntity -> {
                        return stWaterRateEntity.getCtime().substring(0, 13);
                    }));
                    for (String ct : ctTimeMapList.keySet()) {
                        StWaterRateEntity stWaterRateEntity = ctTimeMapList.get(ct).get(0);
                        StWaterRateEntityDTO stStbprpBEntityDTO1 = new StWaterRateEntityDTO();
                        //水位 从毫米 转换为m
                        String addrv = stWaterRateEntity.getAddrv();
                        if (StrUtil.isEmpty(addrv)){
                            addrv = "0";
                        }
                        BigDecimal meter = new BigDecimal(addrv).divide(new BigDecimal(1000),2,BigDecimal.ROUND_HALF_UP);
                        BeanUtils.copyProperties(stWaterRateEntity, stStbprpBEntityDTO1);
                        stStbprpBEntityDTO1.setAddrv(meter.toString());
                        if (StrUtil.isNotEmpty(stWaterRateEntity.getCtime())){
                            String ctime = stWaterRateEntity.getCtime();
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                            Date parse = formatter.parse(ctime);
//                            DateTime dateTime = DateUtil.parseDate(ctime);
                            String format = DateUtil.format(parse, "yyyy-MM-dd HH:mm:ss");
                            stStbprpBEntityDTO1.setCtime(format);
                            stStbprpBEntityDTO1.setMDh(format.substring(5,16));
                            //高程
                            BigDecimal dtmel = stStbprpBEntity.getDtmel() == null ? new BigDecimal(0) :stStbprpBEntity.getDtmel();

                            stStbprpBEntityDTO1.setWaterDeep(meter.add(dtmel));

                        }
                        list.add(stStbprpBEntityDTO1);
                    }
                }
            }

        }else {
            QueryWrapper<StWaterRateEntity>  wrapper= new QueryWrapper<>();
            if (stStbprpBEntityDTO.getStcd().length() <=3){
                return null;
            }
            wrapper.eq("did",stStbprpBEntityDTO.getStcd().substring(2));
            if (StrUtil.isNotEmpty(stStbprpBEntityDTO.getEndTime()) ){
                wrapper.le("ctime",stStbprpBEntityDTO.getEndTime());
            }
            if (StrUtil.isNotEmpty(stStbprpBEntityDTO.getStartTime()) ){
                wrapper.ge("ctime",stStbprpBEntityDTO.getStartTime());
            }
            List<StWaterRateEntity> stWaterRateEntities = stWaterRateDao.selectList(wrapper);
            if (!CollectionUtils.isEmpty(stWaterRateEntities)){
                // 以小时为分组 取最后一个小时的数据
                Map<String, List<StWaterRateEntity>> ctTimeMapList = stWaterRateEntities.parallelStream().collect(Collectors.groupingBy(stWaterRateEntity -> {
                    return stWaterRateEntity.getCtime().substring(0, 13);
                }));
                for (String ct : ctTimeMapList.keySet()) {
                    StWaterRateEntity stWaterRateEntity = ctTimeMapList.get(ct).get(0);
                    StWaterRateEntityDTO stWaterRateEntityDTO = new StWaterRateEntityDTO();
                    BeanUtils.copyProperties(stWaterRateEntity, stWaterRateEntityDTO);
                    if (StrUtil.isNotEmpty(stWaterRateEntity.getCtime())){
                        stWaterRateEntityDTO.setMDh(stWaterRateEntity.getCtime().substring(5,16));
                        stWaterRateEntityDTO.setAddrv(stWaterRateEntity.getMomentRate() == null ? "0": stWaterRateEntity.getMomentRate() );
                        //高程
                        BigDecimal dtmel = stStbprpBEntity.getDtmel() == null ? new BigDecimal(0) :stStbprpBEntity.getDtmel();
                        stWaterRateEntityDTO.setWaterDeep(new BigDecimal(stWaterRateEntityDTO.getAddrv()).add(dtmel));
                    }
                    list.add(stWaterRateEntityDTO);
                }
            }

        }
        if (CollUtil.isNotEmpty(list)){
            list = list.stream().sorted(Comparator.comparing(StWaterRateEntityDTO::getCtime)).collect(Collectors.toList());
        }
        return list;
    }


    @Override
    public List<StWaterRateEntityDTO> getDataList(StStbprpBEntityDTO stStbprpBEntityDTO) throws ParseException {
        List<StWaterRateEntityDTO> list = new ArrayList<>();
        List<StStbprpBEntity> stStbprpBEntities = stStbprpBDao.selectList(new QueryWrapper<>());
        Map<String, StStbprpBEntity> stationMap = stStbprpBEntities.parallelStream().collect(Collectors.toMap(StStbprpBEntity::getStcd, Function.identity()));
        StStbprpBEntity stStbprpBEntity = stationMap.get(stStbprpBEntityDTO.getStcd());
        //1.通过前端传递的stcd 去表中获取详细的 监测数据  分为两种情况 水位流量需要找对应的关系 进行转换一下sn码再进行查询  ,流量则不需要
        if (stStbprpBEntityDTO.getSttp().equals("ZZ")){
            Map<String, String> stcdMap = stStbprpBEntityDTO.getStcdMap();
            if (CollUtil.isEmpty(stcdMap)){
                List<StSnConvertEntity> list1 = stSnConvertDao.selectList(new QueryWrapper<>());
                stcdMap = list1.parallelStream().collect(Collectors.toMap(StSnConvertEntity::getStcd, StSnConvertEntity::getSn));
            }
            if (stcdMap != null){
                QueryWrapper<StWaterRateEntity>  wrapper= new QueryWrapper<>();
                wrapper.eq("did",stcdMap.get(stStbprpBEntityDTO.getStcd()));
                if (StrUtil.isNotEmpty(stStbprpBEntityDTO.getEndTime()) ){
                    wrapper.le("ctime",stStbprpBEntityDTO.getEndTime());
                }
                if (StrUtil.isNotEmpty(stStbprpBEntityDTO.getStartTime()) ){
                    wrapper.ge("ctime",stStbprpBEntityDTO.getStartTime());
                }
                List<StWaterRateEntity> stWaterRateEntities = stWaterRateDao.selectList(wrapper);
                if (!CollectionUtils.isEmpty(stWaterRateEntities)){
                    // 以小时为分组 取最后一个小时的数据
                    Map<String, List<StWaterRateEntity>> ctTimeMapList = stWaterRateEntities.parallelStream().collect(Collectors.groupingBy(stWaterRateEntity -> {
                        return stWaterRateEntity.getCtime().substring(0, 13);
                    }));
                    for (String ct : ctTimeMapList.keySet()) {
                        StWaterRateEntity stWaterRateEntity = ctTimeMapList.get(ct).get(0);
                        StWaterRateEntityDTO stStbprpBEntityDTO1 = new StWaterRateEntityDTO();
                        //水位 从毫米 转换为m
                        String addrv = stWaterRateEntity.getAddrv();
                        if (StrUtil.isEmpty(addrv)){
                            addrv = "0";
                        }
                        BigDecimal meter = new BigDecimal(addrv).divide(new BigDecimal(1000),2,BigDecimal.ROUND_HALF_UP);
                        BeanUtils.copyProperties(stWaterRateEntity, stStbprpBEntityDTO1);
                        stStbprpBEntityDTO1.setAddrv(meter.toString());
                        if (StrUtil.isNotEmpty(stWaterRateEntity.getCtime())){
                            String ctime = stWaterRateEntity.getCtime();
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                            Date parse = formatter.parse(ctime);
//                            DateTime dateTime = DateUtil.parseDate(ctime);
                            String format = DateUtil.format(parse, "yyyy-MM-dd HH:mm:ss");
                            stStbprpBEntityDTO1.setCtime(format);
                            stStbprpBEntityDTO1.setMDh(format.substring(5,16));
                            //高程
                            BigDecimal dtmel = stStbprpBEntity.getDtmel() == null ? new BigDecimal(0) :stStbprpBEntity.getDtmel();

                            stStbprpBEntityDTO1.setWaterDeep(meter.add(dtmel));

                        }
                        list.add(stStbprpBEntityDTO1);
                    }
                }
            }

        }else {
            QueryWrapper<StWaterRateEntity>  wrapper= new QueryWrapper<>();
            if (stStbprpBEntityDTO.getStcd().length() <=3){
                return null;
            }
            wrapper.eq("did",stStbprpBEntityDTO.getStcd().substring(2));
            if (StrUtil.isNotEmpty(stStbprpBEntityDTO.getEndTime()) ){
                wrapper.le("ctime",stStbprpBEntityDTO.getEndTime());
            }
            if (StrUtil.isNotEmpty(stStbprpBEntityDTO.getStartTime()) ){
                wrapper.ge("ctime",stStbprpBEntityDTO.getStartTime());
            }
            List<StWaterRateEntity> stWaterRateEntities = stWaterRateDao.selectList(wrapper);
            if (!CollectionUtils.isEmpty(stWaterRateEntities)){
                // 以小时为分组
                Map<String, List<StWaterRateEntity>> ctTimeMapList = stWaterRateEntities.parallelStream().collect(Collectors.groupingBy(stWaterRateEntity -> {
                    return stWaterRateEntity.getCtime().substring(0, 13);
                }));
                for (String ct : ctTimeMapList.keySet()) {
                    StWaterRateEntity stWaterRateEntity = ctTimeMapList.get(ct).get(0);
                    StWaterRateEntityDTO stWaterRateEntityDTO = new StWaterRateEntityDTO();
                    BeanUtils.copyProperties(stWaterRateEntity, stWaterRateEntityDTO);
                    if (StrUtil.isNotEmpty(stWaterRateEntity.getCtime())){
                        stWaterRateEntityDTO.setMDh(stWaterRateEntity.getCtime().substring(5,16));
                        stWaterRateEntityDTO.setAddrv(stWaterRateEntity.getMomentRate() == null ? "0": stWaterRateEntity.getMomentRate() );
                        //高程
                        BigDecimal dtmel = stStbprpBEntity.getDtmel() == null ? new BigDecimal(0) :stStbprpBEntity.getDtmel();
                        stWaterRateEntityDTO.setWaterDeep(new BigDecimal(stWaterRateEntityDTO.getAddrv()).add(dtmel));
                    }
                    list.add(stWaterRateEntityDTO);
                }
            }
        }
        if (CollUtil.isNotEmpty(list)){
            list = list.stream().sorted(Comparator.comparing(StWaterRateEntityDTO::getCtime)).collect(Collectors.toList());
        }
        return list;
    }

    @Override
    public void updateById(StStbprpBEntityDTO stStbprpB) {
        StStbprpBEntity stbprpBEntity = new StStbprpBEntity();
        BeanUtils.copyProperties(stStbprpB,stbprpBEntity);
        stStbprpBDao.updateById(stbprpBEntity);
    }

    @Override
    public void removeByIds(List<String> asList) {

        QueryWrapper<StStbprpBEntity> wrapper = new QueryWrapper<>();
        wrapper.in("stcd",asList);
        stStbprpBDao.delete(wrapper);

    }

    @Override
    public void add(StStbprpBEntityDTO stStbprpB) {
        QueryWrapper<StStbprpBEntity> wrapper =new QueryWrapper();
        wrapper.eq("stcd",stStbprpB.getStcd());
        List<StStbprpBEntity> stStbprpBEntities = stStbprpBDao.selectList(wrapper);
        if (!CollectionUtils.isEmpty(stStbprpBEntities)){
            throw new RuntimeException("测站id不能重复");
        }
        StStbprpBEntity stbprpBEntity = new StStbprpBEntity();
        BeanUtils.copyProperties(stStbprpB,stbprpBEntity);

        stStbprpBDao.insert(stbprpBEntity);
    }

    @Override
    public List<StStbprpBEntityDTO> getStationExcelList(StStbprpBEntityDTO requestStStbprpBEntityDTO) {

        QueryWrapper<StStbprpBEntity> wrapper = new QueryWrapper<>();
        if (StrUtil.isNotEmpty(requestStStbprpBEntityDTO.getStnm()) ){
            wrapper.eq("stnm",requestStStbprpBEntityDTO.getStnm());

        }
        if (StrUtil.isNotEmpty(requestStStbprpBEntityDTO.getRvnm()) ){
            wrapper.eq("rvnm",requestStStbprpBEntityDTO.getRvnm());
        }
        if (StrUtil.isNotEmpty(requestStStbprpBEntityDTO.getStlc()) ){
            wrapper.eq("stlc",requestStStbprpBEntityDTO.getStlc());
        }
        if (StrUtil.isNotEmpty(requestStStbprpBEntityDTO.getSttp()) ){
            wrapper.eq("sttp",requestStStbprpBEntityDTO.getSttp());
        }else {
            wrapper.eq("sttp","ZZ").or().eq("sttp","ZQ");
        }
        IPage pageInfo = new Page(requestStStbprpBEntityDTO.getCurrent(),requestStStbprpBEntityDTO.getSize());
        IPage pages = stStbprpBDao.selectPage(pageInfo, wrapper);
        List<StStbprpBEntity> stStbprpBEntities = pages.getRecords();
        List<StStbprpBEntityDTO> list =  new ArrayList<>();
        if (!CollectionUtils.isEmpty(stStbprpBEntities)){
            for (StStbprpBEntity stStbprpBEntity : stStbprpBEntities) {
                StStbprpBEntityDTO stStbprpBEntityDTO = new StStbprpBEntityDTO();
                BeanUtils.copyProperties(stStbprpBEntity, stStbprpBEntityDTO);
                list.add(stStbprpBEntityDTO);
            }
        }
        return list;
    }

    @Override
    public DeviceStatusDTO getDeviceRunStatus() {
        List<DeviceDataStatisticEntity> deviceDataStatisticEntities = deviceDataStatisticDao.selectList(new QueryWrapper<>());
        Map<String, DeviceDataStatisticEntity> typeMap = new HashedMap<>();
        if (CollUtil.isNotEmpty(deviceDataStatisticEntities)){
            typeMap = deviceDataStatisticEntities.parallelStream().collect(Collectors.toMap(DeviceDataStatisticEntity::getType, Function.identity()));
        }
        DeviceStatusDTO deviceStatusDTO = new DeviceStatusDTO();
        //水位
        DeviceDataStatisticEntity zz = typeMap.get("ZZ");
        if (zz != null){
            DeviceDataStatisticDTO water = new DeviceDataStatisticDTO();
            water.setOnline(zz.getOnline());
            water.setType("ZZ");
            water.setTotal(zz.getTotal());
            deviceStatusDTO.setWaterPosition(water);
        }

        //流量
        DeviceDataStatisticEntity zq = typeMap.get("ZQ");
        if (zq != null){
            DeviceDataStatisticDTO flowRate = new DeviceDataStatisticDTO();
            flowRate.setOnline(zq.getOnline());
            flowRate.setType("ZQ");
            flowRate.setTotal(zq.getTotal());
            deviceStatusDTO.setFlowRate(flowRate);
        }
        //流量
        DeviceDataStatisticEntity pp = typeMap.get("PP");
        if (pp != null){
            DeviceDataStatisticDTO rainRate = new DeviceDataStatisticDTO();
            rainRate.setOnline(pp.getOnline());
            rainRate.setType("PP");
            rainRate.setTotal(pp.getTotal());
            deviceStatusDTO.setRainRate(rainRate);
        }
        return deviceStatusDTO;
    }

    @Override
    public WaterPositionStatisticDTO getStationStatus() {
        WaterPositionStatisticEntity  waterPositionStatisticEntity = waterPositionStatisticDao.selectOne(new QueryWrapper<>());
        WaterPositionStatisticDTO waterPositionStatisticDTO = new WaterPositionStatisticDTO();
        if (waterPositionStatisticEntity != null){
            BeanUtil.copyProperties(waterPositionStatisticEntity,waterPositionStatisticDTO);
        }
        return waterPositionStatisticDTO;
    }

    @Override
    public DeviceFeelCountDTO getDeviceFellStatistic(DeviceFeelRequestDTO deviceFeelRequestDTO) {
        DeviceFeelCountDTO result = new DeviceFeelCountDTO();
        if (deviceFeelRequestDTO.getDateType().equals("1")){
            result = getYearStatistic( deviceFeelRequestDTO);
        }
        if (deviceFeelRequestDTO.getDateType().equals("2")){
            result = getMonthStatistic(deviceFeelRequestDTO);
        }
        if (deviceFeelRequestDTO.getDateType().equals("3")){
            result = getDayStatistic(deviceFeelRequestDTO);
        }
        return result;
    }
    @Override
    public OnlineCheckDeviceDTO getDeviceOnlineStatistic(DeviceFeelRequestDTO deviceFeelRequestDTO) {
        OnlineCheckDeviceDTO result = new OnlineCheckDeviceDTO();
        String format = DateUtil.format(deviceFeelRequestDTO.getDate(), "yyyy-MM-dd");
        if (deviceFeelRequestDTO.getDateType().equals("1")){
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("stcd",deviceFeelRequestDTO.getStcd());
            wrapper.eq("date_type",deviceFeelRequestDTO.getDateType());
            wrapper.eq("date",format.substring(0,4));
            OnlineCheckDeviceEntity onlineCheckDeviceEntity = onlineCheckDeviceDao.selectOne(wrapper);
            if (onlineCheckDeviceEntity != null){
                String json = onlineCheckDeviceEntity.getStatus();
                List<Integer> status = JSONObject.parseArray(json, Integer.class);
                BeanUtil.copyProperties(onlineCheckDeviceEntity,result);
                result.setStatus(status);
            }
        }
        if (deviceFeelRequestDTO.getDateType().equals("2")){
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("stcd",deviceFeelRequestDTO.getStcd());
            wrapper.eq("date_type",deviceFeelRequestDTO.getDateType());
            wrapper.eq("date",format.substring(0,7));
            OnlineCheckDeviceEntity onlineCheckDeviceEntity = onlineCheckDeviceDao.selectOne(wrapper);
            if (onlineCheckDeviceEntity != null){
                String json = onlineCheckDeviceEntity.getStatus();
                List<Integer> status = JSONObject.parseArray(json, Integer.class);
                BeanUtil.copyProperties(onlineCheckDeviceEntity,result);
                result.setStatus(status);
            }
        }
        if (deviceFeelRequestDTO.getDateType().equals("3")){
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("stcd",deviceFeelRequestDTO.getStcd());
            wrapper.eq("date_type",deviceFeelRequestDTO.getDateType());
            wrapper.eq("date",format);
            OnlineCheckDeviceEntity onlineCheckDeviceEntity = onlineCheckDeviceDao.selectOne(wrapper);
            if (onlineCheckDeviceEntity != null){
                String json = onlineCheckDeviceEntity.getStatus();
                List<Integer> status = JSONObject.parseArray(json, Integer.class);
                BeanUtil.copyProperties(onlineCheckDeviceEntity,result);
                result.setStatus(status);
            }
        }
        return result;
    }



    @Override
    public void setStationCheckConfig(List<QualifiedConfigDTO> qualifiedConfigDTOs) {
        if (CollUtil.isNotEmpty(qualifiedConfigDTOs)){
            for (QualifiedConfigDTO qualifiedConfigDTO : qualifiedConfigDTOs) {
                DeviceQualifiedEntity deviceQualifiedEntity = new DeviceQualifiedEntity();
                BeanUtil.copyProperties(qualifiedConfigDTO,deviceQualifiedEntity);
                //1 通过类型 去查询之前是否有数 如果有则更新 没有则新增
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("device_type",deviceQualifiedEntity.getDeviceType());
                queryWrapper.eq("date_type",deviceQualifiedEntity.getDateType());
                queryWrapper.eq("tm",deviceQualifiedEntity.getTm());
                DeviceQualifiedEntity findOne = deviceQualifiedDao.selectOne(queryWrapper);
                if (findOne != null){
                    //更新
                    deviceQualifiedDao.update(deviceQualifiedEntity,queryWrapper);
                    continue;
                }
                deviceQualifiedDao.insert(deviceQualifiedEntity);
            }
        }
    }

    @Override
    public PageUtil<OnlineDeviceDetailDTO> getStationStatusList(StationStatusRequestDTO stationStatusRequestDTO) {

        List<OnlineDeviceDetailDTO>  list = new ArrayList<>();
        Date date = stationStatusRequestDTO.getDate();
        String format = DateUtil.format(date, "yyyy-MM-dd");
        Integer checkStatus = stationStatusRequestDTO.getChecked();
        Integer dateType = stationStatusRequestDTO.getDateType();
        QueryWrapper<StStbprpBEntity> wrapper = new QueryWrapper<>();

        if (StrUtil.isNotEmpty(stationStatusRequestDTO.getStnm()) ){
            wrapper.eq("stnm",stationStatusRequestDTO.getStnm());
        }
        if (StrUtil.isNotEmpty(stationStatusRequestDTO.getRvnm()) ){
            wrapper.eq("rvnm",stationStatusRequestDTO.getRvnm());
        }
        if (StrUtil.isNotEmpty(stationStatusRequestDTO.getSttp()) ){
            wrapper.eq("sttp",stationStatusRequestDTO.getSttp());
        }
        List<StStbprpBEntity> stStbprpBEntities = stStbprpBDao.selectList(wrapper);
        //将视频列表加入
        QueryWrapper<VideoInfoBase> videoWrapper = new QueryWrapper<>();
        if (StrUtil.isNotEmpty(stationStatusRequestDTO.getStnm()) ){
            videoWrapper.like("name",stationStatusRequestDTO.getStnm());
        }
        if (StrUtil.isNotEmpty(stationStatusRequestDTO.getRvnm()) ){
            videoWrapper.eq("st_b_river_id",stationStatusRequestDTO.getRvnm());
        }
        if (StrUtil.isEmpty(stationStatusRequestDTO.getSttp()) || stationStatusRequestDTO.getSttp().equals("VIDEO")){
            List<VideoInfoBase> videoInfoBases = videoInfoBaseDao.selectList(videoWrapper);
            if (CollUtil.isNotEmpty(videoInfoBases)){
                for (VideoInfoBase videoInfoBase : videoInfoBases) {
                    StStbprpBEntity stStbprpBEntity = new StStbprpBEntity();
                    stStbprpBEntity.setStcd(videoInfoBase.getId().toString());
                    stStbprpBEntity.setStnm(videoInfoBase.getName()  == null? null : videoInfoBase.getName()  );
                    stStbprpBEntity.setLgtd(videoInfoBase.getLgtd() == null? null : new BigDecimal(videoInfoBase.getLgtd()) );
                    stStbprpBEntity.setLttd(videoInfoBase.getLttd() == null? null : new BigDecimal(videoInfoBase.getLttd()));
                    stStbprpBEntity.setLocality(videoInfoBase.getAddress() == null ? null : videoInfoBase.getAddress());
                    stStbprpBEntity.setSttp("VIDEO");
                    stStbprpBEntity.setRvnm(videoInfoBase.getStBRiverId());
                    stStbprpBEntities.add(stStbprpBEntity);
                }
            }
        }
        //查询 河流
        //先去查询河道数据 id <= 31 则为河道  使用河道id 关联查询设备表中的 rvnm
        QueryWrapper<ReaBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.le("id", 31);
        List<ReaBase> reaBases = reaBaseDao.selectList(queryWrapper);
        Map<String, ReaBase> riverMap = new HashedMap<>();
        if (CollUtil.isNotEmpty(reaBases)){
            riverMap = reaBases.parallelStream().collect(Collectors.toMap(ReaBase::getId, Function.identity()));
        }
        if (CollUtil.isNotEmpty(stStbprpBEntities)){
            for (StStbprpBEntity stStbprpBEntity : stStbprpBEntities) {
                OnlineDeviceDetailDTO onlineDeviceDetailDTO = new OnlineDeviceDetailDTO();
                BeanUtil.copyProperties(stStbprpBEntity,onlineDeviceDetailDTO);
                if (StrUtil.isNotEmpty(stStbprpBEntity.getRvnm())){
                    ReaBase reaBase = riverMap.get(stStbprpBEntity.getRvnm());
                    onlineDeviceDetailDTO.setRvnm(reaBase.getReaName());
                }
                String stcd = stStbprpBEntity.getStcd();
                QueryWrapper onlineWrapper = new QueryWrapper();
                if (stationStatusRequestDTO.getDateType().equals(1)){
                    onlineWrapper.eq("stcd",stcd);
                    onlineWrapper.eq("date_type",dateType);
                    onlineWrapper.eq("date",format.substring(0,4));
                }
                if (stationStatusRequestDTO.getDateType().equals(2)){
                    onlineWrapper.eq("stcd",stcd);
                    onlineWrapper.eq("date_type",dateType);
                    onlineWrapper.eq("date",format.substring(0,7));
                }
                if (stationStatusRequestDTO.getDateType().equals(3)){
                    onlineWrapper.eq("stcd",stcd);
                    onlineWrapper.eq("date_type",dateType);
                    onlineWrapper.eq("date",format.substring(0,4));
                }
                OnlineCheckDeviceEntity onlineCheckDeviceEntity = onlineCheckDeviceDao.selectOne(onlineWrapper);
                if (onlineCheckDeviceEntity != null){
                    String status = onlineCheckDeviceEntity.getStatus();
                    List<Integer> integers = JSONObject.parseArray(status, Integer.class);
                    List<Integer> onlineStatus = integers.parallelStream().filter(integer -> {
                        return integer.equals(1);
                    }).collect(Collectors.toList());
                    BigDecimal value =   new BigDecimal(onlineStatus.size()).divide(new BigDecimal(integers.size()),2,BigDecimal.ROUND_HALF_UP);
                    onlineDeviceDetailDTO.setTotal(integers.size());
                    onlineDeviceDetailDTO.setFactNum(onlineStatus.size());
                    onlineDeviceDetailDTO.setPercent(value);
                    if (value.compareTo(new BigDecimal(0.9)) >= 0){
                        onlineDeviceDetailDTO.setChecked(1);
                    }else {
                        onlineDeviceDetailDTO.setChecked(2);
                    }

                }
                list.add(onlineDeviceDetailDTO);
            }
        }
        List<OnlineDeviceDetailDTO> checked = list.parallelStream().filter(onlineDeviceDetailDTO -> {
            return (onlineDeviceDetailDTO.getChecked() != null) && (checkStatus != null)  && (onlineDeviceDetailDTO.getChecked().equals(checkStatus)) ;
        }).collect(Collectors.toList());
        BigDecimal percent = new BigDecimal(0);
        if (CollUtil.isNotEmpty(list)){
            percent = new BigDecimal(checked.size()).divide(new BigDecimal(list.size()),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
        }
        if (checkStatus != null){
            PageUtil<OnlineDeviceDetailDTO> pageUtil = new PageUtil(checked,stationStatusRequestDTO.getCurrent(),stationStatusRequestDTO.getSize(),percent,checked.size()) ;
            return pageUtil;
        }
        PageUtil<OnlineDeviceDetailDTO> pageUtil = new PageUtil(list,stationStatusRequestDTO.getCurrent(),stationStatusRequestDTO.getSize(),percent,checked.size()) ;
        return pageUtil;
    }

    @Override
    public List<QualifiedConfigDTO> getStationCheckConfig(QualifiedConfigDTO requestConfig) {
        List<QualifiedConfigDTO> result = new ArrayList<>();
        QueryWrapper wrapper = new QueryWrapper<>();
        if (StrUtil.isNotEmpty( requestConfig.getTm())){
            wrapper.eq("tm",requestConfig.getTm());
        }

        List<DeviceQualifiedEntity> deviceQualifiedEntities = deviceQualifiedDao.selectList(wrapper);
        if (CollUtil.isNotEmpty(deviceQualifiedEntities)){
            for (DeviceQualifiedEntity deviceQualifiedEntity : deviceQualifiedEntities) {
                QualifiedConfigDTO qualifiedConfigDTO = new QualifiedConfigDTO();
                BeanUtil.copyProperties(deviceQualifiedEntity,qualifiedConfigDTO);
                result.add(qualifiedConfigDTO);
            }
        }
        return result;
    }

    @Override
    public DeviceCountDTO getDeviceCount() {
        DeviceCountDTO res = new DeviceCountDTO();
        List<StStbprpBEntity> stStbprpBEntities = stStbprpBDao.selectList(new QueryWrapper<>());
        Map<String, List<StStbprpBEntity>> sttpMap = stStbprpBEntities.parallelStream().filter(stStbprpBEntity -> {
            return Objects.nonNull(stStbprpBEntity.getSttp());
        } ).collect(Collectors.groupingBy(StStbprpBEntity::getSttp));
        res.setZzCount(CollUtil.isNotEmpty(sttpMap.get("ZZ")) ? sttpMap.get("ZZ").size() : 0);
        res.setZqCount(CollUtil.isNotEmpty(sttpMap.get("ZQ")) ? sttpMap.get("ZQ").size() : 0);
        res.setDdCount(CollUtil.isNotEmpty(sttpMap.get("DD")) ? sttpMap.get("DD").size() : 0);
        res.setDpCount(CollUtil.isNotEmpty(sttpMap.get("DP")) ? sttpMap.get("DP").size() : 0);

        List<VideoInfoBase> videoInfoBases = videoInfoBaseDao.selectList(new QueryWrapper<>());
        Map<String, List<VideoInfoBase>> functionMap = videoInfoBases.parallelStream().filter(videoInfoBase -> {
            return Objects.nonNull(videoInfoBase.getFunctionType());
        } ).collect(Collectors.groupingBy(VideoInfoBase::getFunctionType));
        res.setFunctionMonitor(CollUtil.isNotEmpty(functionMap.get("1")) ? functionMap.get("1").size() : 0);
        res.setProtectMonitor(CollUtil.isNotEmpty(functionMap.get("2")) ? functionMap.get("2").size() : 0);
        return res;
    }

    @Override
    public StationFlowStatisticDTO getFlowRiver(StationFlowDTO stationFlowDTO) {
        //先去查询河道数据 id <= 31 则为河道  使用河道id 关联查询设备表中的 rvnm
        QueryWrapper<ReaBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.le("id", 31);
        List<ReaBase> reaBases = reaBaseDao.selectList(queryWrapper);
        Map<String, ReaBase> riverMap = new HashedMap<>();
        if (CollUtil.isNotEmpty(reaBases)){
            riverMap = reaBases.parallelStream().collect(Collectors.toMap(ReaBase::getId, Function.identity()));
        }
        List<DeviceStatusEntity> deviceStatusEntities = deviceStatusDao.selectList(new QueryWrapper<>());
        Map<String, DeviceStatusEntity> stcdMap = new HashedMap<>();
        if (CollUtil.isNotEmpty(deviceStatusEntities)){
            stcdMap = deviceStatusEntities.parallelStream().collect(Collectors.toMap(DeviceStatusEntity::getStcd, Function.identity()));
        }
        QueryWrapper<StStbprpBEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("sttp","ZZ");
        List<StStbprpBEntity> stStbprpBEntities = stStbprpBDao.selectList(wrapper);
        List<StationFlowDTO> stationFlowDTOS = new ArrayList<>();
        Integer normal = 0;
        Integer unNormal = 0;
        if (CollUtil.isNotEmpty(stStbprpBEntities)){
            for (StStbprpBEntity stStbprpBEntity : stStbprpBEntities) {
                StationFlowDTO stationFlowDTO1 = new StationFlowDTO();
                BeanUtil.copyProperties(stStbprpBEntity,stationFlowDTO1);
                ReaBase reaBase = riverMap.get(stationFlowDTO1.getRvnm());
                stationFlowDTO1.setRvnm(reaBase.getReaName());
                DeviceStatusEntity deviceStatusEntity = stcdMap.get(stationFlowDTO1.getStcd());
                if (deviceStatusEntity != null){
                    BigDecimal waterRate = deviceStatusEntity.getWaterRate() == null ? null : new BigDecimal(deviceStatusEntity.getWaterRate());
                    stationFlowDTO1.setWaterPosition(deviceStatusEntity.getWaterPosition() == null ? null : new BigDecimal( deviceStatusEntity.getWaterPosition()) );
                    stationFlowDTO1.setWaterRate(deviceStatusEntity.getWaterRate() == null ? null : new BigDecimal( deviceStatusEntity.getWaterRate()) );
                    if (waterRate!= null && waterRate.compareTo(new BigDecimal(0.1)) <= 0){
                        stationFlowDTO1.setFlowStatus(2);
                        ++ unNormal ;
                    }else {
                        stationFlowDTO1.setFlowStatus(1);
                        ++ normal ;
                    }
                }else {
                    stationFlowDTO1.setFlowStatus(1);
                    ++ normal ;
                }
                stationFlowDTOS.add(stationFlowDTO1);
            }
        }
        //对 字段进行排序
        if (StrUtil.isNotEmpty(stationFlowDTO.getField())  && stationFlowDTO.getSort() != null){
            stationFlowDTOS = getSortList(stationFlowDTOS,stationFlowDTO);
        }
        StationFlowStatisticDTO res = new StationFlowStatisticDTO();
        res.setNormalNum(normal);
        res.setUnNormalNum(unNormal);
        res.setStationFlowDTOS(stationFlowDTOS);
        return res;
    }

    /**
     * 排序
     * @param stationFlowDTOS
     * @param stationFlowDTO
     * @return
     */
    public List<StationFlowDTO> getSortList(List<StationFlowDTO> stationFlowDTOS,StationFlowDTO stationFlowDTO){
        List<StationFlowDTO> res = new ArrayList<>();
        List<StationFlowDTO> reverse = new ArrayList<>();
        if (stationFlowDTO.getField().equals("status")){
            res =  stationFlowDTOS.parallelStream().sorted(Comparator.comparing(StationFlowDTO::getFlowStatus)).collect(Collectors.toList());
            if (stationFlowDTO.getSort().equals(1)){
                return res;
            }else {
                res =  stationFlowDTOS.parallelStream().sorted(Comparator.comparing(StationFlowDTO::getFlowStatus)).collect(Collectors.toList());
                reverse = CollUtil.reverse(res);
                return reverse;
            }
        }
        else if (stationFlowDTO.getField().equals("waterRate")){
            res =  stationFlowDTOS.parallelStream().sorted(Comparator.comparing(stationFlowDTO1 -> {
                return  (stationFlowDTO1.getWaterRate() == null) ? new BigDecimal(0) : stationFlowDTO1.getWaterRate();
            } )).collect(Collectors.toList());
            if (stationFlowDTO.getSort().equals(1)){

                return res;
            }else {

                reverse = CollUtil.reverse(res);
                return reverse;
            }
        }
        else if (stationFlowDTO.getField().equals("waterPosition")){
            res =  stationFlowDTOS.parallelStream().sorted(Comparator.comparing(stationFlowDTO1 -> {
                return  (stationFlowDTO1.getWaterPosition() == null) ? new BigDecimal(0) : stationFlowDTO1.getWaterPosition();
            } )).collect(Collectors.toList());
            if (stationFlowDTO.getSort().equals(1)){
                return res;
            }else {
                reverse = CollUtil.reverse(res);
                return reverse;
            }
        }else {
            return stationFlowDTOS;
        }
    }


    public DeviceFeelCountDTO getYearStatistic(DeviceFeelRequestDTO deviceFeelRequestDTO){
        DeviceFeelCountDTO result = new DeviceFeelCountDTO();
        String format = DateUtil.format(deviceFeelRequestDTO.getDate(), "yyyy-MM-dd HH:mm:ss");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("date_type",1);
        wrapper.eq("date",format.substring(0,4));
        wrapper.eq("type",deviceFeelRequestDTO.getType());
        DeviceFellEntity deviceFellEntity = deviceFellDao.selectOne(wrapper);
        if (deviceFellEntity != null){
            String percent = deviceFellEntity.getPercent();
            String onValue = deviceFellEntity.getUpValue();
            String downValue = deviceFellEntity.getDownValue();

            result.setDateType("1");
            result.setUpValue(JSONObject.parseArray(onValue,Integer.class));
            result.setDownValue(JSONObject.parseArray(downValue,Integer.class));
            result.setPercent(JSONObject.parseArray(percent,BigDecimal.class));

        }

        return result ;
    }

    public DeviceFeelCountDTO getMonthStatistic(DeviceFeelRequestDTO deviceFeelRequestDTO){
        DeviceFeelCountDTO result = new DeviceFeelCountDTO();
        String format = DateUtil.format(deviceFeelRequestDTO.getDate(), "yyyy-MM-dd HH:mm:ss");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("date_type",2);
        wrapper.eq("date",format.substring(0,7));
        wrapper.eq("type",deviceFeelRequestDTO.getType());
        DeviceFellEntity deviceFellEntity = deviceFellDao.selectOne(wrapper);
        if (deviceFellEntity != null){
            String percent = deviceFellEntity.getPercent();
            String onValue = deviceFellEntity.getUpValue();
            String downValue = deviceFellEntity.getDownValue();

            result.setDateType("1");
            result.setUpValue(JSONObject.parseArray(onValue,Integer.class));
            result.setDownValue(JSONObject.parseArray(downValue,Integer.class));
            result.setPercent(JSONObject.parseArray(percent,BigDecimal.class));

        }

        return result ;
    }


    public DeviceFeelCountDTO getDayStatistic(DeviceFeelRequestDTO deviceFeelRequestDTO){
        DeviceFeelCountDTO result = new DeviceFeelCountDTO();
        String format = DateUtil.format(deviceFeelRequestDTO.getDate(), "yyyy-MM-dd HH:mm:ss");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("date_type",3);
        wrapper.eq("date",format.substring(0,10));
        wrapper.eq("type",deviceFeelRequestDTO.getType());
        DeviceFellEntity deviceFellEntity = deviceFellDao.selectOne(wrapper);
        if (deviceFellEntity != null){
            String percent = deviceFellEntity.getPercent();
            String onValue = deviceFellEntity.getUpValue();
            String downValue = deviceFellEntity.getDownValue();

            result.setDateType("1");
            result.setUpValue(JSONObject.parseArray(onValue,Integer.class));
            result.setDownValue(JSONObject.parseArray(downValue,Integer.class));
            result.setPercent(JSONObject.parseArray(percent,BigDecimal.class));

        }

        return result ;
    }



    public WaterPositionStatisticDTO  getWaterPosition() throws ParseException {
        List<StStbprpBEntity> stStbprpBEntities = stStbprpBDao.selectList(new QueryWrapper<>());
        Map<String, List<StStbprpBEntity>> sttpMap = stStbprpBEntities.parallelStream().filter(item-> StrUtil.isNotEmpty(item.getSttp())).collect(Collectors.groupingBy(StStbprpBEntity::getSttp));
        //水位
        List<StStbprpBEntity> zz = sttpMap.get("ZZ");
        Integer normalNum = 0;
        Integer moreForbidNum = 0;
        Integer mostPositionNum = 0;
        QueryWrapper<StSnConvertEntity> convertWrapper = new QueryWrapper();
        List<StSnConvertEntity> list1 = stSnConvertDao.selectList(convertWrapper);
        Map<String, String> stcdMap = list1.parallelStream().collect(Collectors.toMap(StSnConvertEntity::getStcd, StSnConvertEntity::getSn));
        for (StStbprpBEntity stbprpBEntity : zz) {
            String endZZ = DateUtil.format(new Date(), "yyyy/MM/dd HH:mm:ss");
            String startZZ = DateUtil.format(DateUtil.offsetDay(new Date(),-3), "yyyy/MM/dd HH:mm:ss");
            StStbprpBEntityDTO stStbprpBEntityDTO = new StStbprpBEntityDTO();
            stStbprpBEntityDTO.setSttp("ZZ");
            stStbprpBEntityDTO.setStartTime(startZZ);
            stStbprpBEntityDTO.setEndTime(endZZ);
            stStbprpBEntityDTO.setStcd(stbprpBEntity.getStcd());
            stStbprpBEntityDTO.setStcdMap(stcdMap);
            List<StWaterRateEntityDTO> stationDataListZZ = getStationDataList(stStbprpBEntityDTO);
            if (CollectionUtil.isNotEmpty(stationDataListZZ)){
                StWaterRateEntityDTO stWaterRateEntityDTO = stationDataListZZ.get(stationDataListZZ.size() - 1);
                //水位值
                BigDecimal water = new BigDecimal(0);
                if (!StrUtil.isEmpty(stWaterRateEntityDTO.getAddrv())){
                    water = new BigDecimal( stWaterRateEntityDTO.getAddrv()).divide(new BigDecimal(1000),3,BigDecimal.ROUND_HALF_UP);
                }
                //高程
                BigDecimal dtmel = stbprpBEntity.getDtmel() == null ? new BigDecimal(0) :stbprpBEntity.getDtmel();
                //警戒水位
                BigDecimal wrz = stbprpBEntity.getWrz() == null ? new BigDecimal(0) : stbprpBEntity.getWrz();
                //最高水位
                BigDecimal bhtz = stbprpBEntity.getBhtz() == null ? new BigDecimal(0) : stbprpBEntity.getBhtz() ;
                if (water.add(dtmel).compareTo(bhtz) >=0){
                    mostPositionNum ++;
                }
                if (water.add(dtmel).compareTo(wrz) >=0){
                    moreForbidNum ++;
                }else {
                    normalNum++;
                }
            }
        }

        //流量
        List<StStbprpBEntity> zq = sttpMap.get("ZQ");
        for (StStbprpBEntity stbprpBEntity : zq) {
            String endZQ = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
            String startZQ = DateUtil.format(DateUtil.offsetDay(new Date(),-3), "yyyy-MM-dd HH:mm:ss");
            StStbprpBEntityDTO stStbprpBEntityZQDTO = new StStbprpBEntityDTO();
            stStbprpBEntityZQDTO.setSttp("ZQ");
            stStbprpBEntityZQDTO.setStartTime(startZQ);
            stStbprpBEntityZQDTO.setEndTime(endZQ);
            stStbprpBEntityZQDTO.setStcd(stbprpBEntity.getStcd());
            List<StWaterRateEntityDTO> stationDataListZQ = getStationDataList(stStbprpBEntityZQDTO);
            if (CollectionUtil.isNotEmpty(stationDataListZQ)){
                StWaterRateEntityDTO stWaterRateEntityDTO = stationDataListZQ.get(stationDataListZQ.size() - 1);
                //水位值
                BigDecimal water = new BigDecimal(0);
                if (!StrUtil.isEmpty(stWaterRateEntityDTO.getAddrv())){
                    water = new BigDecimal(stWaterRateEntityDTO.getAddrv()).divide(new BigDecimal(1000),3,BigDecimal.ROUND_HALF_UP);
                }
                //高程
                BigDecimal dtmel = stbprpBEntity.getDtmel() == null ? new BigDecimal(0) :stbprpBEntity.getDtmel();
                //警戒水位
                BigDecimal wrz = stbprpBEntity.getWrz() == null ? new BigDecimal(0) : stbprpBEntity.getWrz();
                //最高水位
                BigDecimal bhtz = stbprpBEntity.getBhtz() == null ? new BigDecimal(0) : stbprpBEntity.getBhtz() ;
                if (water.add(dtmel).compareTo(bhtz) >=0){
                    mostPositionNum ++;
                }
                if (water.add(dtmel).compareTo(wrz) >=0){
                    moreForbidNum ++;
                }else {
                    normalNum++;
                }
            }
        }
        WaterPositionStatisticDTO waterPositionStatisticDTO = new WaterPositionStatisticDTO();
        waterPositionStatisticDTO.setNormalNum(normalNum);
        waterPositionStatisticDTO.setMoreForbidNum(moreForbidNum);
        waterPositionStatisticDTO.setMostPositionNum(mostPositionNum);
        int total = normalNum+moreForbidNum+mostPositionNum;
        waterPositionStatisticDTO.setNormalPercent( new BigDecimal(normalNum).divide(new BigDecimal(total),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)) );
        waterPositionStatisticDTO.setMoreForbidPercent(new BigDecimal(moreForbidNum).divide(new BigDecimal(total),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
        waterPositionStatisticDTO.setMostPositionPercent(new BigDecimal(mostPositionNum).divide(new BigDecimal(total),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
        return waterPositionStatisticDTO;
    }
}
