package com.essence.service.impl;

import com.essence.dao.EventBaseStatusDao;
import com.essence.dao.FileBaseDao;
import com.essence.dao.WorkorderBaseDao;
import com.essence.dao.entity.EventBaseStatusEntity;
import com.essence.interfaces.api.CodeGenerateService;
import com.essence.interfaces.api.EventBaseStatusService;
import com.essence.interfaces.api.FileBaseService;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.EventBaseEsu;
import com.essence.interfaces.model.EventBaseStatusEsr;
import com.essence.interfaces.model.EventBaseStatusEsu;
import com.essence.interfaces.param.EventBaseStatusEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterEventBaseEtoT;
import com.essence.service.converter.ConverterEventBaseTtoR;
import com.essence.service.converter.ConverterEventStatusBaseEtoT;
import com.essence.service.converter.ConverterEventStatusBaseTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * @author zhy
 * @since 2022/5/13 15:05
 */
@Service
public class EventBaseStatusServiceImpl extends BaseApiImpl<EventBaseStatusEsu, EventBaseStatusEsp, EventBaseStatusEsr, EventBaseStatusEntity> implements EventBaseStatusService {

    @Autowired
    private EventBaseStatusDao eventBaseStatusDao;
    @Autowired
    private ConverterEventBaseEtoT converterEventBaseEtoT;
    @Autowired
    private ConverterEventBaseTtoR converterEventBaseTtoR;
    @Autowired
    private CodeGenerateService codeGenerateService;

    @Autowired
    private FileBaseService fileBaseService;
    @Autowired
    private FileBaseDao fileBaseDao;
    @Resource
    private WorkorderBaseDao workorderBaseDao;

    public EventBaseStatusServiceImpl(EventBaseStatusDao eventBaseDao, ConverterEventStatusBaseEtoT converterEventBaseEtoT, ConverterEventStatusBaseTtoR converterEventBaseTtoR) {
        super(eventBaseDao, converterEventBaseEtoT, converterEventBaseTtoR);
    }


    @Override
    public Paginator<EventBaseStatusEsr> findByPaginator(PaginatorParam param) {

        Paginator<EventBaseStatusEsr> paginator = super.findByPaginator(param);
        return paginator;
    }

    @Override
    public int add(EventBaseEsu eventBaseEsu) {
        return 0;
    }
}
