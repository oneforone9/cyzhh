package com.essence.service.impl.alg;


import com.essence.dao.StCaseProgressDao;
import com.essence.dao.entity.StCaseProgressDto;
import com.essence.eluban.utils.SnowflakeIdWorker;
import com.essence.interfaces.api.StCaseProgressService;
import com.essence.interfaces.model.StCaseProgressEsr;
import com.essence.interfaces.model.StCaseProgressEsu;
import com.essence.interfaces.param.StCaseProgressEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStCaseProgressEtoT;
import com.essence.service.converter.ConverterStCaseProgressTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 方案执行进度表(StCaseProgress)业务层
 * @author BINX
 * @since 2023-04-18 17:03:02
 */
@Service
public class StCaseProgressServiceImpl extends BaseApiImpl<StCaseProgressEsu, StCaseProgressEsp, StCaseProgressEsr, StCaseProgressDto> implements StCaseProgressService {


    @Autowired
    private StCaseProgressDao stCaseProgressDao;
    @Autowired
    private ConverterStCaseProgressEtoT converterStCaseProgressEtoT;
    @Autowired
    private ConverterStCaseProgressTtoR converterStCaseProgressTtoR;

    public StCaseProgressServiceImpl(StCaseProgressDao stCaseProgressDao, ConverterStCaseProgressEtoT converterStCaseProgressEtoT, ConverterStCaseProgressTtoR converterStCaseProgressTtoR) {
        super(stCaseProgressDao, converterStCaseProgressEtoT, converterStCaseProgressTtoR);
    }
}
