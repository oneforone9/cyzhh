package com.essence.interfaces.api;

import com.essence.common.dto.*;
import com.essence.common.utils.PageUtil;
import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.dot.ReaBaseDto;
import com.essence.interfaces.model.OnlineDeviceDetailDVo;
import com.essence.interfaces.vo.WaterFlowIncreaseVo;

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
    ResponseResult getStationInfoList(StStbprpBEntityDTO stStbprpBEntityDTO) throws ParseException;

    /**
     * 获取单个场站 详细数据
     * @return
     * @param stStbprpBEntityDTO
     */
    List<StWaterRateEntityDTO> getStationDataList(StStbprpBEntityDTO stStbprpBEntityDTO) throws ParseException;

    List<StWaterRateExcelDTO> getStationExcelDataList(StStbprpBEntityDTO stStbprpBEntityDTO) throws ParseException;

    /**
     * 获取每分钟数据
     * @param stStbprpBEntityDTO
     * @return
     * @throws ParseException
     */
    List<StWaterRateEntityDTO> getStationDataMomentList(StStbprpBEntityDTO stStbprpBEntityDTO) ;


    /**
     * 水位流量上升增大对比
     */
    WaterFlowIncreaseVo getWaterFlowIncrease();

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

    List<DeviceFellStatusDTO> getDeviceFellStatistic(DeviceFeelRequestDTO deviceFeelRequestDTO);

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
    DeviceCountDTO getDeviceCount(String unitId);

    /**
     * 获取流量站 状态信息
     * @param stationFlowDTO
     * @return
     */
    StationFlowStatisticDTO getFlowRiver(StationFlowDTO stationFlowDTO);

    List<ReaBaseDto> getFlowRiverMap();

    RainComposeDto getStationRainNum(StStbprpBEntityDTO stStbprpBEntityDTO);

    FlowMonitorDto getFlowRateMonitor(StStbprpBEntityDTO stStbprpBEntityDTO) throws ParseException;

    List<OnlineDeviceDetailDVo> getStationStatusLists(StationStatusRequestDTO stationStatusRequestDTO);




}
