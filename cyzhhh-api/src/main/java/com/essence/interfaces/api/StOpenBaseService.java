package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.StOpenBaseEsr;
import com.essence.interfaces.model.StOpenBaseEsu;
import com.essence.interfaces.model.WorkorderBaseEsu;
import com.essence.interfaces.param.StOpenBaseEsp;

import javax.servlet.http.HttpServletRequest;

/**
 * 服务层
 * @author liwy
 * @since 2023-08-23 14:29:17
 */
public interface StOpenBaseService extends BaseApi<StOpenBaseEsu, StOpenBaseEsp, StOpenBaseEsr> {
    /**
     * 根据jsCode获取openId并绑定当前登录的用户
     * @param request
     * @param stOpenBaseEsu
     * @return
     */
    Integer getOpenId(HttpServletRequest request, StOpenBaseEsu stOpenBaseEsu) throws Exception;

    /**
     * 获取当前登录的用户openId。 true表示已经，false表示未绑定过
     * @param userId
     * @return
     */
    Boolean selectOpenId(String userId);

    /**
     * 订阅消息通知
     * @return
     * @param workorderBaseEsu
     */
    String sendMsg(WorkorderBaseEsu workorderBaseEsu);
}
