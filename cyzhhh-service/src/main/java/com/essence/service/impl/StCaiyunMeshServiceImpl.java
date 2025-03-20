package com.essence.service.impl;

import com.essence.dao.StCaiyunMeshDao;
import com.essence.dao.entity.caiyun.StCaiyunMeshDto;
import com.essence.interfaces.api.StCaiyunMeshService;
import com.essence.interfaces.model.StCaiyunMeshEsr;
import com.essence.interfaces.model.StCaiyunMeshEsu;
import com.essence.interfaces.param.StCaiyunMeshEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStCaiyunMeshEtoT;
import com.essence.service.converter.ConverterStCaiyunMeshTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* 彩云预报网格编号 (st_caiyun_mesh)表数据库业务层
*
* @author BINX
* @since 2023年5月4日 下午3:47:15
*/
@Service
public class StCaiyunMeshServiceImpl extends BaseApiImpl<StCaiyunMeshEsu, StCaiyunMeshEsp, StCaiyunMeshEsr, StCaiyunMeshDto> implements StCaiyunMeshService {


    @Autowired
    private StCaiyunMeshDao stCaiyunMeshDao;
    @Autowired
    private ConverterStCaiyunMeshEtoT converterStCaiyunMeshEtoT;
    @Autowired
    private ConverterStCaiyunMeshTtoR converterStCaiyunMeshTtoR;

    public StCaiyunMeshServiceImpl(StCaiyunMeshDao stCaiyunMeshDao, ConverterStCaiyunMeshEtoT converterStCaiyunMeshEtoT, ConverterStCaiyunMeshTtoR converterStCaiyunMeshTtoR) {
        super(stCaiyunMeshDao, converterStCaiyunMeshEtoT, converterStCaiyunMeshTtoR);
    }
}
