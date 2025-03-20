package com.essence.interfaces.api;

import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.ReaFocusGeomEsr;
import com.essence.interfaces.model.ReaFocusGeomEsu;
import com.essence.interfaces.param.ReaFocusGeomEsp;

import java.util.List;

/**
 * 河道重点位置地理信息表服务层
 *
 * @author zhy
 * @since 2022-10-26 14:06:36
 */
public interface ReaFocusGeomService extends BaseApi<ReaFocusGeomEsu, ReaFocusGeomEsp, ReaFocusGeomEsr> {

    /**
     * 根据河道主键查询（不含空间数据）
     * @param reaId
     * @return
     */
    List<ReaFocusGeomEsr> findByReaId(String reaId);
}
