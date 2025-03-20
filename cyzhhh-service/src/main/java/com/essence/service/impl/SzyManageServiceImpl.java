package com.essence.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.*;
import com.essence.dao.entity.*;
import com.essence.interfaces.api.SzyManageService;
import com.essence.interfaces.dot.WaterOverLevelDto;
import com.essence.interfaces.dot.WaterOverLevelStatisticsDto;
import com.essence.service.listener.TimeSupplyModelExcelListener;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Author BINX
 * @Description TODO
 * @Date 2023/5/4 18:12
 */
@Service
public class SzyManageServiceImpl implements SzyManageService {

    @Autowired
    StSideGateDao stSideGateDao;
    @Autowired
    StWaterPortDao stWaterPortDao;
    @Autowired
    ReaBaseDao reaBaseDao;
    @Autowired
    WaterSupplyCaseDao waterSupplyCaseDao;
    @Autowired
    WaterPortTimeSupplyDao waterPortTimeSupplyDao;
    @Autowired
    WaterTransferDao waterTransferDao;
    @Autowired
    StPumpDataDao stPumpDataDao;
    @Autowired
    StWaterRateDao stWaterRateDao;
    @Autowired
    StStbprpBDao stStbprpBDao;
    @Autowired
    StSnConvertDao stSnConvertDao;
    @Autowired
    GateStationRelatedDao gateStationRelatedDao;
    @Override
    public List<WaterSupplyCaseDto> getWaterSupplyParam(String caseId) {
        if(caseId == null) {
            List<WaterSupplyCaseDto> resList = new ArrayList<>();
            QueryWrapper<StSideGateDto> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("sttp", "DP");
            queryWrapper.like("section_name_down", "%补水口%");
            List<StSideGateDto> stSideGateDtos = stSideGateDao.selectList(queryWrapper);
            for (StSideGateDto stSideGateDto : stSideGateDtos) {
                QueryWrapper<ReaBase> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("id", stSideGateDto.getRiverId());
                ReaBase reaBase = reaBaseDao.selectOne(queryWrapper1);
                WaterSupplyCaseDto res = new WaterSupplyCaseDto();
                res.setRiverName(reaBase.getReaName());
                res.setWaterPortName(stSideGateDto.getStnm());
                res.setSupply(BigDecimal.valueOf(Double.valueOf(stSideGateDto.getDesignFlow() == null ? "0" : stSideGateDto.getDesignFlow())));
                res.setLgtd(BigDecimal.valueOf(stSideGateDto.getLgtd()));
                res.setLttd(BigDecimal.valueOf(stSideGateDto.getLttd()));
                res.setSectionName(stSideGateDto.getSectionName());
                res.setSeriaName(stSideGateDto.getSeriaName());
                QueryWrapper<WaterPortTimeSupplyDto> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("water_port_name", stSideGateDto.getStnm());
                WaterPortTimeSupplyDto waterPortTimeSupplyDto = waterPortTimeSupplyDao.selectOne(queryWrapper2);
                if(null == waterPortTimeSupplyDto) {
                    res.setSupplyWay(0);
                } else {
                    res.setSupplyWay(1);
                    res.setTimeSupply(waterPortTimeSupplyDto.getTimesupply());
                }
                resList.add(res);
            }
            QueryWrapper<StWaterPortDto> queryWrapper3 = new QueryWrapper<>();
            queryWrapper3.eq("is_model_used", 1);
            List<StWaterPortDto> stWaterPortDtos = stWaterPortDao.selectList(queryWrapper3);
            for (StWaterPortDto stWaterPortDto : stWaterPortDtos) {
                WaterSupplyCaseDto res = new WaterSupplyCaseDto();
                res.setRiverName(stWaterPortDto.getRiverName());
                res.setWaterPortName(stWaterPortDto.getWaterPortName());
                res.setSupply(BigDecimal.valueOf(stWaterPortDto.getSupply()));
                res.setLgtd(BigDecimal.valueOf(stWaterPortDto.getLgtd()));
                res.setLttd(BigDecimal.valueOf(stWaterPortDto.getLttd()));
                res.setSectionName(stWaterPortDto.getSectionName());
                res.setSeriaName(stWaterPortDto.getSeriaName());
                QueryWrapper<WaterPortTimeSupplyDto> queryWrapper4 = new QueryWrapper<>();
                queryWrapper4.eq("water_port_name", stWaterPortDto.getWaterPortName());
                WaterPortTimeSupplyDto waterPortTimeSupplyDto = waterPortTimeSupplyDao.selectOne(queryWrapper4);
                if(null == waterPortTimeSupplyDto) {
                    res.setSupplyWay(0);
                } else {
                    res.setSupplyWay(1);
                    res.setTimeSupply(waterPortTimeSupplyDto.getTimesupply());
                }
                resList.add(res);
            }
            return resList;
        }
        QueryWrapper<WaterSupplyCaseDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("case_id", caseId);
        List<WaterSupplyCaseDto> waterSupplyCaseDtos = waterSupplyCaseDao.selectList(queryWrapper);
        waterSupplyCaseDtos.forEach(waterSupplyCaseDto -> {
            waterSupplyCaseDto.setSupply(waterSupplyCaseDto.getSupply().divide(new BigDecimal(10000)));
        });
        return waterSupplyCaseDtos;
    }

    @Transactional
    @Override
    public Object saveWaterSupplyParam(WaterSupplyParamDto waterSupplyParam) {
        List<WaterSupplyCaseDto> list = waterSupplyParam.getList();
        if(StrUtil.isEmpty(list.get(0).getCaseId())) {
            for (WaterSupplyCaseDto waterSupplyCaseParam : list) {
                waterSupplyCaseParam.setCaseId(waterSupplyParam.getCaseId());
                waterSupplyCaseParam.setSupply(waterSupplyCaseParam.getSupply().multiply(new BigDecimal(10000)));
            }
            return waterSupplyCaseDao.insertAll(list);
        } else {
            for (WaterSupplyCaseDto waterSupplyCaseParam : list) {
                waterSupplyCaseParam.setSupply(waterSupplyCaseParam.getSupply().multiply(new BigDecimal(10000)));
            }
            return waterSupplyCaseDao.updateAll(list);
        }
    }

    @SneakyThrows
    @Override
    public Object importTimeSupply(WaterSupplyCaseImportDto param) {
        InputStream inputStream = param.getFile().getInputStream();
        EasyExcel.read(inputStream, TimeSupplyModelEntity.class,new TimeSupplyModelExcelListener(param.getId(), param.getWaterPortName(), waterPortTimeSupplyDao, waterSupplyCaseDao)).sheet().doRead();
        return null;
    }

    @Override
    public void downloadTimeSupplyModel(HttpServletResponse response) {
        // 导出excel
        // 设置响应类型
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        // 设置编码
        response.setCharacterEncoding("utf-8");
        try {
            String fileName = URLEncoder.encode("补水口时间序列流量模板", "UTF-8").replaceAll("\\+", "%20");
            // 设置响应头
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            List<TimeSupplyModelEntity> all = new ArrayList<>();
            TimeSupplyModelEntity e = new TimeSupplyModelEntity();
            e.setTime(new Date());
            e.setSupply(new BigDecimal(0));
            all.add(e);
            // 生成excel
            EasyExcel.write(response.getOutputStream(), TimeSupplyModelEntity.class)
                    .autoCloseStream(Boolean.FALSE)
                    .sheet("补水口时间序列流量")
                    .doWrite(all);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<WaterTransferFlowDto> getWaterTransfer() {
        QueryWrapper<WaterTransferDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("recent_transfer", 0);
        List<WaterTransferDto> waterTransferDtos = waterTransferDao.selectList(queryWrapper);
        List<WaterTransferFlowDto> waterTransferFlowDtoList = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long todayZero = calendar.getTimeInMillis();

        waterTransferDtos.forEach(waterTransferDto -> {
            WaterTransferFlowDto waterTransferFlowDto = new WaterTransferFlowDto();
            BeanUtils.copyProperties(waterTransferDto, waterTransferFlowDto);
            waterTransferFlowDto.setSumFlow(BigDecimal.ZERO);
            QueryWrapper<StSideGateDto> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("sttp", "DP");
            queryWrapper1.eq("transfer_id", waterTransferDto.getId());
            StSideGateDto stSideGateDto = stSideGateDao.selectOne(queryWrapper1);
            QueryWrapper<StPumpDataDto> queryWrapper2 = new QueryWrapper<>();
            if(null != stSideGateDto && null != stSideGateDto.getStcd()) {
                queryWrapper2.eq("device_addr", stSideGateDto.getStcd());
                queryWrapper2.ge("date", new Date(todayZero));
                queryWrapper2.le("date", new Date());
                queryWrapper2.orderByAsc("date");
                List<StPumpDataDto> stPumpDataDtos = stPumpDataDao.selectList(queryWrapper2);
                BigDecimal sumFlow = new BigDecimal(0);
                if(stPumpDataDtos.size() > 0) {
                    sumFlow = BigDecimal.valueOf(Double.valueOf(stPumpDataDtos.get(stPumpDataDtos.size() - 1).getAddFlowRate())).subtract(BigDecimal.valueOf(Double.valueOf(stPumpDataDtos.get(0).getAddFlowRate())));
                }
                waterTransferFlowDto.setSumFlow(sumFlow);
            }
            if(null != stSideGateDto) {
                waterTransferFlowDto.setRemoteControl(stSideGateDto.getRemoteControl());
                waterTransferFlowDto.setPumpStnm(stSideGateDto.getStnm());
                waterTransferFlowDto.setPumpLgtd(String.valueOf(stSideGateDto.getLgtd()));
                waterTransferFlowDto.setPumpLttd(String.valueOf(stSideGateDto.getLttd()));
            }
            waterTransferFlowDtoList.add(waterTransferFlowDto);
        });
        return waterTransferFlowDtoList;
    }

    @Override
    public WaterOverLevelStatisticsDto getWaterLevel(int state) {
        Date date = new Date();
        DateTime start = DateUtil.beginOfDay(date);
        WaterOverLevelStatisticsDto res = new WaterOverLevelStatisticsDto();
        String period = "";
        String levelName = "";
        List<WaterOverLevelDto> resList = new ArrayList<>();
        List<WaterOverLevelDto> resOverList = new ArrayList<>();
        List<WaterOverLevelDto> resUnderList = new ArrayList<>();
        List<WaterOverLevelDto> resNormalList = new ArrayList<>();
        List<WaterOverLevelDto> resUnMatchList = new ArrayList<>();
        Calendar now = Calendar.getInstance();
        // 主汛期开始结束时间
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.set(Calendar.getInstance().get(Calendar.YEAR), 6, 19);
        endDate.set(Calendar.getInstance().get(Calendar.YEAR), 7, 16);
        if(now.after(startDate) && now.before(endDate)) {
            period = "主汛期";
            levelName = "设计中水位";
        } else {
            period = "非汛期";
            levelName = "设计高水位";
        }
        // 查询有水位限定数据的闸坝
        QueryWrapper<StSideGateDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("sttp", "DD", "SB");
        queryWrapper.isNotNull("high_water_level");
        queryWrapper.isNotNull("middle_water_level");
        queryWrapper.notIn("high_water_level", "/", "敞泄", "关闭");
        queryWrapper.or();
        queryWrapper.notIn("middle_water_level", "/", "敞泄", "关闭");
        List<StSideGateDto> stSideGateDtos = stSideGateDao.selectList(queryWrapper);
        // 根据日期判断是否超出 中/高 水位
        for (StSideGateDto stSideGateDto : stSideGateDtos) {
            WaterOverLevelDto resDto = new WaterOverLevelDto();
            resDto.setStnm(stSideGateDto.getStnm());
            resDto.setHighWaterLevel(stSideGateDto.getHighWaterLevel());
            resDto.setMiddleWaterLevel(stSideGateDto.getMiddleWaterLevel());
            BigDecimal actualTimeLevel = new BigDecimal(0);
            QueryWrapper<ReaBase> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("id", stSideGateDto.getRiverId());
            ReaBase reaBase = reaBaseDao.selectOne(queryWrapper1);
            resDto.setRiverName(reaBase.getReaName());
            QueryWrapper<GateStationRelatedDto> queryWrapper5 = new QueryWrapper<>();
            queryWrapper5.eq("gate_name", stSideGateDto.getStnm());
            queryWrapper5.in("sttp", "ZZ", "ZQ");
            queryWrapper5.eq("stream_loc", 0);
            List<GateStationRelatedDto> gateStationRelatedDtos = gateStationRelatedDao.selectList(queryWrapper5);
            // 基面高程
            BigDecimal dtmel = null;
            String stcd = "";
            String stationType = "";
            List<StWaterRateEntity> stWaterRateEntities = new ArrayList<>();
            boolean haveZZ = false;
            for (GateStationRelatedDto gateStationRelatedDto : gateStationRelatedDtos) {
                QueryWrapper<StStbprpBEntity> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("stcd", gateStationRelatedDto.getStcd());
                StStbprpBEntity stStbprpBEntity = stStbprpBDao.selectOne(queryWrapper2);
                if (stStbprpBEntity == null){
                    continue;
                }
                if("ZZ".equals(stStbprpBEntity.getSttp())) {
                    QueryWrapper<StSnConvertEntity> queryWrapper3 = new QueryWrapper<>();
                    queryWrapper3.eq("stcd", stStbprpBEntity.getStcd());
                    StSnConvertEntity stSnConvertEntity = stSnConvertDao.selectOne(queryWrapper3);
                    if(null != stSnConvertEntity) {
                        stcd = stSnConvertEntity.getSn();
                        String format1 = DateUtil.format(start, "yyyy/MM/dd HH:mm:ss");
                        String format2 = DateUtil.format(date, "yyyy/MM/dd HH:mm:ss");
                        QueryWrapper<StWaterRateEntity> queryWrapper4 = new QueryWrapper<>();
                        queryWrapper4.eq("did", stcd);
                        queryWrapper4.ge("ctime",format1);
                        queryWrapper4.le("ctime",format2);
                        queryWrapper4.orderByDesc("ctime");
                        stWaterRateEntities = stWaterRateDao.selectList(queryWrapper4);
                    }
                    if(CollectionUtil.isNotEmpty(stWaterRateEntities)) {
                        // 基面高程
                        dtmel = stStbprpBEntity.getDtmel() == null ? new BigDecimal(0) : stStbprpBEntity.getDtmel();
                        haveZZ = true;
                    }
                    stationType = "ZZ";
                } else if("ZQ".equals(stStbprpBEntity.getSttp()) && !haveZZ) {
                    stcd = stStbprpBEntity.getStcd().substring(2);
                    String format1 = DateUtil.format(start, "yyyy/MM/dd HH:mm:ss");
                    String format2 = DateUtil.format(date, "yyyy/MM/dd HH:mm:ss");
                    QueryWrapper<StWaterRateEntity> queryWrapper4 = new QueryWrapper<>();
                    queryWrapper4.eq("did", stcd);
                    queryWrapper4.ge("ctime",format1);
                    queryWrapper4.le("ctime",format2);
                    queryWrapper4.orderByDesc("ctime");
                    stWaterRateEntities = stWaterRateDao.selectList(queryWrapper4);
                    stationType = "ZQ";
                    // 基面高程
                    dtmel = stStbprpBEntity.getDtmel() == null ? new BigDecimal(0) : stStbprpBEntity.getDtmel();
                }
            }
            if(!"".equals(stcd)) {
                if("ZZ".equals(stationType) && haveZZ) {
                    actualTimeLevel = BigDecimal.valueOf(Double.valueOf(0 < stWaterRateEntities.size() && StrUtil.isNotEmpty(stWaterRateEntities.get(0).getAddrv())? stWaterRateEntities.get(0).getAddrv() : "0")).divide(new BigDecimal(1000));
                } else if("ZQ".equals(stationType) && !haveZZ){
                    actualTimeLevel = BigDecimal.valueOf(Double.valueOf(0 < stWaterRateEntities.size() && StrUtil.isNotEmpty(stWaterRateEntities.get(0).getMomentRiverPosition()) ? stWaterRateEntities.get(0).getMomentRiverPosition() : "0"));
                } else {
                    actualTimeLevel = BigDecimal.ZERO;
                }
                if(null != dtmel) {
                    actualTimeLevel = actualTimeLevel.add(dtmel);
                }
                if(0 == BigDecimal.ZERO.compareTo(actualTimeLevel)) {
                    resDto.setActualTimeLevel(null);
                } else {
                    resDto.setActualTimeLevel(actualTimeLevel.setScale(2, RoundingMode.HALF_UP));
                }
                // 2023-06-25 需求变更 高于 (中/高) 水位展示"高于", 等于 (中/高) 水位展示"正常", 低于 (中/高) 水位展示"低于", 其他情况: 展示空
                if(now.after(startDate) && now.before(endDate)) {
                    if("敞泄".equals(stSideGateDto.getMiddleWaterLevel()) || "关闭".equals(stSideGateDto.getMiddleWaterLevel()) || "/".equals(stSideGateDto.getMiddleWaterLevel())) {
                        resDto.setIsOverLevel("");
                        resNormalList.add(resDto);
                    } else if(null != resDto.getActualTimeLevel() && actualTimeLevel.compareTo(BigDecimal.valueOf(Double.valueOf(stSideGateDto.getMiddleWaterLevel()))) > 0) {
                        resDto.setIsOverLevel("高于");
                        resOverList.add(resDto);
                    } else if(null != resDto.getActualTimeLevel() && actualTimeLevel.compareTo(BigDecimal.valueOf(Double.valueOf(stSideGateDto.getMiddleWaterLevel()))) == 0) {
                        // 正常水位的
                        resDto.setIsOverLevel("正常");
                        resNormalList.add(resDto);
                    } else if(null != resDto.getActualTimeLevel() && actualTimeLevel.compareTo(BigDecimal.valueOf(Double.valueOf(stSideGateDto.getMiddleWaterLevel()))) < 0) {
                        resDto.setIsOverLevel("低于");
                        resUnderList.add(resDto);
                    }
                } else {
                    if("敞泄".equals(stSideGateDto.getHighWaterLevel()) || "关闭".equals(stSideGateDto.getHighWaterLevel()) || "/".equals(stSideGateDto.getHighWaterLevel())) {
                        resDto.setIsOverLevel("");
                        resNormalList.add(resDto);
                    } else if(null != resDto.getActualTimeLevel() && actualTimeLevel.compareTo( BigDecimal.valueOf(Double.valueOf(stSideGateDto.getHighWaterLevel()))) > 0) {
                        resDto.setIsOverLevel("高于");
                        resOverList.add(resDto);
                    } else if(null != resDto.getActualTimeLevel() && actualTimeLevel.compareTo( BigDecimal.valueOf(Double.valueOf(stSideGateDto.getHighWaterLevel()))) == 0) {
                        // 正常水位的
                        resDto.setIsOverLevel("正常");
                        resNormalList.add(resDto);
                    } else if(null != resDto.getActualTimeLevel() && actualTimeLevel.compareTo( BigDecimal.valueOf(Double.valueOf(stSideGateDto.getHighWaterLevel()))) < 0) {
                        resDto.setIsOverLevel("低于");
                        resUnderList.add(resDto);
                    }
                }
            }
            if(null == resDto.getActualTimeLevel()) {
                resUnMatchList.add(resDto);
            }
        }
        // 状态筛选 0 - 全部 ; 1 - 正常; 2 -高于; 3 - 低于; 4 - 无数据
        switch (state) {
            case 0:
                resList.addAll(resOverList);
                resList.addAll(resUnderList);
                resList.addAll(resNormalList);
                resList.addAll(resUnMatchList);
                break;
            case 1:
                resList.addAll(resNormalList);
                break;
            case 2:
                resList.addAll(resOverList);
                break;
            case 3:
                resList.addAll(resUnderList);
                break;
            case 4:
                resList.addAll(resUnMatchList);
                break;
            default:
        }
        res.setPeriod(period);
        res.setLevelName(levelName);
        res.setOverLevelCount(resOverList.size());
        res.setUnderLevelCount(resUnderList.size());
        res.setTotal(stSideGateDtos.size());
        res.setList(resList);

        return res;
    }

}
