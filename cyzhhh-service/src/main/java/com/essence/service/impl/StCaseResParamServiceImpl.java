package com.essence.service.impl;

import com.essence.dao.StCaseResParamDao;
import com.essence.dao.entity.alg.StCaseResParamDto;
import com.essence.interfaces.api.StCaseResParamService;
import com.essence.interfaces.model.StCaseResParamEsr;
import com.essence.interfaces.model.StCaseResParamEsu;
import com.essence.interfaces.param.StCaseResParamEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStCaseResParamEtoT;
import com.essence.service.converter.ConverterStCaseResParamTtoR;
import com.essence.service.utils.BatchUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.BiConsumer;

/**
* 方案执行结果表入参表 (st_case_res_param)表数据库业务层
*
* @author BINX
* @since 2023年4月28日 下午5:45:25
*/
@Service
public class StCaseResParamServiceImpl extends BaseApiImpl<StCaseResParamEsu, StCaseResParamEsp, StCaseResParamEsr, StCaseResParamDto> implements StCaseResParamService {


    @Autowired
    private StCaseResParamDao stCaseResParamDao;
    @Autowired
    private ConverterStCaseResParamEtoT converterStCaseResParamEtoT;
    @Autowired
    private ConverterStCaseResParamTtoR converterStCaseResParamTtoR;

    public StCaseResParamServiceImpl(StCaseResParamDao stCaseResParamDao, ConverterStCaseResParamEtoT converterStCaseResParamEtoT, ConverterStCaseResParamTtoR converterStCaseResParamTtoR) {
        super(stCaseResParamDao, converterStCaseResParamEtoT, converterStCaseResParamTtoR);
    }

    @Autowired
    private BatchUtils batchUtils;


    @Override
    public void saveOrUpdate(List<StCaseResParamEsu> stCaseResParamEsuList) {
        List<StCaseResParamDto> sysRoleMenus = converterStCaseResParamEtoT.toList(stCaseResParamEsuList);
        batchUtils.multiBatchInsert(sysRoleMenus, StCaseResParamDao.class,(mapper,e)->{
            mapper.saveOrUpdate(e);
        },500);
    }
}
