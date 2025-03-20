package com.essence.service.impl;


import com.essence.dao.StPlanOperateRejectDao;
import com.essence.dao.entity.StPlanOperateRejectDto;
import com.essence.interfaces.api.StPlanOperateRejectService;
import com.essence.interfaces.model.StPlanOperateRejectEsr;
import com.essence.interfaces.model.StPlanOperateRejectEsu;
import com.essence.interfaces.param.StPlanOperateRejectEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStPlanOperateRejectEtoT;
import com.essence.service.converter.ConverterStPlanOperateRejectTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 养护内容-驳回记录表(StPlanOperateReject)业务层
 *
 * @author BINX
 * @since 2023-09-11 17:52:35
 */
@Service
public class StPlanOperateRejectServiceImpl extends BaseApiImpl<StPlanOperateRejectEsu, StPlanOperateRejectEsp, StPlanOperateRejectEsr, StPlanOperateRejectDto> implements StPlanOperateRejectService {


    @Autowired
    private StPlanOperateRejectDao stPlanOperateRejectDao;
    @Autowired
    private ConverterStPlanOperateRejectEtoT converterStPlanOperateRejectEtoT;
    @Autowired
    private ConverterStPlanOperateRejectTtoR converterStPlanOperateRejectTtoR;

    public StPlanOperateRejectServiceImpl(StPlanOperateRejectDao stPlanOperateRejectDao, ConverterStPlanOperateRejectEtoT converterStPlanOperateRejectEtoT, ConverterStPlanOperateRejectTtoR converterStPlanOperateRejectTtoR) {
        super(stPlanOperateRejectDao, converterStPlanOperateRejectEtoT, converterStPlanOperateRejectTtoR);
    }

    @Override
    public Object addStPlanOperate(List<StPlanOperateRejectEsu> list) {
        int insert = 0;
        for (int i = 0; i < list.size(); i++) {
            StPlanOperateRejectEsu stPlanOperateRejectEsu = list.get(i);
            stPlanOperateRejectEsu.setGmtCreate(new Date());
            // 新增
            insert = super.insert(stPlanOperateRejectEsu);
        }
        return insert;
    }
}
