package com.essence.service.impl;



import com.essence.dao.StEcologyWaterDao;
import com.essence.dao.entity.StEcologyWaterDto;
import com.essence.interfaces.api.StEcologyWaterService;
import com.essence.interfaces.model.StEcologyWaterEsr;
import com.essence.interfaces.model.StEcologyWaterEsu;
import com.essence.interfaces.param.StEcologyWaterEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStEcologyWaterEtoT;
import com.essence.service.converter.ConverterStEcologyWaterTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * (StEcologyWater)业务层
 * @author BINX
 * @since 2023-02-21 16:33:38
 */
@Service
public class StEcologyWaterServiceImpl extends BaseApiImpl<StEcologyWaterEsu, StEcologyWaterEsp, StEcologyWaterEsr, StEcologyWaterDto> implements StEcologyWaterService {

    @Autowired
    private StEcologyWaterDao stEcologyWaterDao;
    @Autowired
    private StEcologyWaterService stEcologyWaterService;
    @Autowired
    private ConverterStEcologyWaterEtoT converterStEcologyWaterEtoT;
    @Autowired
    private ConverterStEcologyWaterTtoR converterStEcologyWaterTtoR;

    public StEcologyWaterServiceImpl(StEcologyWaterDao stEcologyWaterDao, ConverterStEcologyWaterEtoT converterStEcologyWaterEtoT, ConverterStEcologyWaterTtoR converterStEcologyWaterTtoR) {
        super(stEcologyWaterDao, converterStEcologyWaterEtoT, converterStEcologyWaterTtoR);
    }

    /**
     * 北部、中部、南部 汇总查询
     * @return
     */
    @Override
    public Object count() {
        List<Map> map = stEcologyWaterDao.count();
        return map;
    }
}
