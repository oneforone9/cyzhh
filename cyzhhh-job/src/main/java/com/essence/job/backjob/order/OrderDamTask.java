package com.essence.job.backjob.order;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.essence.common.constant.ItemConstant;
import com.essence.common.utils.TimeUtil;
import com.essence.dao.StPlanRecordDao;
import com.essence.dao.TWorkorderProcessNewestDao;
import com.essence.euauth.common.util.UuidUtil;
import com.essence.framework.util.DateUtil;
import com.essence.interfaces.api.*;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.*;
import com.essence.job.validate.CronJobIdentifierProvider;
import com.essence.service.utils.OrderCodeGenerateUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.essence.common.constant.ItemConstant.DEV;
import static com.essence.common.constant.JobConstant.BACK_END;

/**
 * 创建闸坝运行养护工作计划工单
 *
 * @author zhy
 * @since 2022/10/31 16:07
 */
@Component
@Log4j2
public class OrderDamTask {
    @Autowired
    private WorkorderBaseService workorderBaseService;
    @Autowired
    private WorkorderProcessService workorderProcessService;
    @Autowired
    private StPlanInfoService stPlanInfoService;
    @Autowired
    private TWorkorderFlowService tWorkorderFlowService;
    @Autowired
    private StPlanRecordService stPlanRecordService;
    @Autowired
    private StPlanRecordDao stPlanRecordDao;
    @Autowired
    private CodeGenerateService codeGenerateService;
    @Autowired
    private TWorkorderProcessNewestDao tWorkorderProcessNewestDao;
    @Autowired
    private TWorkorderProcessNewestService tWorkorderProcessNewestService;
    @Autowired
    private StOpenBaseService stOpenBaseService;

    @Autowired
    private CronJobIdentifierProvider crdJobIdentifierProvider;

    @Value("${spring.profiles.active}")
    private String env;

    @Transactional
    @Scheduled(cron = "0 0/12 5,6,7 * * ?")
    public void syncCreateOrderDamTask() {
        if (DEV.equals(env)) {
            return;
        }
        if (!BACK_END.equals(crdJobIdentifierProvider.getCronJobIdentifier())) {
            log.debug("只有{}环境才执行order-创建闸坝运行养护工作计划工单任务,结束.", BACK_END);
            return;
        }
        log.info("创建闸坝运行养护工作计划工单....开始" + new Date());

        // 1 获取闸坝运行养护工作计划表
        PaginatorParam param = new PaginatorParam();
        param.setCurrentPage(0);
        param.setPageSize(0);
        Paginator<StPlanInfoEsr> byPaginator = stPlanInfoService.findByPaginator(param);
        List<StPlanInfoEsr> items = byPaginator.getItems();

        if (CollectionUtils.isEmpty(items)) {
            log.info("创建闸坝运行养护工作计划工单终止-闸坝运行养护工作计划表");
            return;
        }
        for (int i = 0; i < items.size(); i++) {
            StPlanInfoEsr stPlanInfoEsr = items.get(i);
            String whTime = stPlanInfoEsr.getWhTime();
            //维护日期类型 1-年季度月  2-月/周
            String type = stPlanInfoEsr.getType();
            Map<String, Object> whTimeMap = new HashMap<>();

            //去除{}
            String s1 = whTime.replace("{", "");
            String s2 = s1.replace("}", "");
            String s3 = s2.trim();

            //1.根据逗号分隔
            String[] split = s3.split(",");

            for (int q = split.length - 1; q >= 0; q--) {
                String trim = split[q].trim();
                String[] split1 = trim.split("=");
                whTimeMap.put(split1[0], split1[1]);

                //去判断 闸坝计划生成记录表查询当年是否已生成工单
                String year = TimeUtil.getYear(new Date());
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("plan_id", stPlanInfoEsr.getId());
                queryWrapper.eq("wh_time", trim);
                queryWrapper.like("gmt_create", year);
                List list = stPlanRecordDao.selectList(queryWrapper);

                if (list.size() == 0) {
                    //未生成，则生成并增加闸坝计划生成记录
                    //先生成维保工单
                    // 开始时间定为每日6点
                    Date startTime = null;
                    Date endTime = null;
                    String mouthKey = split1[0];
                    String mouthEndKey = split1[1];
                    String mouthFlag = null;
                    //按月的
                    if ("1".equals(type)) {

                        DateTime dateTime = cn.hutool.core.date.DateUtil.parse(year + "-" + mouthKey, "yyyy-MM");
                        DateTime dateTimeEnd = cn.hutool.core.date.DateUtil.parse(year + "-" + mouthEndKey, "yyyy-MM");
                        startTime = cn.hutool.core.date.DateUtil.beginOfMonth(dateTime);
                        endTime = cn.hutool.core.date.DateUtil.endOfMonth(dateTimeEnd);
                        mouthFlag = TimeUtil.getMouth(new Date());

                    } else if ("2".equals(type)) {
                        //按周的
                        DateTime dateTime = cn.hutool.core.date.DateUtil.parse(year + mouthKey, "yyyyMMdd");
                        DateTime dateTimeEnd = cn.hutool.core.date.DateUtil.parse(year + mouthEndKey, "yyyyMMdd");
                        startTime = cn.hutool.core.date.DateUtil.beginOfDay(dateTime);
                        endTime = cn.hutool.core.date.DateUtil.endOfDay(dateTimeEnd);
                        mouthFlag = TimeUtil.getMouthDay(new Date());

                    }
                    // 处理时长
                    String s = cn.hutool.core.date.DateUtil.between(startTime, endTime, DateUnit.MINUTE) + " 分钟";
                    Integer timeSpan = Integer.valueOf(s.trim().replace(" 分钟", ""));
                    //根据排班计划的时间判断是否需要生成闸坝养护计划工单
//                    if(mouthFlag.equals(mouthKey) || mouthFlag.compareTo(mouthKey)>=0){
//                        createrWBOrder(stPlanInfoEsr, startTime, endTime, timeSpan, trim);
//                    }

                    Date date = new Date();
                    log.info(date.compareTo(startTime));
                    log.info(date.compareTo(endTime));
                    if (date.compareTo(startTime) > 0 && date.compareTo(endTime) < 0) {
                        createrWBOrder(stPlanInfoEsr, startTime, endTime, timeSpan, trim);
                    }


                } else {
                    //已生成，则不在进行生成
                    log.info(stPlanInfoEsr.getId() + stPlanInfoEsr.getStnm() + "-" + stPlanInfoEsr.getEquipmentName() + "-" + trim + "已生成过，不需要进行生成");
                }
            }
        }
        log.info("创建闸坝运行养护工作计划工单.....结束" + new Date());
    }


    private void createrWBOrder(StPlanInfoEsr stPlanInfoEsr, Date startTime, Date endTime, Integer timeSpan, String trim) {
        //1 组装维保工单的数据
        WorkorderBaseEsu workorderBaseEsu = new WorkorderBaseEsu();
        workorderBaseEsu.setId(UuidUtil.get32UUIDStr());
        workorderBaseEsu.setPersonId(stPlanInfoEsr.getUserId()); //处理人id
        workorderBaseEsu.setPersonName(stPlanInfoEsr.getName()); //处理人名称
        workorderBaseEsu.setOrderManager(stPlanInfoEsr.getName()); //工单负责人
        workorderBaseEsu.setOrderType(ItemConstant.ORDER_TYPE_YH); //工单类型(1巡查 2 保洁 3 绿化 4维保 5运行 6养护)
        workorderBaseEsu.setOrderSource(ItemConstant.ORDER_SOURCE_JH); //工单来源(1 计划生成 2 巡查上报)
        // 工单名称 日期+河系+设备设施名称+维保工单
        String yyyyMMdd = DateUtil.dateToStringWithFormat(new Date(), "yyyyMMdd");
        String name = yyyyMMdd + stPlanInfoEsr.getStnm() + ItemConstant.ORDER_NAME_SUFFIX_YH;
        workorderBaseEsu.setOrderName(name);
        workorderBaseEsu.setDistributeType(ItemConstant.ORDER_DISRRIBUTE_ZD); //派发方式(1人工派发 2自动派发)
        workorderBaseEsu.setUnitId(stPlanInfoEsr.getUnitId()); //所属单位主键
        workorderBaseEsu.setUnitName(stPlanInfoEsr.getUnitName()); //所属单位名称
        workorderBaseEsu.setStartTime(startTime); //创建时间
        workorderBaseEsu.setTimeSpan(timeSpan); //处理时段（以分钟为单位）
        workorderBaseEsu.setEndTime(endTime); //截止时间
        // 2）是否关注 默认不关注
        workorderBaseEsu.setIsAttention(ItemConstant.ORDER_NO_ATTENTION);
        // 3) 是否删除 默认不删除
        workorderBaseEsu.setIsDelete(ItemConstant.ORDER_NO_DELETE);

        // 4) 工单编码如果为空自动生成工单编码(日期 +拼音缩写 +顺序码)
        if (StringUtils.isEmpty(workorderBaseEsu.getOrderCode())) {
            // 日期
            String yyyyMMdd2 = DateUtil.dateToStringWithFormat(new Date(), "yyyyMMdd");
            // 拼音缩写
            String type = OrderCodeGenerateUtil.renPing(workorderBaseEsu.getOrderType());
            // 编码
            List<String> codes = codeGenerateService.getCode(yyyyMMdd2 + type, 1);
            workorderBaseEsu.setOrderCode(codes.get(0));
        }
        workorderBaseEsu.setGmtCreate(new Date()); //新增时间
        workorderBaseEsu.setGmtModified(new Date()); //更新时间
        workorderBaseEsu.setCreator("-1"); //创建者
        workorderBaseEsu.setUpdater("-1"); //更新者
        workorderBaseEsu.setUnitId(stPlanInfoEsr.getUnitId());
        workorderBaseEsu.setUnitName(stPlanInfoEsr.getUnitName());
        workorderBaseEsu.setSendTo(stPlanInfoEsr.getCompany());
        workorderBaseEsu.setCompany(stPlanInfoEsr.getCompany());
        workorderBaseEsu.setCompanyId(stPlanInfoEsr.getCompanyId());
        workorderBaseEsu.setRealId(stPlanInfoEsr.getRiverId().toString());
        workorderBaseEsu.setRealName(stPlanInfoEsr.getRiverName());
        workorderBaseEsu.setSttp(stPlanInfoEsr.getSttp());
        workorderBaseEsu.setEquipmentName(stPlanInfoEsr.getEquipmentName());
        workorderBaseEsu.setLgtd(stPlanInfoEsr.getLgtd());
        workorderBaseEsu.setLttd(stPlanInfoEsr.getLttd());
        workorderBaseEsu.setServiceContent(stPlanInfoEsr.getServiceContent());
        workorderBaseEsu.setStnm(stPlanInfoEsr.getStnm());

        // 2 工单入库
        workorderBaseService.insert(workorderBaseEsu);
        // 3 派发工单
        WorkorderProcessEsu workorderProcessEsu = new WorkorderProcessEsu();
        workorderProcessEsu.setOrderTime(DateUtil.getNextSecond(startTime, 1));
        workorderProcessEsu.setPersonId(workorderBaseEsu.getPersonId());
        workorderProcessEsu.setPersonName(workorderBaseEsu.getPersonName());
        workorderProcessEsu.setOrderId(workorderBaseEsu.getId());
        workorderProcessEsu.setOrderStatus(ItemConstant.ORDER_STATUS_NO_START);
        workorderProcessService.insert(workorderProcessEsu);

        //20230802 保存最新的工单状态表
        //再处理工单流程的最新记录表只保留最新记录一条数据
        String orderId = workorderProcessEsu.getOrderId();
        //2删除orderId数据
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("order_id", orderId);
        int delete = tWorkorderProcessNewestDao.delete(wrapper);
        //3添加最新工单记录
        //组装数据
        TWorkorderProcessNewestEsu newestEsu = new TWorkorderProcessNewestEsu();
        BeanUtil.copyProperties(workorderProcessEsu, newestEsu);

        int insert1 = tWorkorderProcessNewestService.insert(newestEsu);
        //20230802增加工单的最新状态到工单最新记录状态表中

        //4 系统生成并派发给指定人
        TWorkorderFlowEsu tWorkorderFlowEsu = new TWorkorderFlowEsu();
        tWorkorderFlowEsu.setOrderId(workorderBaseEsu.getId());
        tWorkorderFlowService.addTWorkOrderFlows(tWorkorderFlowEsu);


        //5 再增加闸坝计划生成记录
        StPlanRecordEsu esu = new StPlanRecordEsu();
        esu.setPlanId(stPlanInfoEsr.getId());
        esu.setWhTime(trim);
        esu.setGmtCreate(new Date());
        int insert = stPlanRecordService.insert(esu);

        //20230828发送订阅消息通知
        // String s = stOpenBaseService.sendMsg(workorderBaseEsu);
        //20230828发送订阅消息通知

    }

}
