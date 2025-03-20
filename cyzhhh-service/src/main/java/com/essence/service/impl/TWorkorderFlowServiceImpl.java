package com.essence.service.impl;

import com.essence.common.utils.UuidUtil;
import com.essence.dao.TWorkorderFlowDao;
import com.essence.dao.entity.TWorkorderFlowDto;
import com.essence.framework.util.DateUtil;
import com.essence.interfaces.api.TWorkorderFlowService;
import com.essence.interfaces.model.TWorkorderFlowEsr;
import com.essence.interfaces.model.TWorkorderFlowEsu;
import com.essence.interfaces.param.TWorkorderFlowEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterTWorkorderFlowEtoT;
import com.essence.service.converter.ConverterTWorkorderFlowTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 工单处理过程表(TWorkorderFlow)业务层
 * @author majunjie
 * @since 2023-06-07 10:31:07
 */
@Service
public class TWorkorderFlowServiceImpl extends BaseApiImpl<TWorkorderFlowEsu, TWorkorderFlowEsp,TWorkorderFlowEsr, TWorkorderFlowDto> implements TWorkorderFlowService {

    @Autowired
    private TWorkorderFlowDao tWorkorderFlowDao;
    @Autowired
    private ConverterTWorkorderFlowEtoT converterTWorkorderFlowEtoT;
    @Autowired
    private ConverterTWorkorderFlowTtoR converterTWorkorderFlowTtoR;

    public TWorkorderFlowServiceImpl(TWorkorderFlowDao tWorkorderFlowDao, ConverterTWorkorderFlowEtoT converterTWorkorderFlowEtoT, ConverterTWorkorderFlowTtoR converterTWorkorderFlowTtoR) {
        super(tWorkorderFlowDao, converterTWorkorderFlowEtoT, converterTWorkorderFlowTtoR);
    }

    @Override
    public String addTWorkOrderFlow(TWorkorderFlowEsu tWorkorderFlowEsu) {
        TWorkorderFlowDto tWorkorderFlowDto = converterTWorkorderFlowEtoT.toBean(tWorkorderFlowEsu);
        tWorkorderFlowDto.setId(UuidUtil.get32UUIDStr());
        tWorkorderFlowDao.insert(tWorkorderFlowDto);
        return "成功";
    }
    //系统自动生成并派发
    @Override
    public void addTWorkOrderFlows(TWorkorderFlowEsu tWorkorderFlowEsu) {
        List<TWorkorderFlowDto>list=new ArrayList<>();
        //生成工单记录
        TWorkorderFlowDto tWorkorderFlowDto=new TWorkorderFlowDto();
        tWorkorderFlowDto.setId(UuidUtil.get32UUIDStr());
        tWorkorderFlowDto.setOrderId(tWorkorderFlowEsu.getOrderId());
        tWorkorderFlowDto.setPersonName("系统管理员");
        tWorkorderFlowDto.setOrderTime(new Date());
        tWorkorderFlowDto.setOperation("1");
        list.add(tWorkorderFlowDto);
        TWorkorderFlowDto tWorkorderFlowDtoP = converterTWorkorderFlowEtoT.toBean(tWorkorderFlowEsu);
        tWorkorderFlowDtoP.setId(UuidUtil.get32UUIDStr());
        tWorkorderFlowDtoP.setPersonName("系统管理员");
        tWorkorderFlowDtoP.setOperation("2");
        tWorkorderFlowDtoP.setOrderTime(DateUtil.getNextSecond(tWorkorderFlowDto.getOrderTime(),+1));
        list.add(tWorkorderFlowDtoP);
        if (!CollectionUtils.isEmpty(list)){
            tWorkorderFlowDao.insertTWorkOrderFlow(list);
        }
    }

    @Override
    public void addTWorkOrderFlowP(TWorkorderFlowEsu tWorkorderFlowEsu) {
        List<TWorkorderFlowDto>list=new ArrayList<>();
        //生成工单记录
        TWorkorderFlowDto tWorkorderFlowDto=new TWorkorderFlowDto();
        tWorkorderFlowDto.setId(UuidUtil.get32UUIDStr());
        tWorkorderFlowDto.setOrderId(tWorkorderFlowEsu.getOrderId());
        tWorkorderFlowDto.setPersonName(tWorkorderFlowEsu.getPersonName());
        tWorkorderFlowDto.setPersonId(tWorkorderFlowEsu.getPersonId());
        tWorkorderFlowDto.setOrderTime(new Date());
        tWorkorderFlowDto.setOperation("1");
        list.add(tWorkorderFlowDto);
        TWorkorderFlowDto tWorkorderFlowDtoP = converterTWorkorderFlowEtoT.toBean(tWorkorderFlowEsu);
        tWorkorderFlowDtoP.setId(UuidUtil.get32UUIDStr());
        tWorkorderFlowDtoP.setOperation("2");
        tWorkorderFlowDtoP.setOrderTime(DateUtil.getNextSecond(tWorkorderFlowDto.getOrderTime(),+1));
        list.add(tWorkorderFlowDtoP);
        if (!CollectionUtils.isEmpty(list)){
            tWorkorderFlowDao.insertTWorkOrderFlow(list);
        }
    }
}
