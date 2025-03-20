package com.essence.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.dto.StStbprpBEntityDTO;
import com.essence.common.dto.WaterTransitDto;
import com.essence.dao.ReaBaseDao;
import com.essence.dao.StStbprpBDao;
import com.essence.dao.StWaterRateDao;
import com.essence.dao.entity.ReaBase;
import com.essence.dao.entity.StStbprpBEntity;
import com.essence.dao.entity.StWaterRateEntity;
import com.essence.interfaces.api.TransitWaterService;
import lombok.SneakyThrows;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 巡查一览业务层
 *
 * @Author BINX
 * @Description TODO
 * @Date 2023/4/24 10:53
 */
@Service
public class TransitWaterServiceImpl implements TransitWaterService {

    @Autowired
    ReaBaseDao reaBaseDao;

    @Autowired
    StStbprpBDao stStbprpBDao;

    @Autowired
    StWaterRateDao stWaterRateDao;

    @SneakyThrows
    @Override
    public WaterTransitDto getWaterTransit(StStbprpBEntityDTO stStbprpBEntityDTO) {
        WaterTransitDto res = new WaterTransitDto();
        // 获取河道
        QueryWrapper<ReaBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.le("id", 31);
        List<ReaBase> reaBases = reaBaseDao.selectList(queryWrapper);
        List<ReaBase> resList = new ArrayList<>();
        Map<String, ReaBase> riverMap = new HashedMap<>();
        if (CollUtil.isNotEmpty(reaBases)) {
            riverMap = reaBases.parallelStream().collect(Collectors.toMap(ReaBase::getId, Function.identity()));
        }
        // 获取河道站位监测点类型
        List<StStbprpBEntity> stStbprpBEntities = stStbprpBDao.selectList(new QueryWrapper<>());
        List<StStbprpBEntityDTO> list = new ArrayList<>();
        for (StStbprpBEntity stStbprpBEntity : stStbprpBEntities) {
            StStbprpBEntityDTO st = new StStbprpBEntityDTO();
            BeanUtil.copyProperties(stStbprpBEntity, st);
            if (StrUtil.isNotEmpty(stStbprpBEntity.getRvnm())) {
                ReaBase reaBase = riverMap.get(stStbprpBEntity.getRvnm());
                st.setRvnm(reaBase.getReaName());
                st.setUnitId(reaBase.getUnitId());
                st.setUnitName(reaBase.getUnitName());
                if (st.getRequireProvide() && stStbprpBEntity.getSttp().equals("ZQ")){
                    setDatProvideWater(st);
                }
                list.add(st);
            }
        }
        BigDecimal flowIn = new BigDecimal(0);
        BigDecimal flowOut = new BigDecimal(0);
        BigDecimal flowTransit = new BigDecimal(0);
        List<String> dids = stStbprpBEntities.stream().filter(x -> "ZQ".equals(x.getSttp())).map(x -> x.getStcd().substring(2)).collect(Collectors.toList());
        QueryWrapper<StWaterRateEntity> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.in("did", dids);
        queryWrapper1.ge("ctime", stStbprpBEntityDTO.getStartTime());
        queryWrapper1.le("ctime", stStbprpBEntityDTO.getEndTime());
        List<StWaterRateEntity> stWaterRateEntities = stWaterRateDao.selectList(queryWrapper1);
        Map<String, List<StWaterRateEntity>> stWaterMap = stWaterRateEntities.parallelStream().filter(stWaterRateEntity -> {return StrUtil.isNotEmpty(stWaterRateEntity.getDid());}).collect(Collectors.groupingBy(StWaterRateEntity::getDid));
        for (StStbprpBEntityDTO stRes : list) {
            if(StrUtil.isNotEmpty(stRes.getFlowType()) && stRes.getFlowType().contains("出境")) {
                stStbprpBEntityDTO.setStcd(stRes.getStcd());
                BigDecimal stationHourFlow = getStationHourFlow(stStbprpBEntityDTO, stWaterMap);
                flowOut = flowOut.add(stationHourFlow);
            } else if(StrUtil.isNotEmpty(stRes.getFlowType()) && stRes.getFlowType().contains("入境")) {
                stStbprpBEntityDTO.setStcd(stRes.getStcd());
                BigDecimal stationHourFlow = getStationHourFlow(stStbprpBEntityDTO, stWaterMap);
                flowIn = flowIn.add(stationHourFlow);
            }
        }
        // 过境流量入境减出境
        flowTransit = flowIn.subtract(flowOut);
        // 获取流量数据 单位 万m³/d
        res.setFlowIn(flowIn.divide(new BigDecimal(10000), BigDecimal.ROUND_HALF_UP));
        res.setFlowOut(flowOut.divide(new BigDecimal(10000), BigDecimal.ROUND_HALF_UP));
        res.setFlowTransit(flowTransit.divide(new BigDecimal(10000), BigDecimal.ROUND_HALF_UP));
        return res;
    }

    public BigDecimal getStationHourFlow(StStbprpBEntityDTO stStbprpBEntityDTO, Map<String, List<StWaterRateEntity>> stWaterMap) {
        SimpleDateFormat startTimeFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        SimpleDateFormat endTimeFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        Date now = new Date();
        if(StrUtil.isEmpty(stStbprpBEntityDTO.getStartTime()) && StrUtil.isEmpty(stStbprpBEntityDTO.getEndTime())) {
            stStbprpBEntityDTO.setStartTime(startTimeFormat.format(now));
            stStbprpBEntityDTO.setEndTime(endTimeFormat.format(now));
        }
        BigDecimal stationFlow = new BigDecimal(0);
        List<StWaterRateEntity> stWaterRateEntities = stWaterMap.get(stStbprpBEntityDTO.getStcd().substring(2));
        if (!CollectionUtils.isEmpty(stWaterRateEntities)) {
            // 以小时为分组 取每个小时数据求和
            Map<String, List<StWaterRateEntity>> ctTimeMapList = stWaterRateEntities.parallelStream().collect(Collectors.groupingBy(stWaterRateEntity -> {
                return stWaterRateEntity.getCtime().substring(0, 13);
            }));
            BigDecimal momentSum = new BigDecimal(0);

            List<String> dates = new ArrayList<>();
            for (String ct : ctTimeMapList.keySet()) {
                List<StWaterRateEntity> stWaterRateEntities1 = ctTimeMapList.get(ct);
                // 每个小时取第一条数据
                StWaterRateEntity stWaterRateEntity = stWaterRateEntities1.get(0);
                if (StrUtil.isNotEmpty(stWaterRateEntity.getCtime())) {
                    BigDecimal value = new BigDecimal(null != stWaterRateEntity.getMomentRate() ? stWaterRateEntity.getMomentRate() : "0");
                    momentSum = momentSum.add(value.multiply(new BigDecimal(60 * 60)));
                }
                dates.add(ct);
            }
            stationFlow = momentSum;
        }

        return stationFlow;
    }

    public void setDatProvideWater(StStbprpBEntityDTO stStbprpBEntityDTO) throws ParseException {
        BigDecimal avg = this.dealDayProvide(stStbprpBEntityDTO);
        Date date = new Date();
        long l = DateUtil.betweenMs(DateUtil.beginOfDay(date), date);
        BigDecimal provide =  new BigDecimal(l).multiply(avg == null ? new BigDecimal(0) : avg);
        stStbprpBEntityDTO.setAvgRate(avg);
        stStbprpBEntityDTO.setDayProvideWater(provide);

    }

    public BigDecimal dealDayProvide(StStbprpBEntityDTO stStbprpBEntityDTO) throws ParseException {
        QueryWrapper flowWrapper = new QueryWrapper();
        flowWrapper.eq("stcd",stStbprpBEntityDTO.getStcd());
        StStbprpBEntity stStbprpBEntities = stStbprpBDao.selectOne(flowWrapper);

        QueryWrapper<StWaterRateEntity> wrapper = new QueryWrapper<>();
        if (stStbprpBEntityDTO.getStcd().length() <= 3) {
            return null;
        }
        Date date = new Date();
        stStbprpBEntityDTO.setEndTime(DateUtil.format(date,"yyyy-MM-dd HH:mm:ss"));
        stStbprpBEntityDTO.setStartTime( DateUtil.format(DateUtil.beginOfDay(date) ,"yyyy-MM-dd HH:mm:ss"));
        //流量站检测数据返回的数据id 缺失前两位 00 台账表中的台账ID 需要截取掉前两位进行查询
        wrapper.eq("did", stStbprpBEntityDTO.getStcd().substring(2));
        wrapper.le("ctime", stStbprpBEntityDTO.getEndTime());
        wrapper.ge("ctime", stStbprpBEntityDTO.getStartTime());
        List<StWaterRateEntity> stWaterRateEntities = stWaterRateDao.selectList(wrapper);
        if (!CollectionUtils.isEmpty(stWaterRateEntities)) {
            // 以小时为分组 取最后一个小时的数据
            Map<String, List<StWaterRateEntity>> ctTimeMapList = stWaterRateEntities.parallelStream().collect(Collectors.groupingBy(stWaterRateEntity -> {
                return stWaterRateEntity.getCtime().substring(0, 13);
            }));
            BigDecimal momentRate = new BigDecimal(0);
            for (String ct : ctTimeMapList.keySet()) {
                StWaterRateEntity stWaterRateEntity = ctTimeMapList.get(ct).get(0);
                if (StrUtil.isNotEmpty(stWaterRateEntity.getCtime())) {
                    BigDecimal value = new BigDecimal(stWaterRateEntity.getMomentRate()==null?"0":stWaterRateEntity.getMomentRate());
                    momentRate = momentRate.add(value);
                }
            }
            int size = ctTimeMapList.keySet().size();
            //平均流量
            BigDecimal divide = momentRate.divide(new BigDecimal(size), 2, RoundingMode.HALF_UP);
            return divide;
        }
        return null;
    }
}
