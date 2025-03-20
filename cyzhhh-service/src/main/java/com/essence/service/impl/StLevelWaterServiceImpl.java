package com.essence.service.impl;

import com.essence.dao.StLevelWaterDao;
import com.essence.dao.entity.StLevelWaterDto;
import com.essence.interfaces.api.StLevelWaterService;
import com.essence.interfaces.model.StLevelRainEsu;
import com.essence.interfaces.model.StLevelWaterEsr;
import com.essence.interfaces.model.StLevelWaterEsu;
import com.essence.interfaces.param.StLevelWaterEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStLevelWaterEtoT;
import com.essence.service.converter.ConverterStLevelWaterTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * (StLevelWater)业务层
 * @author BINX
 * @since 2023-03-08 11:32:33
 */
@Service
public class StLevelWaterServiceImpl extends BaseApiImpl<StLevelWaterEsu, StLevelWaterEsp, StLevelWaterEsr, StLevelWaterDto> implements StLevelWaterService {

    @Autowired
    private StLevelWaterDao stLevelWaterDao;
    @Autowired
    private StLevelWaterService stLevelWaterService;
    @Autowired
    private ConverterStLevelWaterEtoT converterStLevelWaterEtoT;
    @Autowired
    private ConverterStLevelWaterTtoR converterStLevelWaterTtoR;

    public StLevelWaterServiceImpl(StLevelWaterDao stLevelWaterDao, ConverterStLevelWaterEtoT converterStLevelWaterEtoT, ConverterStLevelWaterTtoR converterStLevelWaterTtoR) {
        super(stLevelWaterDao, converterStLevelWaterEtoT, converterStLevelWaterTtoR);
    }

    /**
     * 编辑积水深度
     * @param list
     * @return
     */
    @Override
    public Object editStLevelWater(List<StLevelWaterEsu> list) {
        int update = 0;
        for (int i = 0; i < list.size(); i++) {
            StLevelWaterEsu stLevelWaterEsu = list.get(i);
            update +=stLevelWaterDao.editStLevelWater(stLevelWaterEsu.getId(),stLevelWaterEsu.getLower(),stLevelWaterEsu.getUpper());
        }
        return update;
    }
}
