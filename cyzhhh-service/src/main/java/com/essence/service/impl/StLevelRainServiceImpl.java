package com.essence.service.impl;

import com.essence.dao.StLevelRainDao;
import com.essence.dao.entity.StLevelRainDto;
import com.essence.interfaces.api.StLevelRainService;
import com.essence.interfaces.model.StCompanyBaseEsu;
import com.essence.interfaces.model.StLevelRainEsr;
import com.essence.interfaces.model.StLevelRainEsu;
import com.essence.interfaces.param.StLevelRainEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStLevelRainEtoT;
import com.essence.service.converter.ConverterStLevelRainTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * (StLevelRain)业务层
 * @author BINX
 * @since 2023-03-08 11:31:39
 */
@Service
public class StLevelRainServiceImpl extends BaseApiImpl<StLevelRainEsu, StLevelRainEsp, StLevelRainEsr, StLevelRainDto> implements StLevelRainService {

    @Autowired
    private StLevelRainDao stLevelRainDao;
    @Autowired
    private StLevelRainService stLevelRainService;
    @Autowired
    private ConverterStLevelRainEtoT converterStLevelRainEtoT;
    @Autowired
    private ConverterStLevelRainTtoR converterStLevelRainTtoR;

    public StLevelRainServiceImpl(StLevelRainDao stLevelRainDao, ConverterStLevelRainEtoT converterStLevelRainEtoT, ConverterStLevelRainTtoR converterStLevelRainTtoR) {
        super(stLevelRainDao, converterStLevelRainEtoT, converterStLevelRainTtoR);
    }

    /**
     * 编辑小时降雨量
     * @param list
     * @return
     */
    @Override
    public Object editStLevelRain(List<StLevelRainEsu> list) {
        int update = 0;
        for (int i = 0; i < list.size(); i++) {
            StLevelRainEsu stLevelRainEsu = list.get(i);
            Integer flag = stLevelRainEsu.getFlag();
            if (flag == 1) {
                update += stLevelRainDao.editStLevelRain(stLevelRainEsu.getId(),stLevelRainEsu.getLower1(),stLevelRainEsu.getUpper1());
            }else if(flag == 3){
                update += stLevelRainDao.editStLevelRain3(stLevelRainEsu.getId(),stLevelRainEsu.getLower3(),stLevelRainEsu.getUpper3());
            }else if(flag == 12){
                update += stLevelRainDao.editStLevelRain12(stLevelRainEsu.getId(),stLevelRainEsu.getLower12(),stLevelRainEsu.getUpper12());
            }else if(flag == 24){
                update += stLevelRainDao.editStLevelRain24(stLevelRainEsu.getId(),stLevelRainEsu.getLower24(),stLevelRainEsu.getUpper24());
            }
        }
        return update;
    }
}
