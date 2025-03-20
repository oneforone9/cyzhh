package com.essence.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.essence.common.exception.BusinessException;
import com.essence.dao.WorkorderTrackDao;
import com.essence.dao.entity.WorkorderTrack;
import com.essence.euauth.common.util.UuidUtil;
import com.essence.interfaces.api.WorkorderTrackService;
import com.essence.interfaces.model.WorkorderTrackEsr;
import com.essence.interfaces.model.WorkorderTrackEsu;
import com.essence.interfaces.param.WorkorderTrackEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterWorkorderTrackEtoT;
import com.essence.service.converter.ConverterWorkorderTrackTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;


/**
 * @author zhy
 * @since 2022/5/13 15:05
 */
@Service
public class WorkorderTrackServiceImpl extends BaseApiImpl<WorkorderTrackEsu, WorkorderTrackEsp, WorkorderTrackEsr, WorkorderTrack> implements WorkorderTrackService {

    @Autowired
    private WorkorderTrackDao workorderTrackDao;
    @Autowired
    private ConverterWorkorderTrackEtoT converterWorkorderTrackEtoT;
    @Autowired
    private ConverterWorkorderTrackTtoR converterWorkorderTrackTtoR;

    public WorkorderTrackServiceImpl(WorkorderTrackDao workorderTrackDao, ConverterWorkorderTrackEtoT converterWorkorderTrackEtoT, ConverterWorkorderTrackTtoR converterWorkorderTrackTtoR) {
        super(workorderTrackDao, converterWorkorderTrackEtoT, converterWorkorderTrackTtoR);
    }

    @Override
    public int insert(WorkorderTrackEsu workorderTrackEsu) {
        // 设置主键
        workorderTrackEsu.setId(UuidUtil.get32UUIDStr());
        // 设置事件
        workorderTrackEsu.setGmtCreate(new Date());
        return super.insert(workorderTrackEsu);
    }

    @Override
    public int update(WorkorderTrackEsu workorderTrackEsu) {
        throw new BusinessException("不可更新");
    }

    @Override
    public List<WorkorderTrackEsr> findListByOrderId(String orderId) {
        QueryWrapper<WorkorderTrack> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        queryWrapper.orderByAsc("gmt_create");
        return converterWorkorderTrackTtoR.toList(workorderTrackDao.selectList(queryWrapper));
    }

    @Override
    public WorkorderTrackEsr findOneByOrderId(String orderId) {
        QueryWrapper<WorkorderTrack> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        queryWrapper.orderByDesc("gmt_create");
        IPage<WorkorderTrack> workorderTrackIPage = workorderTrackDao.selectPage(new Page<>(1, 1), queryWrapper);
        if (CollectionUtils.isEmpty(workorderTrackIPage.getRecords())){
            return null;
        }
        return converterWorkorderTrackTtoR.toBean(workorderTrackIPage.getRecords().get(0));
    }
}

