package com.essence.service.impl;

import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.constant.ItemConstant;
import com.essence.dao.EventBaseDao;
import com.essence.dao.EventDepositDao;
import com.essence.dao.FileBaseDao;
import com.essence.dao.entity.EventDeposit;
import com.essence.euauth.common.util.UuidUtil;
import com.essence.framework.util.DateUtil;
import com.essence.interfaces.api.CodeGenerateService;
import com.essence.interfaces.api.EventBaseService;
import com.essence.interfaces.api.EventDepositService;
import com.essence.interfaces.api.FileBaseService;
import com.essence.interfaces.entity.Criterion;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.EventBaseEsp;
import com.essence.interfaces.param.EventDepositEsp;
import com.essence.service.baseimpl.BaseApiImpl;
import com.essence.service.converter.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author zhy
 * @since 2022/5/13 15:05
 */
@Service
public class EventDepositServiceImpl extends BaseApiImpl<EventDepositEsu, EventDepositEsp, EventDepositEsr, EventDeposit> implements EventDepositService {

    @Autowired
    private EventDepositDao eventDepositDao;
    @Autowired
    private ConverterEventDepositEtoT converterEventBaseEtoT;
    @Autowired
    private ConverterEventDepositTtoR converterEventBaseTtoR;
    @Autowired
    private ConverterEventDeposittoBase converterEventDeposittoBase;
    @Autowired
    private CodeGenerateService codeGenerateService;

    @Autowired
    private FileBaseService fileBaseService;
    @Autowired
    private EventBaseService eventBaseService;
    @Autowired
    private FileBaseDao fileBaseDao;

    public EventDepositServiceImpl(EventDepositDao eventDepositDao, ConverterEventDepositEtoT converterEventBaseEtoT, ConverterEventDepositTtoR converterEventBaseTtoR) {
        super(eventDepositDao, converterEventBaseEtoT, converterEventBaseTtoR);
    }

    @Transactional
    @Override
    public int insert(EventDepositEsu eventBaseEsu) {
        eventBaseEsu.setId(UuidUtil.get32UUIDStr());
        // 案件编号 (日期+案件分类编码+顺序码)
        if (StringUtils.isEmpty(eventBaseEsu.getEventCode())) {
            // 日期
            String yyyyMMdd = DateUtil.dateToStringWithFormat(new Date(), "yyyyMMdd");
            // 案件分类编码
            String eventClass = eventBaseEsu.getEventClass();
            // 编码
            List<String> codes = codeGenerateService.getCode(yyyyMMdd + eventClass, 1);
            eventBaseEsu.setEventCode(codes.get(0));
        }
        // 案件时间
        if (null == eventBaseEsu.getEventTime()) {
            eventBaseEsu.setEventTime(new Date());
        }
        // 未删除
        eventBaseEsu.setIsDelete(ItemConstant.EVENT_NO_DELETE);
        // 新增
        int insert = super.insert(eventBaseEsu);
        // 如果传入图片信息关联图片
        List<String> fileIds = eventBaseEsu.getFileIds();
        if (CollectionUtils.isEmpty(fileIds)) {
            return insert;
        }
        fileIds.forEach(p -> {
            FileBaseEsu fileBaseEsu = new FileBaseEsu();
            fileBaseEsu.setId(p);
            fileBaseEsu.setTypeId(ItemConstant.QUES_FILE_PREFIX + eventBaseEsu.getId());
            fileBaseService.update(fileBaseEsu);
        });
        return insert;
    }


    @Override
    public int deleteById(Serializable id) {
        EventDeposit eventBase = new EventDeposit();
        eventBase.setId((String) id);
        eventBase.setIsDelete(ItemConstant.EVENT_IS_DELETE);
        return eventDepositDao.updateById(eventBase);
    }

    @Override
    public EventDepositEsr findById(Serializable id) {
        EventDepositEsr eventBaseEsr = super.findById(id);
        if (null == eventBaseEsr) {
            return eventBaseEsr;
        }

        // 查询图片
        PaginatorParam param = new PaginatorParam();
        param.setPageSize(0);
        param.setCurrentPage(0);
        List currency = new ArrayList<>();
        Criterion criterion = new Criterion();
        criterion.setFieldName("typeId");
        criterion.setOperator(Criterion.IN);
        criterion.setValue(Arrays.asList(ItemConstant.ORDER_FILE_PREFIX + eventBaseEsr.getId(), ItemConstant.QUES_FILE_PREFIX + eventBaseEsr.getId()));
        currency.add(criterion);
        param.setCurrency(currency);
        Paginator<FileBaseEsr> paginator = fileBaseService.findByPaginator(param);
        List<FileBaseEsr> items = paginator.getItems();
        if (CollectionUtils.isEmpty(items)) {
            return eventBaseEsr;
        }

        Map<String, List<FileBaseEsr>> fileMap = items.stream().collect(Collectors.groupingBy(FileBaseEsr::getTypeId));
        eventBaseEsr.setBeforeFileList(fileMap.get(ItemConstant.QUES_FILE_PREFIX + eventBaseEsr.getId()));
        eventBaseEsr.setAfterFileList(fileMap.get(ItemConstant.ORDER_FILE_PREFIX + eventBaseEsr.getId()));

        return eventBaseEsr;
    }

    @Override
    public Paginator<EventDepositEsr> findByPaginator(PaginatorParam param) {
        Paginator<EventDepositEsr> paginator = super.findByPaginator(param);
        List<EventDepositEsr> items = paginator.getItems();
        if (CollectionUtils.isEmpty(items)) {
            return paginator;
        }

        // 获取关联建
        List<String> typeIds = items.stream().map(p -> {
            Map<String, String> map = new HashMap<>();
            map.put(ItemConstant.QUES_FILE_PREFIX, ItemConstant.QUES_FILE_PREFIX + p.getId());
            map.put(ItemConstant.ORDER_FILE_PREFIX, ItemConstant.ORDER_FILE_PREFIX + p.getId());
            return map;
        }).flatMap(m -> m.values().stream()).collect(Collectors.toList());
        // 查询图片
        PaginatorParam fileparam = new PaginatorParam();
        fileparam.setPageSize(0);
        fileparam.setCurrentPage(0);
        List currency = new ArrayList<>();
        Criterion criterion = new Criterion();
        criterion.setFieldName("typeId");
        criterion.setOperator(Criterion.IN);
        criterion.setValue(typeIds);
        currency.add(criterion);
        fileparam.setCurrency(currency);
        Paginator<FileBaseEsr> filePaginator = fileBaseService.findByPaginator(fileparam);
        List<FileBaseEsr> fileItems = filePaginator.getItems();
        if (CollectionUtils.isEmpty(fileItems)) {
            return paginator;
        }

        Map<String, List<FileBaseEsr>> fileMap = fileItems.stream().collect(Collectors.groupingBy(FileBaseEsr::getTypeId));
        items.forEach(p -> {
            p.setBeforeFileList(fileMap.get(ItemConstant.QUES_FILE_PREFIX + p.getId()));
            p.setAfterFileList(fileMap.get(ItemConstant.ORDER_FILE_PREFIX + p.getId()));
        });
        items = items.stream().sorted(Comparator.comparing(EventDepositEsr::getEventTime).reversed()).collect(Collectors.toList());
        paginator.setItems(items);
        return paginator;
    }

    @Transactional
    @Override
    public int updateEventFromDepositByOrderId(String orderId) {
        // 查询
        List<EventDeposit> eventDepositList = findByOrderId(orderId);
        if (CollectionUtils.isEmpty(eventDepositList)){
            return 0;
        }
        // 添加
        List<EventBaseEsu> eventBaseEsus = converterEventDeposittoBase.toList(eventDepositList);
        eventBaseEsus.forEach(
                p->{
                    eventBaseService.add(p);
                }
        );
        // 删除
        deleteByOrderId(orderId);
        return eventDepositList.size();
    }

    private List<EventDeposit> findByOrderId(String orderId){
        QueryWrapper<EventDeposit> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        queryWrapper.eq("is_delete", ItemConstant.EVENT_NO_DELETE);
        return eventDepositDao.selectList(queryWrapper);
    }
    private int deleteByOrderId(String orderId){
        QueryWrapper<EventDeposit> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        return eventDepositDao.delete(queryWrapper);
    }
}
