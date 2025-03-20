package com.essence.service.utils;

import com.essence.interfaces.model.WorkorderBaseEsu;
import com.essence.interfaces.model.WorkorderProcessEsu;

/**
 * @author zhy
 * @since 2022/10/28 9:39
 */
public class ExtractProcess {
    public static WorkorderProcessEsu fromOrderBase(WorkorderBaseEsu workorderBaseEsu, String processId, String orderStatus) {
        if (null == workorderBaseEsu){
            return null;
        }
        WorkorderProcessEsu workorderProcessEsu = new WorkorderProcessEsu();

        workorderProcessEsu.setId(processId);

        workorderProcessEsu.setOrderId(workorderBaseEsu.getId());

        workorderProcessEsu.setOrderStatus(orderStatus);

        workorderProcessEsu.setOrderTime(workorderBaseEsu.getStartTime());

        workorderProcessEsu.setPersonId(workorderBaseEsu.getPersonId());

        workorderProcessEsu.setPersonName(workorderBaseEsu.getPersonName());

        return workorderProcessEsu;
    }
}
