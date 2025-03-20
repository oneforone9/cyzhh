package com.essence.interfaces.api;


import com.essence.common.dto.DeviceForRiverDTO;
import com.essence.common.dto.RiverRequestDTO;
import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.ReaBaseEsp;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 河道信息表服务层
 *
 * @author zhy
 * @since 2022-10-18 17:22:20
 */
public interface ReaBaseService extends BaseApi<ReaBaseEsu, ReaBaseEsp, ReaBaseEsr> {

    /**
     * 扩展分页查询 添加树形结构
     *
     * @param param
     * @return
     */
    Paginator<ReaBaseEsrEx> searchTree(@RequestBody PaginatorParam param);


    /**
     * 一级河道类型统计
     * @return
     */
    List<StatisticsBase> statistics();
    /**
     * 一级河道列表
     * @return
     */
    List<ReaBaseEsr> searchLevel1();

    List<DeviceForRiverDTO> getDeviceOfRiver(RiverRequestDTO riverRequestDTO);

     List<ReaBaseDTO> getRiverInfoList();

    List<UnitBaseDTO> getPatrolPreview();

    ReaViewStatisticDto getReaViewData(String unitId);

}
