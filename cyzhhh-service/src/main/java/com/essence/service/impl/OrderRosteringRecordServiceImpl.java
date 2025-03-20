package com.essence.service.impl;

import com.essence.dao.OrderRosteringRecordDao;
import com.essence.dao.entity.OrderRosteringRecord;
import com.essence.euauth.common.util.UuidUtil;
import com.essence.interfaces.api.OrderRosteringRecordService;
import com.essence.interfaces.model.OrderRosteringRecordEsr;
import com.essence.interfaces.model.OrderRosteringRecordEsu;
import com.essence.interfaces.param.OrderRosteringRecordEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterOrderRosteringRecordEtoT;
import com.essence.service.converter.ConverterOrderRosteringRecordTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author zhy
 * @since 2022/5/13 15:05
 */
@Service
public class OrderRosteringRecordServiceImpl extends BaseApiImpl<OrderRosteringRecordEsu, OrderRosteringRecordEsp, OrderRosteringRecordEsr, OrderRosteringRecord> implements OrderRosteringRecordService {

    @Autowired
    private OrderRosteringRecordDao orderRosteringRecordDao;
    @Autowired
    private ConverterOrderRosteringRecordEtoT converterOrderRosteringRecordEtoT;
    @Autowired
    private ConverterOrderRosteringRecordTtoR converterOrderRosteringRecordTtoR;


    public OrderRosteringRecordServiceImpl(OrderRosteringRecordDao orderRosteringRecordDao, ConverterOrderRosteringRecordEtoT converterOrderRosteringRecordEtoT, ConverterOrderRosteringRecordTtoR converterOrderRosteringRecordTtoR) {
        super(orderRosteringRecordDao, converterOrderRosteringRecordEtoT, converterOrderRosteringRecordTtoR);
    }

    @Override
    public int insert(OrderRosteringRecordEsu orderRosteringRecordEsu) {
        orderRosteringRecordEsu.setId(UuidUtil.get32UUIDStr());
        return super.insert(orderRosteringRecordEsu);
    }
}
