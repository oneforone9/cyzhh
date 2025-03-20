package com.essence.service.impl;

import com.essence.dao.StSideGateRelationDao;
import com.essence.dao.entity.StSideGateRelationDto;
import com.essence.interfaces.api.StSideGateRelationService;
import com.essence.interfaces.model.StSideGateRelationEsr;
import com.essence.interfaces.model.StSideGateRelationEsu;
import com.essence.interfaces.param.StSideGateRelationEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterStSideGateRelationEtoT;
import com.essence.service.converter.ConverterStSideGateRelationTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 闸坝负责人关联表(StSideGateRelation)业务层
 *
 * @author liwy
 * @since 2023-04-13 17:50:12
 */
@Service
public class StSideGateRelationServiceImpl extends BaseApiImpl<StSideGateRelationEsu, StSideGateRelationEsp, StSideGateRelationEsr, StSideGateRelationDto> implements StSideGateRelationService {

    @Autowired
    private StSideGateRelationDao stSideGateRelationDao;
    @Autowired
    private ConverterStSideGateRelationEtoT converterStSideGateRelationEtoT;
    @Autowired
    private ConverterStSideGateRelationTtoR converterStSideGateRelationTtoR;
    @Autowired
    private StSideGateRelationService stSideGateRelationService;

    public StSideGateRelationServiceImpl(StSideGateRelationDao stSideGateRelationDao, ConverterStSideGateRelationEtoT converterStSideGateRelationEtoT, ConverterStSideGateRelationTtoR converterStSideGateRelationTtoR) {
        super(stSideGateRelationDao, converterStSideGateRelationEtoT, converterStSideGateRelationTtoR);
    }

    /**
     * 编辑闸坝负责人信息
     *
     * @param stSideGateRelationEsu
     * @return
     */
    @Override
    public Object updateStSideGateRelation(StSideGateRelationEsu stSideGateRelationEsu) {
        Integer id = stSideGateRelationEsu.getId();
        int insert;
        if ("".equals(id) || id == null) {
            //新增
            insert = stSideGateRelationService.insert(stSideGateRelationEsu);
        } else {
            //编辑
            String jsfzr = stSideGateRelationEsu.getJsfzr();
            String jsfzrPhone = stSideGateRelationEsu.getJsfzrPhone();
            String xcfzr = stSideGateRelationEsu.getXcfzr();
            String xcfzrPhone = stSideGateRelationEsu.getXcfzrPhone();
            String xcfzrUserId = stSideGateRelationEsu.getXcfzrUserId();
            String xzfzr = stSideGateRelationEsu.getXzfzr();
            String xzfzrPhone = stSideGateRelationEsu.getXzfzrPhone();
            insert = stSideGateRelationDao.updateStSideGateRelation(id, jsfzr, jsfzrPhone, xcfzr, xcfzrPhone, xcfzrUserId, xzfzr, xzfzrPhone);
        }
        return insert;
    }
}
