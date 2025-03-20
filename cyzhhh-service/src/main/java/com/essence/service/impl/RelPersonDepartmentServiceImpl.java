package com.essence.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.exception.BusinessException;
import com.essence.dao.RelPersonDepartmentDao;
import com.essence.dao.entity.RelPersonDepartment;
import com.essence.interfaces.api.PersonBaseService;
import com.essence.interfaces.api.RelPersonDepartmentService;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.PersonBaseEsr;
import com.essence.interfaces.model.RelPersonDepartmentEsr;
import com.essence.interfaces.model.RelPersonDepartmentEsu;
import com.essence.interfaces.param.RelPersonDepartmentEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterRelPersonDepartmentEtoT;
import com.essence.service.converter.ConverterRelPersonDepartmentTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author zhy
 * @since 2022/5/13 15:05
 */
@Service
public class RelPersonDepartmentServiceImpl extends BaseApiImpl<RelPersonDepartmentEsu, RelPersonDepartmentEsp, RelPersonDepartmentEsr, RelPersonDepartment> implements RelPersonDepartmentService {

    @Autowired
    private RelPersonDepartmentDao relPersonDepartmentDao;
    @Autowired
    private ConverterRelPersonDepartmentEtoT converterRelPersonDepartmentEtoT;
    @Autowired
    private ConverterRelPersonDepartmentTtoR converterRelPersonDepartmentTtoR;
    @Autowired
    private PersonBaseService personBaseService;

    public RelPersonDepartmentServiceImpl(RelPersonDepartmentDao relPersonDepartmentDao, ConverterRelPersonDepartmentEtoT converterRelPersonDepartmentEtoT, ConverterRelPersonDepartmentTtoR converterRelPersonDepartmentTtoR) {
        super(relPersonDepartmentDao, converterRelPersonDepartmentEtoT, converterRelPersonDepartmentTtoR);
    }

    @Override
    public int update(RelPersonDepartmentEsu relPersonDepartmentEsu) {
        return -1;
    }

    @Override
    public int deleteById(Serializable id) {
        return -1;
    }

    @Override
    public RelPersonDepartmentEsr findById(Serializable id) {
        return null;
    }

    @Override
    public int insert(RelPersonDepartmentEsu relPersonDepartmentEsu) {
        int count = this.count(relPersonDepartmentEsu);
        if (count > 0) {
            throw new BusinessException("配置关系已存在，不可重复添加");
        }
        return super.insert(relPersonDepartmentEsu);
    }


    private int count(RelPersonDepartmentEsu relPersonDepartmentEsu) {
        QueryWrapper<RelPersonDepartment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("person_id", relPersonDepartmentEsu.getPersonId());
        queryWrapper.eq("department_id", relPersonDepartmentEsu.getDepartmentId());
        return relPersonDepartmentDao.selectCount(queryWrapper);
    }

    @Override
    public int delete(RelPersonDepartmentEsu relPersonDepartmentEsu) {
        QueryWrapper<RelPersonDepartment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("person_id", relPersonDepartmentEsu.getPersonId());
        queryWrapper.eq("department_id", relPersonDepartmentEsu.getDepartmentId());
        return relPersonDepartmentDao.delete(queryWrapper);
    }

    @Override
    public List<RelPersonDepartmentEsr> findBycondition(PaginatorParam param) {
        param.setCurrentPage(0);
        param.setPageSize(0);
        Paginator<RelPersonDepartmentEsr> paginator = this.findByPaginator(param);
        List<RelPersonDepartmentEsr> items = paginator.getItems();
        if (CollectionUtils.isEmpty(items)) {
            return null;
        }
        // 查询内部启用人员
        List<PersonBaseEsr> personBaseEsrs = personBaseService.findInsideUsing();
        if (CollectionUtils.isEmpty(personBaseEsrs)) {
            return null;
        }
        Map<String, String> personMap = personBaseEsrs.stream().filter(p -> !StringUtils.isEmpty(p.getName())).collect(Collectors.toMap(PersonBaseEsr::getId, PersonBaseEsr::getName));

        Iterator<RelPersonDepartmentEsr> iterator = items.iterator();
        while (iterator.hasNext()) {
            RelPersonDepartmentEsr item = iterator.next();
            String personName = personMap.get(item.getPersonId());
            if (StringUtils.isEmpty(personName)) {
                iterator.remove();
            } else {
                item.setPersonName(personName);
            }
        }
        return items;
    }
}
