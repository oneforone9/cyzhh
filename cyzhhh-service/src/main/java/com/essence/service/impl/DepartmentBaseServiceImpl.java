package com.essence.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.dao.DepartmentBaseDao;
import com.essence.dao.entity.DepartmentBase;
import com.essence.interfaces.api.DepartmentBaseService;
import com.essence.interfaces.model.DepartmentBaseEsr;
import com.essence.interfaces.model.DepartmentBaseEsu;
import com.essence.interfaces.param.DepartmentBaseEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterDepartmentBaseEtoT;
import com.essence.service.converter.ConverterDepartmentBaseTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author zhy
 * @since 2022/5/13 15:05
 */
@Service
public class DepartmentBaseServiceImpl extends BaseApiImpl<DepartmentBaseEsu, DepartmentBaseEsp, DepartmentBaseEsr, DepartmentBase> implements DepartmentBaseService {

    @Autowired
    private DepartmentBaseDao departmentBaseDao;
    @Autowired
    private ConverterDepartmentBaseEtoT converterDepartmentBaseEtoT;
    @Autowired
    private ConverterDepartmentBaseTtoR converterDepartmentBaseTtoR;


    public DepartmentBaseServiceImpl(DepartmentBaseDao departmentBaseDao, ConverterDepartmentBaseEtoT converterDepartmentBaseEtoT, ConverterDepartmentBaseTtoR converterDepartmentBaseTtoR) {
        super(departmentBaseDao, converterDepartmentBaseEtoT, converterDepartmentBaseTtoR);
    }


    @Override
    public List<DepartmentBaseEsr> findAll() {

        return converterDepartmentBaseTtoR.toList(departmentBaseDao.selectList(new QueryWrapper<>()));
    }

    /**
     * 设置巡河组长
     * @param departmentBaseEsu
     * @return
     */
    @Override
    public Integer setDepart(DepartmentBaseEsu departmentBaseEsu) {
        return departmentBaseDao.setDepart(departmentBaseEsu.getId(),departmentBaseEsu.getPersonBaseId(),departmentBaseEsu.getName());
    }
}
