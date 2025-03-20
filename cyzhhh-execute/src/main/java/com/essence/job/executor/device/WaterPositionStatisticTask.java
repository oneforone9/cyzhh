package com.essence.job.executor.device;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.dto.StStbprpBEntityDTO;
import com.essence.common.dto.StWaterRateEntityDTO;
import com.essence.dao.StSnConvertDao;
import com.essence.dao.StStbprpBDao;
import com.essence.dao.StWaterRateDao;
import com.essence.dao.WaterPositionStatisticDao;
import com.essence.entity.StSnConvertEntity;
import com.essence.entity.StStbprpBEntity;
import com.essence.entity.StWaterRateEntity;
import com.essence.entity.WaterPositionStatisticEntity;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class WaterPositionStatisticTask {
    @Resource
    private WaterPositionStatisticDao waterPositionStatisticDao;

    @Resource
    private StSnConvertDao stSnConvertDao;

    @Autowired
    private StStbprpBDao stStbprpBDao;
    @Resource
    private StWaterRateDao stWaterRateDao;



    /**
     * 定时更新 /station/station/status  接口数据
     */
    @XxlJob("WaterPositionStatisticTask")
    public void demoJobHandler() throws Exception {
        getWaterPosition();
        System.out.println("水位统计 健康码采集完成"+new Date());
    }


    public void getWaterPosition(){
        QueryWrapper<StStbprpBEntity> stationWrapper = new QueryWrapper();

        stationWrapper.eq("sttp","ZZ").or().eq("sttp","ZQ");
        List<StStbprpBEntity> stStbprpBEntities = stStbprpBDao.selectList(stationWrapper);
        Map<String, List<StStbprpBEntity>> sttpMap = stStbprpBEntities.parallelStream().filter(item-> StrUtil.isNotEmpty(item.getSttp())).collect(Collectors.groupingBy(StStbprpBEntity::getSttp));
        //水位
        List<StStbprpBEntity> zz = sttpMap.get("ZZ");
        Integer normalNum = 0;
        Integer moreForbidNum = 0;
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

                if (stWaterRateEntityDTO.getAddrv() == null || stbprpBEntity.getWrz() == null){
                    normalNum++;
                    continue;
                }
                if (water.add(dtmel).compareTo(wrz) >=0){
                    moreForbidNum ++;
                }else {
                    normalNum++;
                }
            }else {
                normalNum++;
            }
        }

        //流量
        List<StStbprpBEntity> zq = sttpMap.get("ZQ");
        for (StStbprpBEntity stbprpBEntity : zq) {
            String endZQ = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
            String startZQ = DateUtil.format(DateUtil.offsetDay(new Date(),-1), "yyyy-MM-dd HH:mm:ss");
            StStbprpBEntityDTO stStbprpBEntityZQDTO = new StStbprpBEntityDTO();
            stStbprpBEntityZQDTO.setSttp("ZQ");
            stStbprpBEntityZQDTO.setStartTime(startZQ);
            stStbprpBEntityZQDTO.setEndTime(endZQ);
            stStbprpBEntityZQDTO.setStcd(stbprpBEntity.getStcd());
            if (stbprpBEntity.getStcd().length() <=3){
                normalNum++;
                continue;
            }
            List<StWaterRateEntityDTO> stationDataListZQ = getStationDataList(stStbprpBEntityZQDTO);
            if (CollectionUtil.isNotEmpty(stationDataListZQ)){
                StWaterRateEntityDTO stWaterRateEntityDTO = stationDataListZQ.get(stationDataListZQ.size() - 1);
                //水位值
                BigDecimal water = new BigDecimal(0);
                if (!StrUtil.isEmpty(stWaterRateEntityDTO.getAddrv())){
                    water = new BigDecimal(stWaterRateEntityDTO.getMomentRiverPosition());
                }
                //高程
                BigDecimal dtmel = stbprpBEntity.getDtmel() == null ? new BigDecimal(0) :stbprpBEntity.getDtmel();
                //警戒水位
                BigDecimal wrz = stbprpBEntity.getWrz() == null ? new BigDecimal(0) : stbprpBEntity.getWrz();

                if (stbprpBEntity.getWrz() == null || stWaterRateEntityDTO.getMomentRiverPosition() == null){
                    normalNum++;
                    continue;
                }
                if (water.add(dtmel).compareTo(wrz) >=0){
                    moreForbidNum ++;
                }else {
                    normalNum++;
                }
            }else {
                normalNum++;
            }
        }
        waterPositionStatisticDao.delete(new QueryWrapper<>());
        WaterPositionStatisticEntity waterPositionStatisticDTO = new WaterPositionStatisticEntity();
        waterPositionStatisticDTO.setNormalNum(normalNum);
        waterPositionStatisticDTO.setMoreForbidNum(moreForbidNum);
//        waterPositionStatisticDTO.setMostPositionNum(mostPositionNum);
        int total = normalNum+moreForbidNum;
        waterPositionStatisticDTO.setNormalPercent( new BigDecimal(normalNum).divide(new BigDecimal(total),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)) );
        waterPositionStatisticDTO.setMoreForbidPercent(new BigDecimal(moreForbidNum).divide(new BigDecimal(total),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
//        waterPositionStatisticDTO.setMostPositionPercent(new BigDecimal(mostPositionNum).divide(new BigDecimal(total),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
        waterPositionStatisticDao.insert(waterPositionStatisticDTO);
    }

    public List<StWaterRateEntityDTO> getStationDataList(StStbprpBEntityDTO stStbprpBEntityDTO) {
        List<StWaterRateEntityDTO> list = new ArrayList<>();
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
                    for (StWaterRateEntity stWaterRateEntity : stWaterRateEntities) {
                        StWaterRateEntityDTO stStbprpBEntityDTO1 = new StWaterRateEntityDTO();
                        BeanUtils.copyProperties(stWaterRateEntity, stStbprpBEntityDTO1);
                        if (StrUtil.isNotEmpty(stWaterRateEntity.getCtime())){
                            String ctime = stWaterRateEntity.getCtime();
                            DateTime dateTime = DateUtil.parseDate(ctime);
                            String format = DateUtil.format(dateTime, "yyyy-MM-dd HH:mm:ss");
                            stStbprpBEntityDTO1.setCtime(format);
                            stStbprpBEntityDTO1.setMDh(format.substring(5,16));
                        }
                        list.add(stStbprpBEntityDTO1);
                    }
                }
            }

        }else {
            QueryWrapper<StWaterRateEntity>  wrapper= new QueryWrapper<>();
            wrapper.eq("did",stStbprpBEntityDTO.getStcd().substring(2));
            if (StrUtil.isNotEmpty(stStbprpBEntityDTO.getEndTime()) ){
                wrapper.le("ctime",stStbprpBEntityDTO.getEndTime());
            }
            if (StrUtil.isNotEmpty(stStbprpBEntityDTO.getStartTime()) ){
                wrapper.ge("ctime",stStbprpBEntityDTO.getStartTime());
            }
            List<StWaterRateEntity> stWaterRateEntities = stWaterRateDao.selectList(wrapper);
            if (!CollectionUtils.isEmpty(stWaterRateEntities)){
                for (StWaterRateEntity stWaterRateEntity : stWaterRateEntities) {
                    StWaterRateEntityDTO stWaterRateEntityDTO = new StWaterRateEntityDTO();
                    BeanUtils.copyProperties(stWaterRateEntity, stWaterRateEntityDTO);
                    if (StrUtil.isNotEmpty(stWaterRateEntity.getCtime())){
                        stWaterRateEntityDTO.setMDh(stWaterRateEntity.getCtime().substring(5,16));
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
}
