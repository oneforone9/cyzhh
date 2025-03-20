
package com.essence.job.executor.device;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.dto.DeviceStatusDTO;
import com.essence.common.dto.StStbprpBEntityDTO;
import com.essence.common.dto.StWaterRateEntityDTO;
import com.essence.dao.DeviceDataStatisticDao;
import com.essence.dao.StSnConvertDao;
import com.essence.dao.StStbprpBDao;
import com.essence.entity.DeviceDataStatisticEntity;
import com.essence.entity.StSnConvertEntity;
import com.essence.entity.StStbprpBEntity;
import com.essence.service.StationService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Component
public class DeviceStatusRangTask {
    @Resource
    private DeviceDataStatisticDao deviceDataStatisticDao;
    @Resource
    private StationService stationService;

    @Resource
    private StSnConvertDao stSnConvertDao;

    @Autowired
    private StStbprpBDao stStbprpBDao;

    @XxlJob("DeviceStatusRangTask")
    public void demoJobHandler() throws Exception {
        getDeviceRunStatus();
        System.out.println("设备统计 DeviceStatusRangTask 采集完成"+new Date());
    }


    /**
     * 定时更新设备在线状态
     */
    public void getDeviceRunStatus() throws ParseException {
        QueryWrapper<StStbprpBEntity> stationWrapper = new QueryWrapper();

        stationWrapper.eq("sttp","ZZ").or().eq("sttp","ZQ");
        List<StStbprpBEntity> stStbprpBEntities = stStbprpBDao.selectList(stationWrapper);
        Map<String, List<StStbprpBEntity>> sttpMap = stStbprpBEntities.parallelStream().filter(item-> StrUtil.isNotEmpty(item.getSttp())).collect(Collectors.groupingBy(StStbprpBEntity::getSttp));
        DeviceStatusDTO deviceStatusDTO = new DeviceStatusDTO();
        //水位
        List<StStbprpBEntity> zz = sttpMap.get("ZZ");
        Integer onlineZZ = 0 ;
        QueryWrapper<StSnConvertEntity> convertWrapper = new QueryWrapper();
        List<StSnConvertEntity> list1 = stSnConvertDao.selectList(convertWrapper);
        Map<String, String> stcdMap = list1.parallelStream().collect(Collectors.toMap(StSnConvertEntity::getStcd, StSnConvertEntity::getSn));
        for (StStbprpBEntity stbprpBEntity : zz) {

            String endZZ = DateUtil.format(new Date(), "yyyy/MM/dd HH:mm:ss");
            String startZZ = DateUtil.format(DateUtil.offsetDay(new Date(),-1), "yyyy/MM/dd HH:mm:ss");
            StStbprpBEntityDTO stStbprpBEntityDTO = new StStbprpBEntityDTO();
            stStbprpBEntityDTO.setSttp("ZZ");
            stStbprpBEntityDTO.setStartTime(startZZ);
            stStbprpBEntityDTO.setEndTime(endZZ);
            if (stbprpBEntity.getStcd().length() >=3 ){
                stStbprpBEntityDTO.setStcd(stbprpBEntity.getStcd());
                stStbprpBEntityDTO.setStcdMap(stcdMap);
                List<StWaterRateEntityDTO> stationDataListZZ = stationService.getStationDataList(stStbprpBEntityDTO);
                if (CollectionUtil.isNotEmpty(stationDataListZZ)){
                    ++ onlineZZ;
                }
            }
        }
        DeviceDataStatisticEntity water = new DeviceDataStatisticEntity();
        water.setOnline(onlineZZ);
        water.setType("ZZ");
        water.setTotal(zz.size());

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("type",water.getType());
        DeviceDataStatisticEntity deviceDataStatisticEntity = deviceDataStatisticDao.selectOne(wrapper);
        if (deviceDataStatisticEntity!= null){
            deviceDataStatisticDao.update(water,wrapper);
        }else {
            deviceDataStatisticDao.insert(water);
        }


        //流量
        Integer onlineZQ = 0 ;
        List<StStbprpBEntity> zq = sttpMap.get("ZQ");
        for (StStbprpBEntity stbprpBEntity : zq) {

            String endZQ = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
            String startZQ = DateUtil.format(DateUtil.offsetDay(new Date(),-1), "yyyy-MM-dd HH:mm:ss");
            StStbprpBEntityDTO stStbprpBEntityZQDTO = new StStbprpBEntityDTO();
            stStbprpBEntityZQDTO.setSttp("ZQ");
            stStbprpBEntityZQDTO.setStartTime(startZQ);
            stStbprpBEntityZQDTO.setEndTime(endZQ);
            if (stbprpBEntity.getStcd().length() >=3 ){
                stStbprpBEntityZQDTO.setStcd(stbprpBEntity.getStcd());
                List<StWaterRateEntityDTO> stationDataListZQ = stationService.getStationDataList(stStbprpBEntityZQDTO);
                if (CollectionUtil.isNotEmpty(stationDataListZQ)){
                    ++ onlineZQ;
                }
            }
        }
        DeviceDataStatisticEntity flowRate = new DeviceDataStatisticEntity();
        flowRate.setOnline(onlineZQ);
        flowRate.setType("ZQ");
        flowRate.setTotal(zq.size());
        QueryWrapper wrapperFlowRate = new QueryWrapper();
        wrapperFlowRate.eq("type",flowRate.getType());
        DeviceDataStatisticEntity deviceFlowRateEntity = deviceDataStatisticDao.selectOne(wrapperFlowRate);
        if (deviceFlowRateEntity!= null){
            deviceDataStatisticDao.update(flowRate,wrapperFlowRate);
        }else {
            deviceDataStatisticDao.insert(flowRate);
        }

    }

}

