package com.essence.service.impl;

import com.essence.dao.ReaFocusGeomDao;
import com.essence.dao.entity.ReaFocusGeom;
import com.essence.euauth.common.util.UuidUtil;
import com.essence.interfaces.api.ReaFocusGeomService;
import com.essence.interfaces.model.ReaFocusGeomEsr;
import com.essence.interfaces.model.ReaFocusGeomEsu;
import com.essence.interfaces.param.ReaFocusGeomEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterReaFocusGeomEtoT;
import com.essence.service.converter.ConverterReaFocusGeomTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author zhy
 * @since 2022/5/13 15:05
 */
@Service
public class ReaFocusGeomServiceImpl extends BaseApiImpl<ReaFocusGeomEsu, ReaFocusGeomEsp, ReaFocusGeomEsr, ReaFocusGeom> implements ReaFocusGeomService {

    @Autowired
    private ReaFocusGeomDao reaFocusGeomDao;
    @Autowired
    private ConverterReaFocusGeomEtoT converterReaFocusGeomEtoT;
    @Autowired
    private ConverterReaFocusGeomTtoR converterReaFocusGeomTtoR;


    public ReaFocusGeomServiceImpl(ReaFocusGeomDao reaFocusGeomDao, ConverterReaFocusGeomEtoT converterReaFocusGeomEtoT, ConverterReaFocusGeomTtoR converterReaFocusGeomTtoR) {
        super(reaFocusGeomDao, converterReaFocusGeomEtoT, converterReaFocusGeomTtoR);
    }

    @Override
    public int insert(ReaFocusGeomEsu reaFocusGeomEsu) {
        reaFocusGeomEsu.setId(UuidUtil.get32UUIDStr());
        return super.insert(reaFocusGeomEsu);
    }

    @Override
    public List<ReaFocusGeomEsr> findByReaId(String reaId) {

        return converterReaFocusGeomTtoR.toList(reaFocusGeomDao.mfindByReaId(reaId));
    }
}
