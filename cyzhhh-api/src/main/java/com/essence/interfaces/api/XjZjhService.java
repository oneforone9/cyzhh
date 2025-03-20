package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.XjZjhData;
import com.essence.interfaces.model.XjZjhEsr;
import com.essence.interfaces.model.XjZjhEsu;
import com.essence.interfaces.param.XjZjhEsp;

import java.util.List;
import java.util.Map;


/**
 * 设备周计划服务层
 * @author majunjie
 * @since 2025-01-08 15:52:41
 */
public interface XjZjhService extends BaseApi<XjZjhEsu, XjZjhEsp, XjZjhEsr> {
    String getWeekData(XjZjhData xjZjhData);

    Map<String, List<XjZjhEsr>> selectData(List<Integer> yearList);
}
