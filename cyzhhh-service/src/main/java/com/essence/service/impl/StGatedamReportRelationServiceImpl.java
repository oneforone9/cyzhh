package com.essence.service.impl;

import com.essence.dao.StGatedamReportRelationDao;
import com.essence.dao.entity.StGatedamReportRelationDto;
import com.essence.interfaces.api.StGatedamReportRelationService;
import com.essence.interfaces.model.StGatedamReportRelationEsr;
import com.essence.interfaces.model.StGatedamReportRelationEsu;
import com.essence.interfaces.param.StGatedamReportRelationEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStGatedamReportRelationEtoT;
import com.essence.service.converter.ConverterStGatedamReportRelationTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 闸坝运行维保日志上报-关联表(StGatedamReportRelation)业务层
 * @author liwy
 * @since 2023-03-15 11:56:56
 */
@Service
public class StGatedamReportRelationServiceImpl extends BaseApiImpl<StGatedamReportRelationEsu, StGatedamReportRelationEsp, StGatedamReportRelationEsr, StGatedamReportRelationDto> implements StGatedamReportRelationService {

    @Autowired
    private StGatedamReportRelationDao stGatedamReportRelationDao;
    @Autowired
    private ConverterStGatedamReportRelationEtoT converterStGatedamReportRelationEtoT;
    @Autowired
    private ConverterStGatedamReportRelationTtoR converterStGatedamReportRelationTtoR;

    public StGatedamReportRelationServiceImpl(StGatedamReportRelationDao stGatedamReportRelationDao, ConverterStGatedamReportRelationEtoT converterStGatedamReportRelationEtoT, ConverterStGatedamReportRelationTtoR converterStGatedamReportRelationTtoR) {
        super(stGatedamReportRelationDao, converterStGatedamReportRelationEtoT, converterStGatedamReportRelationTtoR);
    }
}
