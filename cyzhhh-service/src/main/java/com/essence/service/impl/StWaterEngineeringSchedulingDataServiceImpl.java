package com.essence.service.impl;


import com.essence.common.utils.UuidUtil;
import com.essence.dao.StWaterEngineeringSchedulingDataDao;
import com.essence.dao.entity.StWaterEngineeringSchedulingCodeDto;
import com.essence.dao.entity.StWaterEngineeringSchedulingDataDto;
import com.essence.dao.entity.water.StWaterEngineeringSchedulingDto;
import com.essence.interfaces.api.StWaterEngineeringSchedulingCodeService;
import com.essence.interfaces.api.StWaterEngineeringSchedulingDataService;
import com.essence.interfaces.model.StWaterEngineeringSchedulingDataEsr;
import com.essence.interfaces.model.StWaterEngineeringSchedulingDataEsu;
import com.essence.interfaces.param.StWaterEngineeringSchedulingDataEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStWaterEngineeringSchedulingDataEtoT;
import com.essence.service.converter.ConverterStWaterEngineeringSchedulingDataTtoR;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 防汛调度方案指令下发记录(StWaterEngineeringSchedulingData)业务层
 *
 * @author majunjie
 * @since 2023-05-14 18:15:39
 */
@Service
public class StWaterEngineeringSchedulingDataServiceImpl extends BaseApiImpl<StWaterEngineeringSchedulingDataEsu, StWaterEngineeringSchedulingDataEsp, StWaterEngineeringSchedulingDataEsr, StWaterEngineeringSchedulingDataDto> implements StWaterEngineeringSchedulingDataService {

    @Autowired
    private StWaterEngineeringSchedulingDataDao stWaterEngineeringSchedulingDataDao;
    @Autowired
    private ConverterStWaterEngineeringSchedulingDataEtoT converterStWaterEngineeringSchedulingDataEtoT;
    @Autowired
    private ConverterStWaterEngineeringSchedulingDataTtoR converterStWaterEngineeringSchedulingDataTtoR;
@Autowired
private StWaterEngineeringSchedulingCodeService stWaterEngineeringSchedulingCodeService;
    public StWaterEngineeringSchedulingDataServiceImpl(StWaterEngineeringSchedulingDataDao stWaterEngineeringSchedulingDataDao, ConverterStWaterEngineeringSchedulingDataEtoT converterStWaterEngineeringSchedulingDataEtoT, ConverterStWaterEngineeringSchedulingDataTtoR converterStWaterEngineeringSchedulingDataTtoR) {
        super(stWaterEngineeringSchedulingDataDao, converterStWaterEngineeringSchedulingDataEtoT, converterStWaterEngineeringSchedulingDataTtoR);
    }
    @Override
    //局指下发
    public void insertFloodDispatchX(StWaterEngineeringSchedulingDto stWaterEngineeringSchedulingDto) {
        //生成新的调度指令
        StWaterEngineeringSchedulingCodeDto stWaterEngineeringSchedulingCodeDto=new StWaterEngineeringSchedulingCodeDto();
        BeanUtils.copyProperties(stWaterEngineeringSchedulingDto,stWaterEngineeringSchedulingCodeDto );
        stWaterEngineeringSchedulingCodeDto.setId(UuidUtil.get32UUIDStr());
        stWaterEngineeringSchedulingCodeDto.setStId(stWaterEngineeringSchedulingDto.getId());
        stWaterEngineeringSchedulingCodeService.saveStWaterEngineeringSchedulingCode(stWaterEngineeringSchedulingCodeDto);
        //生成记录
        StWaterEngineeringSchedulingDataDto stWaterEngineeringSchedulingDataDto = new StWaterEngineeringSchedulingDataDto();
        stWaterEngineeringSchedulingDataDto.setCodeId(stWaterEngineeringSchedulingCodeDto.getId());
        stWaterEngineeringSchedulingDataDto.setDid(UuidUtil.get32UUIDStr());
        stWaterEngineeringSchedulingDataDto.setRank(stWaterEngineeringSchedulingDto.getRank());
        stWaterEngineeringSchedulingDataDto.setStId(stWaterEngineeringSchedulingDto.getId());
        stWaterEngineeringSchedulingDataDto.setZbmc(stWaterEngineeringSchedulingDto.getZbmc());
        stWaterEngineeringSchedulingDataDto.setForecastState(1);
        stWaterEngineeringSchedulingDataDto.setUnitId(stWaterEngineeringSchedulingDto.getUnitId());
        stWaterEngineeringSchedulingDataDto.setTime(new Date());
        if (stWaterEngineeringSchedulingDto.getRank().equals("0")) {
            String unit = stWaterEngineeringSchedulingDto.getUnitId().equals("1") ? "河道一所" : "河道二所";
            stWaterEngineeringSchedulingDataDto.setReason("局指挥中心下发" + stWaterEngineeringSchedulingDataDto.getZbmc() + "调度指令至" + unit);
        } else {
            stWaterEngineeringSchedulingDataDto.setXcfzr(stWaterEngineeringSchedulingDto.getXcfzr());
            stWaterEngineeringSchedulingDataDto.setReason("局指挥中心下发" + stWaterEngineeringSchedulingDataDto.getZbmc() + "调度指令至" + stWaterEngineeringSchedulingDto.getXcfzr());
        }
        stWaterEngineeringSchedulingDataDao.insert(stWaterEngineeringSchedulingDataDto);
    }
    @Override
    public void insertFloodDispatch(StWaterEngineeringSchedulingCodeDto stWaterEngineeringSchedulingCodeDto ) {
        //河道所接收指令
        StWaterEngineeringSchedulingDataDto stWaterEngineeringSchedulingDataDtos = new StWaterEngineeringSchedulingDataDto();
        stWaterEngineeringSchedulingDataDtos.setDid(UuidUtil.get32UUIDStr());
        stWaterEngineeringSchedulingDataDtos.setStId(stWaterEngineeringSchedulingCodeDto.getStId());
        stWaterEngineeringSchedulingDataDtos.setCodeId(stWaterEngineeringSchedulingCodeDto.getId());
        stWaterEngineeringSchedulingDataDtos.setZbmc(stWaterEngineeringSchedulingCodeDto.getZbmc());
        stWaterEngineeringSchedulingDataDtos.setRank(stWaterEngineeringSchedulingCodeDto.getRank());
        stWaterEngineeringSchedulingDataDtos.setForecastState(1);
        stWaterEngineeringSchedulingDataDtos.setTime(new Date());
        stWaterEngineeringSchedulingDataDtos.setUnitId(stWaterEngineeringSchedulingCodeDto.getUnitId());
        stWaterEngineeringSchedulingDataDtos.setXcfzr(stWaterEngineeringSchedulingCodeDto.getXcfzr());
        String unit = stWaterEngineeringSchedulingCodeDto.getUnitId().equals("1") ? "河道一所" : "河道二所";
        stWaterEngineeringSchedulingDataDtos.setReason(unit + "接收并下发" + stWaterEngineeringSchedulingDataDtos.getZbmc() + "调度指令");
        stWaterEngineeringSchedulingDataDao.insert(stWaterEngineeringSchedulingDataDtos);
    }

    @Override
    public void insertFloodDispatchs(StWaterEngineeringSchedulingCodeDto stWaterEngineeringSchedulingCodeDto) {
        StWaterEngineeringSchedulingDataDto stWaterEngineeringSchedulingDataDto = new StWaterEngineeringSchedulingDataDto();
        stWaterEngineeringSchedulingDataDto.setDid(UuidUtil.get32UUIDStr());
        stWaterEngineeringSchedulingDataDto.setStId(stWaterEngineeringSchedulingCodeDto.getStId());
        stWaterEngineeringSchedulingDataDto.setCodeId(stWaterEngineeringSchedulingCodeDto.getId());
        stWaterEngineeringSchedulingDataDto.setZbmc(stWaterEngineeringSchedulingCodeDto.getZbmc());
        stWaterEngineeringSchedulingDataDto.setUnitId(stWaterEngineeringSchedulingCodeDto.getUnitId());
        stWaterEngineeringSchedulingDataDto.setForecastState(3);
        stWaterEngineeringSchedulingDataDto.setRank(stWaterEngineeringSchedulingCodeDto.getRank());
        stWaterEngineeringSchedulingDataDto.setTime(new Date());
        stWaterEngineeringSchedulingDataDto.setXcfzr(stWaterEngineeringSchedulingCodeDto.getXcfzr());
        stWaterEngineeringSchedulingDataDto.setReason(stWaterEngineeringSchedulingDataDto.getXcfzr() + "完成" + stWaterEngineeringSchedulingDataDto.getZbmc() + "调度指令");
        stWaterEngineeringSchedulingDataDao.insert(stWaterEngineeringSchedulingDataDto);
    }



    @Override
    public void insertFloodDispatchReceive(StWaterEngineeringSchedulingCodeDto stWaterEngineeringSchedulingCodeDto ) {
        StWaterEngineeringSchedulingDataDto stWaterEngineeringSchedulingDataDto = new StWaterEngineeringSchedulingDataDto();
        stWaterEngineeringSchedulingDataDto.setDid(UuidUtil.get32UUIDStr());
        stWaterEngineeringSchedulingDataDto.setStId(stWaterEngineeringSchedulingCodeDto.getStId());
        stWaterEngineeringSchedulingDataDto.setCodeId(stWaterEngineeringSchedulingCodeDto.getId());
        stWaterEngineeringSchedulingDataDto.setZbmc(stWaterEngineeringSchedulingCodeDto.getZbmc());
        stWaterEngineeringSchedulingDataDto.setUnitId(stWaterEngineeringSchedulingCodeDto.getUnitId());
        stWaterEngineeringSchedulingDataDto.setRank(stWaterEngineeringSchedulingCodeDto.getRank());
        stWaterEngineeringSchedulingDataDto.setForecastState(2);
        stWaterEngineeringSchedulingDataDto.setTime(new Date());
        if (stWaterEngineeringSchedulingCodeDto.getRank().equals("0")) {
            String unit = stWaterEngineeringSchedulingCodeDto.getUnitId().equals("1") ? "河道一所" : "河道二所";
            stWaterEngineeringSchedulingDataDto.setReason(unit + "接收" + stWaterEngineeringSchedulingDataDto.getZbmc() + "调度指令");
        } else {
            stWaterEngineeringSchedulingDataDto.setReason(stWaterEngineeringSchedulingCodeDto.getXcfzr() + "接收" + stWaterEngineeringSchedulingDataDto.getZbmc() + "调度指令");
        }
        stWaterEngineeringSchedulingDataDao.insert(stWaterEngineeringSchedulingDataDto);
    }
}
