package com.essence.service.impl;

import com.essence.dao.StPlanOperateDao;
import com.essence.dao.entity.StPlanOperateDto;
import com.essence.interfaces.api.StPlanOperateService;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.StPlanOperateEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStPlanOperateEtoT;
import com.essence.service.converter.ConverterStPlanOperateTtoR;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 养护内容记录表(StPlanOperate)业务层
 *
 * @author liwy
 * @since 2023-07-24 14:16:38
 */
@Slf4j
@Service
public class StPlanOperateServiceImpl extends BaseApiImpl<StPlanOperateEsu, StPlanOperateEsp, StPlanOperateEsr, StPlanOperateDto> implements StPlanOperateService {

    @Autowired
    private StPlanOperateDao stPlanOperateDao;
    @Autowired
    private ConverterStPlanOperateEtoT converterStPlanOperateEtoT;
    @Autowired
    private ConverterStPlanOperateTtoR converterStPlanOperateTtoR;


    public StPlanOperateServiceImpl(StPlanOperateDao stPlanOperateDao, ConverterStPlanOperateEtoT converterStPlanOperateEtoT, ConverterStPlanOperateTtoR converterStPlanOperateTtoR) {
        super(stPlanOperateDao, converterStPlanOperateEtoT, converterStPlanOperateTtoR);
    }

    /**
     * 新增养护工单作业录
     *
     * @param list
     * @return
     */
    @Override
    public Object addStPlanOperate(List<StPlanOperateEsu> list) {
        int insert = 0;
        for (int i = 0; i < list.size(); i++) {
            StPlanOperateEsu stPlanOperateEsu = list.get(i);
            stPlanOperateEsu.setGmtCreate(new Date());
            // 新增
            insert = super.insert(stPlanOperateEsu);
        }
        return insert;
    }
}
