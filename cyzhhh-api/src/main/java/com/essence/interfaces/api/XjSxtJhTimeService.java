package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.XjSxtJhTimeEsr;
import com.essence.interfaces.model.XjSxtJhTimeEsu;
import com.essence.interfaces.param.XjSxtJhTimeEsp;

import java.util.List;

/**
 * 设备巡检摄像头巡检计划日期服务层
 * @author majunjie
 * @since 2025-01-08 17:51:57
 */
public interface XjSxtJhTimeService extends BaseApi<XjSxtJhTimeEsu, XjSxtJhTimeEsp, XjSxtJhTimeEsr> {
    List<XjSxtJhTimeEsr> selectData(Integer id);

    void saveData(List<XjSxtJhTimeEsu> list,String jhid);
}
