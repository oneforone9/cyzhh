package com.essence.service.impl.alg;


import com.essence.dao.StCaseProcessDao;
import com.essence.dao.entity.alg.StCaseProcessDto;
import com.essence.eluban.utils.SnowflakeIdWorker;
import com.essence.interfaces.api.StCaseProcessService;
import com.essence.interfaces.model.StCaseProcessEsr;
import com.essence.interfaces.model.StCaseProcessEsu;
import com.essence.interfaces.param.StCaseProcessEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStCaseProcessEtoT;
import com.essence.service.converter.ConverterStCaseProcessTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 方案执行过程表-存放入参等信息(StCaseProcess)业务层
 * @author BINX
 * @since 2023-04-17 16:30:06
 */
@Service
public class StCaseProcessServiceImpl extends BaseApiImpl<StCaseProcessEsu, StCaseProcessEsp, StCaseProcessEsr, StCaseProcessDto> implements StCaseProcessService {


    @Autowired
    private StCaseProcessDao stCaseProcessDao;
    @Autowired
    private ConverterStCaseProcessEtoT converterStCaseProcessEtoT;
    @Autowired
    private ConverterStCaseProcessTtoR converterStCaseProcessTtoR;

    public StCaseProcessServiceImpl(StCaseProcessDao stCaseProcessDao, ConverterStCaseProcessEtoT converterStCaseProcessEtoT, ConverterStCaseProcessTtoR converterStCaseProcessTtoR) {
        super(stCaseProcessDao, converterStCaseProcessEtoT, converterStCaseProcessTtoR);
    }
}
