package com.essence.service;

import com.essence.common.dto.*;
import com.essence.common.utils.PageUtil;
import com.essence.common.utils.ResponseResult;

import java.text.ParseException;
import java.util.List;

/**
 * @author essence
 * @Classname StationService
 * @Description TODO
 * @Date 2022/10/14 15:56
 * @Created by essence
 */
public interface StationService {
    /**
     * 获取场站基本信息
     * @return
     * @param stStbprpBEntityDTO
     */
    public ResponseResult getStationInfoList(StStbprpBEntityDTO stStbprpBEntityDTO);

    /**
     * 获取单个场站 详细数据
     * @return
     * @param stStbprpBEntityDTO
     */
    List<StWaterRateEntityDTO> getStationDataList(StStbprpBEntityDTO stStbprpBEntityDTO) throws ParseException;

    public List<StWaterRateEntityDTO> getDataList(StStbprpBEntityDTO stStbprpBEntityDTO) throws ParseException;

    /**
     *
     * @param stStbprpB
     */
    void updateById(StStbprpBEntityDTO stStbprpB);

    void removeByIds(List<String> asList);

    void add(StStbprpBEntityDTO stStbprpB);

    /**
     * 获取excel导出数据
     * @param stStbprpBEntityDTO
     * @return
     */
    List<StStbprpBEntityDTO> getStationExcelList(StStbprpBEntityDTO stStbprpBEntityDTO);

    DeviceStatusDTO getDeviceRunStatus();

    WaterPositionStatisticDTO getStationStatus();

    DeviceFeelCountDTO getDeviceFellStatistic(DeviceFeelRequestDTO deviceFeelRequestDTO);

    OnlineCheckDeviceDTO getDeviceOnlineStatistic(DeviceFeelRequestDTO deviceFeelRequestDTO);
    /**
     * 场站达标率配置
     * @param qualifiedConfigDTO
     */
    void setStationCheckConfig(List<QualifiedConfigDTO> qualifiedConfigDTO);

    /**
     * 场站状态 列表查询
     * @param stationStatusRequestDTO
     * @return
     */
    PageUtil<OnlineDeviceDetailDTO> getStationStatusList(StationStatusRequestDTO stationStatusRequestDTO);

    List<QualifiedConfigDTO> getStationCheckConfig(QualifiedConfigDTO qualifiedConfigDTO);

    /**
     * 台账数量统计
     * @return
     */
    DeviceCountDTO getDeviceCount();

    /**
     * 获取流量站 状态信息
     * @param stationFlowDTO
     * @return
     */
    StationFlowStatisticDTO getFlowRiver(StationFlowDTO stationFlowDTO);
}
