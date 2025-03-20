package com.essence.service.impl;

import cn.hutool.core.annotation.MirroredAnnotationAttribute;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.StOfficeBaseDao;
import com.essence.dao.StOfficeBaseRelationDao;
import com.essence.dao.entity.HtglDto;
import com.essence.dao.entity.StOfficeBaseDto;
import com.essence.dao.entity.StOfficeBaseRelationDto;
import com.essence.interfaces.api.StOfficeBaseRelationService;
import com.essence.interfaces.api.StOfficeBaseService;
import com.essence.interfaces.model.StOfficeBaseRelationEsr;
import com.essence.interfaces.model.StOfficeBaseRelationEsu;
import com.essence.interfaces.param.StOfficeBaseRelationEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStOfficeBaseRelationEtoT;
import com.essence.service.converter.ConverterStOfficeBaseRelationTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 科室联系人表(StOfficeBaseRelation)业务层
 *
 * @author liwy
 * @since 2023-03-29 14:21:21
 */
@Service
public class StOfficeBaseRelationServiceImpl extends BaseApiImpl<StOfficeBaseRelationEsu, StOfficeBaseRelationEsp, StOfficeBaseRelationEsr, StOfficeBaseRelationDto> implements StOfficeBaseRelationService {

    @Autowired
    private StOfficeBaseRelationDao stOfficeBaseRelationDao;
    @Autowired
    private ConverterStOfficeBaseRelationEtoT converterStOfficeBaseRelationEtoT;
    @Autowired
    private ConverterStOfficeBaseRelationTtoR converterStOfficeBaseRelationTtoR;
    @Autowired
    private StOfficeBaseDao stOfficeBaseDao;

    public StOfficeBaseRelationServiceImpl(StOfficeBaseRelationDao stOfficeBaseRelationDao, ConverterStOfficeBaseRelationEtoT converterStOfficeBaseRelationEtoT, ConverterStOfficeBaseRelationTtoR converterStOfficeBaseRelationTtoR) {
        super(stOfficeBaseRelationDao, converterStOfficeBaseRelationEtoT, converterStOfficeBaseRelationTtoR);
    }
}
