package com.essence.service.utils;

import com.essence.common.constant.ItemConstant;
import com.essence.framework.util.DateUtil;
import com.essence.interfaces.model.RosteringInfoEsr;
import com.essence.interfaces.model.WorkorderBaseEsu;

import java.util.Date;

/**
 * @author zhy
 * @since 2022/10/31 16:39
 */
public class ExtractOrder {

    public static WorkorderBaseEsu xhOrderFromRostering(RosteringInfoEsr rosteringInfoEsr, String id, Date startTime, Date endTime, Integer timeSpan) {
        if (null == rosteringInfoEsr) {
            return null;
        }
        WorkorderBaseEsu workorderBaseEsu = new WorkorderBaseEsu();
        /**
         * 主键
         * @mock 1
         */
        workorderBaseEsu.setId(id);

        /**
         * 处理人id
         * @mock 1
         */

        workorderBaseEsu.setPersonId(rosteringInfoEsr.getPersonId());
        /**
         * 处理人名称
         * @mock 张三
         */

        workorderBaseEsu.setPersonName(rosteringInfoEsr.getPersonName());
        /**
         * 工单负责人
         * @mock 李四
         */

        workorderBaseEsu.setOrderManager(rosteringInfoEsr.getPersonName());
        /**
         * 工单类型(1巡查 2 保洁 3 绿化 4维保 5运行)
         * @mock 1
         */

        workorderBaseEsu.setOrderType(ItemConstant.ORDER_TYPE_XC);
        /**
         * 工单编号（此处不设置 新增会自动设置）
         * @mock 20221028XC001
         */
        /**
         * 工单来源(1 计划生成 2 巡查上报)
         * @mock 2
         */

        workorderBaseEsu.setOrderSource(ItemConstant.ORDER_SOURCE_JH);
        /**
         * 工单名称 日期+河系+重点位置+巡河工单
         * @mock 巡河工单一
         */
        // 日期
        String yyyyMMdd = DateUtil.dateToStringWithFormat(new Date(), "yyyyMMdd");
        String tname = rosteringInfoEsr.getReaName();
        String name = yyyyMMdd + rosteringInfoEsr.getFocusPosition() + ItemConstant.ORDER_NAME_SUFFIX_XC;
        workorderBaseEsu.setOrderName(name);


        /**
         * 派发方式(1人工派发 2自动派发)
         * @mock 1
         */
        workorderBaseEsu.setDistributeType(ItemConstant.ORDER_DISRRIBUTE_ZD);

        /**
         * 作业区域
         * @mock 河道一
         */
        workorderBaseEsu.setLocation(rosteringInfoEsr.getFocusPosition());

        /**
         * 所属单位主键
         * @mock 1
         */

        workorderBaseEsu.setUnitId(rosteringInfoEsr.getUnitId());
        /**
         * 所属单位名称
         * @mock 河道管理一所
         */
        workorderBaseEsu.setUnitName(rosteringInfoEsr.getUnitName());
        /**
         * 创建时间
         * @mock 2022-10-28 9:59:00
         */
        workorderBaseEsu.setStartTime(startTime);

        /**
         * 处理时段（以分钟为单位）
         * @mock 600
         */
        workorderBaseEsu.setTimeSpan(timeSpan);

        /**
         * 截止时间
         * @mock 2022-10-28 19:59:00
         */
        workorderBaseEsu.setEndTime(endTime);
        /**
         * 工单标签（1市级交办 2区级交办 3局交办）
         * orderLabel
         */

        /**
         * 工单描述
         * orderDesc
         */
        /**
         * 新增时间
         * @ignore
         */
        workorderBaseEsu.setGmtCreate(new Date());
        /**
         * 更新时间
         * @ignore
         */
        workorderBaseEsu.setGmtModified(new Date());

        /**
         * 创建者
         * @ignore
         */
        workorderBaseEsu.setCreator("-1");
        /**
         * 更新者
         * @ignore
         */
        workorderBaseEsu.setUpdater("-1");


        return workorderBaseEsu;
    }
}
