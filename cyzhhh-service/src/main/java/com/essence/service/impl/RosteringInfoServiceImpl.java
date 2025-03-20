package com.essence.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.RosteringInfoDao;
import com.essence.dao.entity.RosteringInfo;
import com.essence.interfaces.api.RosteringInfoService;
import com.essence.interfaces.model.RosteringInfoEsr;
import com.essence.interfaces.model.RosteringInfoEsu;
import com.essence.interfaces.param.RosteringInfoEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterRosteringInfoEtoT;
import com.essence.service.converter.ConverterRosteringInfoTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author zhy
 * @since 2022/5/13 15:05
 */
@Service
public class RosteringInfoServiceImpl extends BaseApiImpl<RosteringInfoEsu, RosteringInfoEsp, RosteringInfoEsr, RosteringInfo> implements RosteringInfoService {

    @Autowired
    private RosteringInfoDao rosteringInfoDao;
    @Autowired
    private ConverterRosteringInfoEtoT converterRosteringInfoEtoT;
    @Autowired
    private ConverterRosteringInfoTtoR converterRosteringInfoTtoR;

    public RosteringInfoServiceImpl(RosteringInfoDao rosteringInfoDao, ConverterRosteringInfoEtoT converterRosteringInfoEtoT, ConverterRosteringInfoTtoR converterRosteringInfoTtoR) {
        super(rosteringInfoDao, converterRosteringInfoEtoT, converterRosteringInfoTtoR);
    }

    @Override
    public int updateByPersonId(String personId, String personName) {
        RosteringInfo rosteringInfo = new RosteringInfo();
        rosteringInfo.setPersonName(personName);

        QueryWrapper<RosteringInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("person_id", personId);
        return rosteringInfoDao.update(rosteringInfo, queryWrapper);
    }

    @Override
    public int updateByReaId(String reaId, String reaName) {
        RosteringInfo rosteringInfo = new RosteringInfo();
        rosteringInfo.setReaName(reaName);

        QueryWrapper<RosteringInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("rea_id", reaId);
        return rosteringInfoDao.update(rosteringInfo, queryWrapper);
    }
}
