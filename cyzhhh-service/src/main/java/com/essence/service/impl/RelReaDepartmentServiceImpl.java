package com.essence.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.exception.BusinessException;
import com.essence.dao.RelReaDepartmentDao;
import com.essence.dao.entity.RelReaDepartment;
import com.essence.interfaces.api.ReaBaseService;
import com.essence.interfaces.api.RelReaDepartmentService;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.ReaBaseEsr;
import com.essence.interfaces.model.RelReaDepartmentEsr;
import com.essence.interfaces.model.RelReaDepartmentEsu;
import com.essence.interfaces.param.RelReaDepartmentEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterRelReaDepartmentEtoT;
import com.essence.service.converter.ConverterRelReaDepartmentTtoR;
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
public class RelReaDepartmentServiceImpl extends BaseApiImpl<RelReaDepartmentEsu, RelReaDepartmentEsp, RelReaDepartmentEsr, RelReaDepartment> implements RelReaDepartmentService {

    @Autowired
    private RelReaDepartmentDao relReaDepartmentDao;
    @Autowired
    private ConverterRelReaDepartmentEtoT converterRelReaDepartmentEtoT;
    @Autowired
    private ConverterRelReaDepartmentTtoR converterRelReaDepartmentTtoR;
    @Autowired
    private ReaBaseService reaBaseService;

    public RelReaDepartmentServiceImpl(RelReaDepartmentDao relReaDepartmentDao, ConverterRelReaDepartmentEtoT converterRelReaDepartmentEtoT, ConverterRelReaDepartmentTtoR converterRelReaDepartmentTtoR) {
        super(relReaDepartmentDao, converterRelReaDepartmentEtoT, converterRelReaDepartmentTtoR);
    }

    @Override
    public int insert(RelReaDepartmentEsu relReaDepartmentEsu) {
        int count = countByReaId(relReaDepartmentEsu.getReaId());
        if (count > 0) {
            throw new BusinessException("河道只可添加一个班组，不可重复添加");
        }

        return super.insert(relReaDepartmentEsu);
    }

    public int countByReaId(String reaId) {
        QueryWrapper<RelReaDepartment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("rea_id", reaId);
        return relReaDepartmentDao.selectCount(queryWrapper);
    }

    @Override
    public int delete(RelReaDepartmentEsu relReaDepartmentEsu) {
        QueryWrapper<RelReaDepartment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("rea_id", relReaDepartmentEsu.getReaId());
        queryWrapper.eq("department_id", relReaDepartmentEsu.getDepartmentId());
        return relReaDepartmentDao.delete(queryWrapper);
    }

    @Override
    public int update(RelReaDepartmentEsu relReaDepartmentEsu) {
        return -1;
    }

    @Override
    public int deleteById(Serializable id) {
        return -1;
    }

    @Override
    public RelReaDepartmentEsr findById(Serializable id) {
        return null;
    }


    @Override
    public List<RelReaDepartmentEsr> findBycondition(PaginatorParam param) {
        param.setPageSize(0);
        param.setCurrentPage(0);
        Paginator<RelReaDepartmentEsr> paginator = this.findByPaginator(param);
        List<RelReaDepartmentEsr> items = paginator.getItems();
        if (CollectionUtils.isEmpty(items)) {
            return null;
        }
        // 查询一级河道
        List<ReaBaseEsr> reaBaseEsrs = reaBaseService.searchLevel1();
        if (CollectionUtils.isEmpty(reaBaseEsrs)) {
            return null;
        }
        Map<String, String> reaMap = reaBaseEsrs.stream().filter(p -> !StringUtils.isEmpty(p.getReaName())).collect(Collectors.toMap(ReaBaseEsr::getId, ReaBaseEsr::getReaName));

        Iterator<RelReaDepartmentEsr> iterator = items.iterator();
        while (iterator.hasNext()) {
            RelReaDepartmentEsr item = iterator.next();
            String reaName = reaMap.get(item.getReaId());
            if (StringUtils.isEmpty(reaName)) {
                iterator.remove();
            } else {
                item.setReaName(reaName);
            }
        }

        return items;
    }
}
