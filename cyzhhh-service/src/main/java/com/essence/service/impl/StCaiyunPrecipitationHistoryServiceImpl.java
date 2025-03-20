package com.essence.service.impl;

import com.essence.dao.StCaiyunPrecipitationHistoryDao;
import com.essence.dao.entity.caiyun.StCaiyunPrecipitationHistoryDto;
import com.essence.interfaces.api.StCaiyunPrecipitationHistoryService;
import com.essence.interfaces.model.StCaiyunPrecipitationHistoryEsr;
import com.essence.interfaces.model.StCaiyunPrecipitationHistoryEsu;
import com.essence.interfaces.model.StCaseResRainQuery;
import com.essence.interfaces.param.StCaiyunPrecipitationHistoryEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStCaiyunPrecipitationHistoryEtoT;
import com.essence.service.converter.ConverterStCaiyunPrecipitationHistoryTtoR;
import com.essence.service.utils.BatchUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* 彩云预报历史数据 (st_caiyun_precipitation_history)表数据库业务层
*
* @author BINX
* @since 2023年5月4日 下午3:47:15
*/
@Service
public class StCaiyunPrecipitationHistoryServiceImpl extends BaseApiImpl<StCaiyunPrecipitationHistoryEsu, StCaiyunPrecipitationHistoryEsp, StCaiyunPrecipitationHistoryEsr, StCaiyunPrecipitationHistoryDto> implements StCaiyunPrecipitationHistoryService {


    @Autowired
    private StCaiyunPrecipitationHistoryDao stCaiyunPrecipitationHistoryDao;
    @Autowired
    private ConverterStCaiyunPrecipitationHistoryEtoT converterStCaiyunPrecipitationHistoryEtoT;
    @Autowired
    private ConverterStCaiyunPrecipitationHistoryTtoR converterStCaiyunPrecipitationHistoryTtoR;

    @Autowired
    private BatchUtils batchUtils;

    public StCaiyunPrecipitationHistoryServiceImpl(StCaiyunPrecipitationHistoryDao stCaiyunPrecipitationHistoryDao, ConverterStCaiyunPrecipitationHistoryEtoT converterStCaiyunPrecipitationHistoryEtoT, ConverterStCaiyunPrecipitationHistoryTtoR converterStCaiyunPrecipitationHistoryTtoR) {
        super(stCaiyunPrecipitationHistoryDao, converterStCaiyunPrecipitationHistoryEtoT, converterStCaiyunPrecipitationHistoryTtoR);
    }

    @Override
    public void saveOrUpdate(List<StCaiyunPrecipitationHistoryEsu> stCaiyunPrecipitationHistoryEsuList) {
        List<StCaiyunPrecipitationHistoryDto> list = converterStCaiyunPrecipitationHistoryEtoT.toList(stCaiyunPrecipitationHistoryEsuList);
        batchUtils.batchInsert(list,StCaiyunPrecipitationHistoryDao.class,(m,e)->{
            m.saveOrUpdate(e);
        },500);
    }

    @Override
    public List<StCaiyunPrecipitationHistoryDto> selectRain(StCaseResRainQuery stCaseResRainQuery) {


        return stCaiyunPrecipitationHistoryDao.selectByTime(stCaseResRainQuery.getStartTime(),stCaseResRainQuery.getEndTime());
    }

}
