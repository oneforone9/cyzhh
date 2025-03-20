package com.essence.service.impl;

import com.essence.dao.StGreenReportRelationDao;
import com.essence.dao.entity.StGreenReportRelationDto;
import com.essence.interfaces.api.StGreenReportRelationService;
import com.essence.interfaces.model.StGreenReportRelationEsr;
import com.essence.interfaces.model.StGreenReportRelationEsu;
import com.essence.interfaces.param.StGreenReportRelationEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStGreenReportRelationEtoT;
import com.essence.service.converter.ConverterStGreenReportRelationTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 绿化保洁工作日志上报表-关联表(StGreenReportRelation)业务层
 * @author liwy
 * @since 2023-03-17 17:19:39
 */
@Service
public class StGreenReportRelationServiceImpl extends BaseApiImpl<StGreenReportRelationEsu, StGreenReportRelationEsp, StGreenReportRelationEsr, StGreenReportRelationDto> implements StGreenReportRelationService {

    @Autowired
    private StGreenReportRelationDao stGreenReportRelationDao;
    @Autowired
    private ConverterStGreenReportRelationEtoT converterStGreenReportRelationEtoT;
    @Autowired
    private ConverterStGreenReportRelationTtoR converterStGreenReportRelationTtoR;

    public StGreenReportRelationServiceImpl(StGreenReportRelationDao stGreenReportRelationDao, ConverterStGreenReportRelationEtoT converterStGreenReportRelationEtoT, ConverterStGreenReportRelationTtoR converterStGreenReportRelationTtoR) {
        super(stGreenReportRelationDao, converterStGreenReportRelationEtoT, converterStGreenReportRelationTtoR);
    }
}
