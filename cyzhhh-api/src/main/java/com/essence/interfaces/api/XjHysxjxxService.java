package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.XjHysxjxxEsr;
import com.essence.interfaces.model.XjHysxjxxEsu;
import com.essence.interfaces.model.XjHysxjxxQuery;
import com.essence.interfaces.param.XjHysxjxxEsp;

import java.util.List;

/**
 * 设备巡检会议室关联巡检信息服务层
 * @author majunjie
 * @since 2025-01-09 14:00:53
 */
public interface XjHysxjxxService extends BaseApi<XjHysxjxxEsu, XjHysxjxxEsp,XjHysxjxxEsr> {
    List<XjHysxjxxEsr> selectHyxcXx(XjHysxjxxQuery xjHysxjxxQuery);
}
