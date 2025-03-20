package com.essence.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUnit;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.constant.ItemConstant;
import com.essence.common.exception.BusinessException;
import com.essence.dao.*;
import com.essence.dao.entity.*;
import com.essence.euauth.common.util.UuidUtil;
import com.essence.interfaces.api.*;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.WorkorderProcessEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.ConverterWorkorderNewestTtoR;
import com.essence.service.converter.ConverterWorkorderProcessEtoT;
import com.essence.service.converter.ConverterWorkorderProcessTtoR;
import com.essence.service.converter.ConverterWorkorderTrackTtoR;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @author zhy
 * @since 2022/5/13 15:05
 */
@Service
public class WorkorderProcessServiceImpl extends BaseApiImpl<WorkorderProcessEsu, WorkorderProcessEsp, WorkorderProcessEsr, WorkorderProcess> implements WorkorderProcessService {

    @Autowired
    private ConverterWorkorderProcessEtoT converterWorkorderProcessEtoT;
    @Autowired
    private ConverterWorkorderProcessTtoR converterWorkorderProcessTtoR;
    @Autowired
    private ConverterWorkorderTrackTtoR converterWorkorderTrackTtoR;
    @Autowired
    private WorkorderTrackDao workorderTrackDao;
    @Autowired
    private FileBaseService fileBaseService;
    @Autowired
    private EventDepositService eventDepositService;
    @Autowired
    private WorkorderNewestDao workorderNewestDao;
    @Autowired
    private ConverterWorkorderNewestTtoR converterWorkorderNewestTtoR;
    @Autowired
    private WorkorderNewestService workorderNewestService;
    @Autowired
    private WorkorderBaseDao workorderBaseDao;
    @Autowired
    private WorkorderProcessDao workorderProcessDao;
    @Autowired
    private WorkorderProcessService workorderProcessService;
    @Autowired
    private TWorkorderProcessNewestService tWorkorderProcessNewestService;
    @Autowired
    private TWorkorderProcessNewestDao tWorkorderProcessNewestDao;


    public WorkorderProcessServiceImpl(WorkorderProcessDao workorderProcessDao, ConverterWorkorderProcessEtoT converterWorkorderProcessEtoT, ConverterWorkorderProcessTtoR converterWorkorderProcessTtoR) {
        super(workorderProcessDao, converterWorkorderProcessEtoT, converterWorkorderProcessTtoR);
    }

    @Transactional
    @Override
    public int insert(WorkorderProcessEsu workorderProcessEsu) {
        if (null == workorderProcessEsu.getId()) {
            workorderProcessEsu.setId(UuidUtil.get32UUIDStr());
        }
        // 处理时间
        if (null == workorderProcessEsu.getOrderTime()) {
            workorderProcessEsu.setOrderTime(new Date());
        }

        //终止操作
        String orderStatus = workorderProcessEsu.getOrderStatus(); //工单当前状态  6
        String orderId = workorderProcessEsu.getOrderId();
        WorkorderNewestEsr workorderNewestEsr = workorderNewestService.findById(orderId);
        if (workorderNewestEsr != null){
            String orderStatusNewest = workorderNewestEsr.getOrderStatus();//工单的最新状态
            if(ItemConstant.ORDER_STATUS_OVER.equals(orderStatus) && ItemConstant.ORDER_STATUS_OVER.equals(orderStatusNewest)){
                 //工单的最新状态为 未开始时可以操作
                throw new BusinessException("功单状态为已终止，不能重复操作");
            }
            if(ItemConstant.ORDER_STATUS_END.equals(orderStatus) && ItemConstant.ORDER_STATUS_END.equals(orderStatusNewest)){
                throw new BusinessException("功单状态为已归档，不能重复操作");
            }
            if(ItemConstant.ORDER_STATUS_STOP_AUDIT.equals(orderStatus) && ItemConstant.ORDER_STATUS_STOP_AUDIT.equals(orderStatusNewest)){
                throw new BusinessException("功单状态为已终止，不能重复操作");

            }
            if(ItemConstant.ORDER_STATUS_OVER.equals(orderStatus) && ItemConstant.ORDER_STATUS_STOP_AUDIT.equals(orderStatusNewest)){
                throw new BusinessException("功单状态为已终止，不能重复操作");
            }
            if(ItemConstant.ORDER_STATUS_EXAMINNING.equals(orderStatus) && ItemConstant.ORDER_STATUS_EXAMINNING.equals(orderStatusNewest)){
                throw new BusinessException("功单状态为待审核，不能重复操作");
            }

            if(ItemConstant.ORDER_STATUS_EXAMINNING.equals(orderStatus) && ItemConstant.ORDER_STATUS_STOP_AUDIT.equals(orderStatusNewest)){
                throw new BusinessException("功单状态为待审核，不能重复操作");
            }
            if(ItemConstant.ORDER_STATUS_STOP_AUDIT.equals(orderStatus) && ItemConstant.ORDER_STATUS_EXAMINNING.equals(orderStatusNewest)){
                throw new BusinessException("功单状态为待审核，不能重复操作");
            }

        }
        //20230615
        //4巡查完后完成工单进入待审核时，判断完成的时间和截止时间，判断处理时效 ：逾期 还是 按期
        if (workorderNewestEsr != null){
            Date dateNow = new Date();
            Date endTime = workorderNewestEsr.getEndTime();
            String orderType = workorderNewestEsr.getOrderType();
            if(ItemConstant.ORDER_STATUS_EXAMINNING.equals(orderStatus)){
                //非巡查的功能
                if( ! ItemConstant.ORDER_TYPE_XC.equals(orderType)){
                    WorkorderBase workorderBase =new WorkorderBase();
                    //int res = date1.compareTo(date2)，相等则返回0，date1大返回1，否则返回-1。
                    //当前系统时间完成工单的时间 大于等于 截止时间
                    if (dateNow.compareTo(endTime) > 0) {
                        //逾期
                        workorderBase.setDealEffect("逾期");
                    } else {
                        //按期
                        workorderBase.setDealEffect("按期");
                    }
                    workorderBase.setId(workorderProcessEsu.getOrderId());
                    workorderBaseDao.updateById(workorderBase);
                    //更新公单的处理时效
                }
            }
        }

        // 新增
        int insert = super.insert(workorderProcessEsu);
        // 如果是待审核状态将小程序上传的事件更新到事件表中
       /* if (ItemConstant.ORDER_STATUS_EXAMINNING.equals(workorderProcessEsu.getOrderStatus())) {
            eventDepositService.updateEventFromDepositByOrderId(workorderProcessEsu.getOrderId());
        }*/

        // 如果传入图片信息关联图片
        List<String> fileIds = workorderProcessEsu.getFileIds();
        if (CollectionUtils.isEmpty(fileIds)) {
            return insert;
        }
        fileIds.forEach(p -> {
            FileBaseEsu fileBaseEsu = new FileBaseEsu();
            fileBaseEsu.setId(p);
            fileBaseEsu.setTypeId(ItemConstant.ORDER_FILE_PREFIX + workorderProcessEsu.getEventId());
            fileBaseEsu.setClassId(ItemConstant.ORDER_FILE_PREFIX + workorderProcessEsu.getOrderId());
            fileBaseEsu.setBusinessId(ItemConstant.ORDER_FILE_PREFIX + workorderProcessEsu.getId());
            fileBaseService.update(fileBaseEsu);
        });

        return insert;
    }

    //查询工单
    @Override
    public List<WorkorderNewestEsr> selectWorkorderProcessList() {
        List<WorkorderNewestEsr> workorderNewestEsrList = new ArrayList<>();
        List<WorkorderNewest> workorderNewestList = Optional.ofNullable(workorderNewestDao.selectList(new QueryWrapper<WorkorderNewest>().lambda().eq(WorkorderNewest::getOrderStatus, ItemConstant.ORDER_STATUS_RUNNING))).orElse(Lists.newArrayList());
        if (null != workorderNewestList && workorderNewestList.size() > 0) {
            workorderNewestEsrList = converterWorkorderNewestTtoR.toList(workorderNewestList);
        }
        return workorderNewestEsrList;
    }

    //查询工单轨迹
    @Override
    public List<WorkorderTrackEsr> selectWorkorderProcessTrack(List<String> collect) {
        List<WorkorderTrackEsr> workorderProcessEsrList = new ArrayList<>();
        List<WorkorderTrack>workorderTrackList = new ArrayList<>();
        List<WorkorderTrack> workOrderTrackList = Optional.ofNullable(workorderTrackDao.selectList(new QueryWrapper<WorkorderTrack>().lambda().in(WorkorderTrack::getOrderId, collect))).orElse(Lists.newArrayList());
        Comparator<WorkorderTrack> comparator = Comparator.comparing(WorkorderTrack::getGmtCreate).reversed();
        for (String sId : collect) {
            List<WorkorderTrack> collect1 = workOrderTrackList.stream().filter(x -> x.getOrderId().equals(sId)).collect(Collectors.toList());
            if (null !=collect1 &&collect1.size()>0){
                workorderTrackList.add( collect1.stream().sorted(comparator).collect(Collectors.toList()).get(0));
            }
        }
        if (null != workorderTrackList && workorderTrackList.size() > 0) {
            System.out.println(workorderTrackList.toString());
            workorderProcessEsrList = converterWorkorderTrackTtoR.toList(workorderTrackList);
        }
        return workorderProcessEsrList;
    }

    @Override
    public Boolean findByPersonIdAndWorkOrderStatus(String userId, String orderStatusRunning, String orderId) {
        List<WorkorderNewest> workorderNewestList = workorderNewestDao.selectList(new QueryWrapper<WorkorderNewest>().lambda()
                .eq(WorkorderNewest::getOrderStatus, orderStatusRunning)
                .eq(WorkorderNewest::getPersonId, userId));
        if (CollUtil.isNotEmpty(workorderNewestList) && 1 == workorderNewestList.size()){
            WorkorderNewest workorderNewest = workorderNewestList.get(0);
            String id = workorderNewest.getId();
            if (orderId.equals(id)) {
                // 同一个工单 则通过
                return false;
            }else {
                return true;
            }
        }
        if (CollUtil.isNotEmpty(workorderNewestList)){
            // 标识查询到数据
            return true;
        }else {
            return false;
        }
    }

    /**
     * 将工单的状态表拆分成所有流程表和最近工单状态表
     * @param e
     * @return
     */
    @Transactional
    @Override
    public int insertDeal(WorkorderProcessEsu e) {
        //1先添加到工单的全部流程表中
        int insert = workorderProcessService.insert(e);

        //再处理工单流程的最新记录表只保留最新记录一条数据
        String orderId = e.getOrderId();
        //2删除orderId数据
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("order_id",orderId);
        int delete = tWorkorderProcessNewestDao.delete(wrapper);
        //3添加最新工单记录
        //组装数据
        TWorkorderProcessNewestEsu esu = new TWorkorderProcessNewestEsu();
        BeanUtil.copyProperties(e, esu);

        int insert1 = tWorkorderProcessNewestService.insert(esu);

        return insert;
    }

    @Override
    public Paginator<WorkorderProcessEsr> findByPaginator(PaginatorParam param){
        Paginator<WorkorderProcessEsr> byPaginator = super.findByPaginator(param);
        if (byPaginator !=null && CollUtil.isNotEmpty(byPaginator.getItems()) ){
            List<WorkorderProcessEsr> items = byPaginator.getItems();
            WorkorderProcessEsr workorderProcessEsr = items.get(0);
            WorkorderProcessEsr workorderProcessEsr1 = items.get(items.size() - 1);
            if (workorderProcessEsr1.getOrderTime() !=null && workorderProcessEsr.getOrderTime()!= null){
                Date in = workorderProcessEsr.getOrderTime();
                Date out = workorderProcessEsr1.getOrderTime();
                //获取不同单位下的时间差
                Long day = cn.hutool.core.date.DateUtil.between(in, out, DateUnit.DAY);
                Long hour = cn.hutool.core.date.DateUtil.between(in, out, DateUnit.HOUR)-day*24;
                Long minute = cn.hutool.core.date.DateUtil.between(in, out, DateUnit.MINUTE)-day*24*60;
                String dataStr = "";
                if (hour != 0 && minute != 0){
                    dataStr = hour+"小时"+minute+"分钟";
                } else if (hour== 0) {
                    dataStr = minute+"分钟";
                }
                byPaginator.getItems().get(byPaginator.getItems().size()-1).setDateStr(dataStr);
            }
        }
        return byPaginator;
    }
}
