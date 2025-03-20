package com.essence.service.impl.alg;


import com.essence.dao.StSectionModelDao;
import com.essence.dao.entity.StSectionModelDto;
import com.essence.eluban.utils.SnowflakeIdWorker;
import com.essence.interfaces.api.StSectionModelService;
import com.essence.interfaces.model.StSectionModelEsr;
import com.essence.interfaces.model.StSectionModelEsu;
import com.essence.interfaces.param.StSectionModelEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStSectionModelEtoT;
import com.essence.service.converter.ConverterStSectionModelTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 模型断面基础表(StSectionModel)业务层
 * @author BINX
 * @since 2023-04-19 18:15:41
 */
@Service
public class StSectionModelServiceImpl extends BaseApiImpl<StSectionModelEsu, StSectionModelEsp, StSectionModelEsr, StSectionModelDto> implements StSectionModelService {


    @Autowired
    private StSectionModelDao stSectionModelDao;
    @Autowired
    private ConverterStSectionModelEtoT converterStSectionModelEtoT;
    @Autowired
    private ConverterStSectionModelTtoR converterStSectionModelTtoR;

    public StSectionModelServiceImpl(StSectionModelDao stSectionModelDao, ConverterStSectionModelEtoT converterStSectionModelEtoT, ConverterStSectionModelTtoR converterStSectionModelTtoR) {
        super(stSectionModelDao, converterStSectionModelEtoT, converterStSectionModelTtoR);
    }
}
