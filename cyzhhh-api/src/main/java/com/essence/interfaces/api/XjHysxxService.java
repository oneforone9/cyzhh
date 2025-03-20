package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.XjHysxjxxEsr;
import com.essence.interfaces.model.XjHysxjxxQuery;
import com.essence.interfaces.model.XjHysxxEsr;
import com.essence.interfaces.model.XjHysxxEsu;
import com.essence.interfaces.param.XjHysxxEsp;

import java.util.List;

/**
 * 设备巡检会议室信息服务层
 * @author majunjie
 * @since 2025-01-08 08:14:05
 */
public interface XjHysxxService extends BaseApi<XjHysxxEsu, XjHysxxEsp, XjHysxxEsr> {

    List<XjHysxjxxEsr> selectHyxcXx(XjHysxjxxQuery xjHysxjxxQuery);
}
