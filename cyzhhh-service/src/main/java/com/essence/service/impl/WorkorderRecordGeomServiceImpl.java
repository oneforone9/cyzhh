package com.essence.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.WorkorderRecordGeomDao;
import com.essence.dao.entity.WorkorderRecordGeom;
import com.essence.euauth.common.util.UuidUtil;
import com.essence.interfaces.api.WorkorderRecordGeomService;
import com.essence.interfaces.model.WorkorderRecordGeomEsr;
import com.essence.interfaces.model.WorkorderRecordGeomEsu;
import com.essence.interfaces.param.WorkorderRecordGeomEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterWorkorderRecordGeomEtoT;
import com.essence.service.converter.ConverterWorkorderRecordGeomTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author zhy
 * @since 2022/5/13 15:05
 */
@Service
public class WorkorderRecordGeomServiceImpl extends BaseApiImpl<WorkorderRecordGeomEsu, WorkorderRecordGeomEsp, WorkorderRecordGeomEsr, WorkorderRecordGeom> implements WorkorderRecordGeomService {

    @Autowired
    private WorkorderRecordGeomDao workorderRecordGeomDao;
    @Autowired
    private ConverterWorkorderRecordGeomEtoT converterWorkorderRecordGeomEtoT;
    @Autowired
    private ConverterWorkorderRecordGeomTtoR converterWorkorderRecordGeomTtoR;


    public WorkorderRecordGeomServiceImpl(WorkorderRecordGeomDao workorderRecordGeomDao, ConverterWorkorderRecordGeomEtoT converterWorkorderRecordGeomEtoT, ConverterWorkorderRecordGeomTtoR converterWorkorderRecordGeomTtoR) {
        super(workorderRecordGeomDao, converterWorkorderRecordGeomEtoT, converterWorkorderRecordGeomTtoR);
    }

    @Override
    public int insert(WorkorderRecordGeomEsu workorderRecordGeomEsu) {
        workorderRecordGeomEsu.setId(UuidUtil.get32UUIDStr());
        return super.insert(workorderRecordGeomEsu);
    }

    @Override
    public List<WorkorderRecordGeomEsr> findByOrderId(String orderId) {
        QueryWrapper<WorkorderRecordGeom> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        List<WorkorderRecordGeom> workorderRecordGeoms = workorderRecordGeomDao.selectList(queryWrapper);
        return converterWorkorderRecordGeomTtoR.toList(workorderRecordGeoms);
    }
}
