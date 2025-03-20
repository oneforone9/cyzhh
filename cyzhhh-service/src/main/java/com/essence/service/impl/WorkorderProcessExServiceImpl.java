package com.essence.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.essence.dao.WorkorderBaseDao;
import com.essence.dao.WorkorderProcessDao;
import com.essence.dao.WorkorderProcessExDao;
import com.essence.dao.entity.WorkorderBase;
import com.essence.dao.entity.WorkorderProcess;
import com.essence.dao.entity.WorkorderProcessEx;
import com.essence.interfaces.api.WorkorderProcessExService;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.WorkorderProcessEsr;
import com.essence.interfaces.model.WorkorderProcessExEsr;
import com.essence.interfaces.model.WorkorderProcessExEsu;
import com.essence.interfaces.param.WorkorderProcessExEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterWorkorderProcessExEtoT;
import com.essence.service.converter.ConverterWorkorderProcessExTtoR;
import com.essence.service.converter.ConverterWorkorderProcessTtoR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @author zhy
 * @since 2022/5/13 15:05
 */
@Service
public class WorkorderProcessExServiceImpl extends BaseApiImpl<WorkorderProcessExEsu, WorkorderProcessExEsp, WorkorderProcessExEsr, WorkorderProcessEx> implements WorkorderProcessExService {

    @Autowired
    private WorkorderProcessExDao workorderProcessExDao;
    @Autowired
    private ConverterWorkorderProcessExEtoT converterWorkorderProcessExEtoT;
    @Autowired
    private ConverterWorkorderProcessExTtoR converterWorkorderProcessExTtoR;

    @Autowired
    private WorkorderBaseDao workorderBaseDao;
    @Autowired
    private WorkorderProcessDao workorderProcessDao;
    @Autowired
    private ConverterWorkorderProcessTtoR converterWorkorderProcessTtoR;

    public WorkorderProcessExServiceImpl(WorkorderProcessExDao workorderProcessExDao, ConverterWorkorderProcessExEtoT converterWorkorderProcessExEtoT, ConverterWorkorderProcessExTtoR converterWorkorderProcessExTtoR) {
        super(workorderProcessExDao, converterWorkorderProcessExEtoT, converterWorkorderProcessExTtoR);
    }

    //重写取消视图查询太慢
    @Override
    public Paginator<WorkorderProcessExEsr> findByPaginator(PaginatorParam param) {
        Date date = new Date();
        DateTime begin = DateUtil.beginOfDay(date);
        DateTime end = DateUtil.endOfDay(date);
        //查询所有工单
        List<WorkorderBase> workorderBases = workorderBaseDao.selectList(new QueryWrapper<>());
        Map<String, String> orderNameMap = new HashMap<>();
        if (CollUtil.isNotEmpty(workorderBases)){
            orderNameMap = workorderBases.parallelStream().collect(Collectors.toMap(WorkorderBase::getId, WorkorderBase::getOrderName, (o1, o2) -> o2));
        }
        //查询所有的工单过程
        //查询当天的吧
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.between("order_time",begin,end);
        List<WorkorderProcess> workorderProcesses = workorderProcessDao.selectList(queryWrapper);
        List<WorkorderProcessEsr> workorderProcessEsrs = converterWorkorderProcessTtoR.toList(workorderProcesses);
        List<WorkorderProcessExEsr> res = new ArrayList<>();
        for (WorkorderProcessEsr workorderProcessEsr : workorderProcessEsrs) {
            WorkorderProcessExEsr workorderProcessExEsr = new WorkorderProcessExEsr();
            workorderProcessExEsr.setId(workorderProcessEsr.getId());
            workorderProcessExEsr.setOrderId(workorderProcessEsr.getOrderId());
            workorderProcessExEsr.setPersonId(workorderProcessEsr.getPersonId());
            workorderProcessExEsr.setPersonName(workorderProcessEsr.getPersonName());
            workorderProcessExEsr.setOrderStatus(workorderProcessEsr.getOrderStatus());

            workorderProcessExEsr.setOperation(workorderProcessEsr.getOperation());
            workorderProcessExEsr.setOrderTime(workorderProcessEsr.getOrderTime());
            String s = orderNameMap.get(workorderProcessEsr.getOrderId());
            workorderProcessExEsr.setOrderName(StrUtil.isNotEmpty(s) ? s : "");
            res.add(workorderProcessExEsr);
        }
        res = res.parallelStream().sorted(Comparator.comparing(WorkorderProcessExEsr::getOrderTime).reversed()).collect(Collectors.toList());
        Paginator<WorkorderProcessExEsr> paginator = new Paginator<>(res,param.getCurrentPage(),param.getCurrentPage());
        return paginator;

    }

}
