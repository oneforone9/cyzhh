package com.essence.web.portal;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.essence.common.dto.*;
import com.essence.common.utils.PageUtil;
import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.StationService;
import com.essence.interfaces.dot.ReaBaseDto;
import com.essence.interfaces.model.OnlineDeviceDetailDVo;
import com.essence.interfaces.vo.WaterFlowIncreaseVo;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 水位、流量测站信息
 */
@RestController
@RequestMapping("/station")
public class StationInfoController {
    @Autowired
    private StationService stationService;

    /**
     * 查询场站列表条件
     *
     * @param stStbprpBEntityDTO 条件
     * @return
     */
    @PostMapping("/info/list")
    public ResponseResult<List<StStbprpBEntityDTO>> getStatInfoList(@RequestBody StStbprpBEntityDTO stStbprpBEntityDTO) throws ParseException {
        ResponseResult stationInfoList = stationService.getStationInfoList(stStbprpBEntityDTO);
        return stationInfoList;
    }

    /**
     * 入境 出境 流量监测
     *
     * @param stStbprpBEntityDTO 条件 传 stcd 即可
     * @return
     */
    @PostMapping("/access")
    public ResponseResult<FlowMonitorDto> getFlowRateMonitor(@RequestBody StStbprpBEntityDTO stStbprpBEntityDTO) throws ParseException {
        FlowMonitorDto stationInfoList = stationService.getFlowRateMonitor(stStbprpBEntityDTO);
        return ResponseResult.success("查询成功", stationInfoList);

    }

    /**
     * 某个场站下具体的瞬时水位/流量数据
     * 需要传递 测站stcd sttp
     *
     * @param stStbprpBEntityDTO
     * @return
     */
    @PostMapping("/data/list")
    public ResponseResult<List<StWaterRateEntityDTO>> getStationDataList(@RequestBody StStbprpBEntityDTO stStbprpBEntityDTO) throws ParseException {
        List<StWaterRateEntityDTO> stationInfoList = stationService.getStationDataList(stStbprpBEntityDTO);
        return ResponseResult.success("获取成功", stationInfoList);
    }


    /**
     * 某个场站下具体的数据-导出
     * 需要传递 测站stcd sttp
     *
     * @param stStbprpBEntityDTO
     * @return
     */
    @GetMapping("/data/excel")
    public ResponseResult<List<StWaterRateExcelDTO>> getStationExcelDataList(StStbprpBEntityDTO stStbprpBEntityDTO, HttpServletResponse response) throws ParseException, IOException {
        List<StWaterRateExcelDTO> stationInfoList = stationService.getStationExcelDataList(stStbprpBEntityDTO);
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + "监测数据" + ".xlsx");
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        //设置头居中
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        //内容策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        //设置 水平居中
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

        EasyExcel.write(response.getOutputStream(), StWaterRateExcelDTO.class).sheet("监测数据").registerWriteHandler(horizontalCellStyleStrategy).doWrite(stationInfoList);
        return ResponseResult.success("获取成功", stationInfoList);
    }

    /**
     * 水位流量上升增大对比
     */
    @GetMapping("/waterFlowIncrease")
    public ResponseResult<WaterFlowIncreaseVo> getWaterFlowIncrease() {
        WaterFlowIncreaseVo vo = stationService.getWaterFlowIncrease();
        return ResponseResult.success("查询成功", vo);
    }

    /**
     * 查询场站下具体的数据 需要传递 测站stcd sttp
     *
     * @param stStbprpBEntityDTO 查询参数
     * @return
     */
    @PostMapping("/rain/data")
    public ResponseResult<RainComposeDto> getStationRainNum(@RequestBody StStbprpBEntityDTO stStbprpBEntityDTO) throws ParseException {
        RainComposeDto rainNum = stationService.getStationRainNum(stStbprpBEntityDTO);
        return ResponseResult.success("获取成功", rainNum);
    }

    /**
     * 修改测站基本信息
     *
     * @param stStbprpB 修改的实体
     */
    @PostMapping("/update")
    public ResponseResult update(@RequestBody StStbprpBEntityDTO stStbprpB) {
        stationService.updateById(stStbprpB);
        return ResponseResult.success("ok", "更新成功");
    }

    /**
     * 根据stcd删除测站基本信息
     */
    @PostMapping("/delete")
    public ResponseResult delete(@RequestBody String[] stcds) {
        stationService.removeByIds(Arrays.asList(stcds));
        return ResponseResult.success("ok", "获取成功");
    }

    /**
     * 新增测站基本信息
     *
     * @param stStbprpB 新增
     */
    @PostMapping("/add")
    public ResponseResult add(@RequestBody StStbprpBEntityDTO stStbprpB) {
        stationService.add(stStbprpB);
        return ResponseResult.success("ok", "获取成功");
    }

    /**
     * 测站基本信息-导出
     *
     * @param stStbprpBEntityDTO 条件
     * @return
     */
    @PostMapping("/excel")
    public void getStatInfoListExcel(@RequestBody StStbprpBEntityDTO stStbprpBEntityDTO, HttpServletResponse response) throws IOException {
        List<StStbprpBEntityDTO> stationInfoList = stationService.getStationExcelList(stStbprpBEntityDTO);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("测站列表", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), StStbprpBEntityDTO.class).sheet("模板").doWrite(stationInfoList);
    }

    /**
     * 设备运行监视
     *
     * @return
     */
    @GetMapping("/device/status")
    public ResponseResult<DeviceStatusDTO> DeviceRunStatus() {
        DeviceStatusDTO deviceRunStatus = stationService.getDeviceRunStatus();
        return ResponseResult.success("ok", deviceRunStatus);
    }


    /**
     * 场站状态
     *
     * @return
     */
    @GetMapping("/station/status")
    public ResponseResult<WaterPositionStatisticDTO> stationStatus() {
        WaterPositionStatisticDTO stationStatus = stationService.getStationStatus();
        return ResponseResult.success("ok", stationStatus);
    }

    /**
     * 设备感知 在线年月日纬度 统计图
     *
     * @return
     */
    @PostMapping("/fell")
    public ResponseResult<List<DeviceFellStatusDTO>> stationFell(@RequestBody DeviceFeelRequestDTO deviceFeelRequestDTO) {
        if (deviceFeelRequestDTO.getDateStr() == null) {
            deviceFeelRequestDTO.setDate(new Date());
        } else {
            deviceFeelRequestDTO.setDate(DateUtil.parseDate(deviceFeelRequestDTO.getDateStr()));
        }
        List<DeviceFellStatusDTO> res = stationService.getDeviceFellStatistic(deviceFeelRequestDTO);
        return ResponseResult.success("ok", res);
    }


    /**
     * 在线达标 统计分析年月日纬度 统计图
     *
     * @return
     */
    @PostMapping("/online/statistic")
    public ResponseResult<OnlineCheckDeviceDTO> stationOnline(@RequestBody DeviceFeelRequestDTO deviceFeelRequestDTO) {
        if (deviceFeelRequestDTO.getDateStr() == null) {
            deviceFeelRequestDTO.setDate(new Date());
        } else {
            deviceFeelRequestDTO.setDate(DateUtil.parseDate(deviceFeelRequestDTO.getDateStr()));
        }
        OnlineCheckDeviceDTO onlineCheckDeviceDTO = stationService.getDeviceOnlineStatistic(deviceFeelRequestDTO);
        return ResponseResult.success("ok", onlineCheckDeviceDTO);
    }

    /**
     * 在线达标 统计分析-列表
     *
     * @return
     */
    @PostMapping("/online/infoList")
    public ResponseResult<PageUtil<OnlineDeviceDetailDTO>> stationOnlineDetail(@RequestBody StationStatusRequestDTO stationStatusRequestDTO) {
        if (stationStatusRequestDTO.getDateStr() == null) {
            stationStatusRequestDTO.setDate(new Date());
        } else {
            stationStatusRequestDTO.setDate(DateUtil.parseDate(stationStatusRequestDTO.getDateStr()));
        }
        PageUtil<OnlineDeviceDetailDTO> onlineDeviceDetailDTO = stationService.getStationStatusList(stationStatusRequestDTO);
        return ResponseResult.success("ok", onlineDeviceDetailDTO);
    }

    /**
     * 导出在线达标统计分析数据
     *
     * @param stationStatusRequestDTO
     * @return R
     */
    @PostMapping("/export/infoList")
    public void exportInfoList(HttpServletResponse response, @RequestBody StationStatusRequestDTO stationStatusRequestDTO) {
        try {
            if (stationStatusRequestDTO.getDateStr() == null) {
                stationStatusRequestDTO.setDate(new Date());
            } else {
                stationStatusRequestDTO.setDate(DateUtil.parseDate(stationStatusRequestDTO.getDateStr()));
            }
            List<OnlineDeviceDetailDVo> list = stationService.getStationStatusLists(stationStatusRequestDTO);

            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("在线达标统计分析数据(" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ")", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            WriteCellStyle headWriteCellStyle = new WriteCellStyle();
            //设置头居中
            headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
            //内容策略
            WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
            //设置 水平居中
            contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
            HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
            EasyExcel.write(response.getOutputStream(), OnlineDeviceDetailDVo.class).sheet("在线达标统计分析数据").registerWriteHandler(horizontalCellStyleStrategy).doWrite(list);
        } catch (Exception e) {
            System.out.println("在线达标统计分析数据" + e);
        }
    }


    /**
     * 场站达标配置
     *
     * @return
     */
    @PostMapping("/check/config")
    public ResponseResult stationCheckConfig(@RequestBody List<QualifiedConfigDTO> qualifiedConfigDTOs) {
        stationService.setStationCheckConfig(qualifiedConfigDTOs);
        return ResponseResult.success("ok", "保存成功");
    }

    /**
     * 场站达标配置列表展示
     *
     * @return
     */
    @GetMapping("/check/config/List")
    public ResponseResult<List<QualifiedConfigDTO>> stationCheckConfigList(QualifiedConfigDTO qualifiedConfigDTO) {
        List<QualifiedConfigDTO> list = stationService.getStationCheckConfig(qualifiedConfigDTO);
        return ResponseResult.success("ok", list);
    }


    /**
     * 获取台账设备数量统计
     *
     * @param unitId 单位id 可以传递 或者不传查询所有
     * @return
     */
    @GetMapping("/count")
    public ResponseResult<DeviceCountDTO> getDeviceCount(String unitId) {
        DeviceCountDTO deviceCount = stationService.getDeviceCount(unitId);
        return ResponseResult.success("ok", deviceCount);
    }

    /**
     * 获取 流动的河 流量
     *
     * @return
     */
    @GetMapping("/flow")
    public ResponseResult<StationFlowStatisticDTO> getFlowRiver(StationFlowDTO stationFlowDTO) {
        StationFlowStatisticDTO stationFlowStatisticDTO = stationService.getFlowRiver(stationFlowDTO);
        return ResponseResult.success("ok", stationFlowStatisticDTO);
    }

    /**
     * 获取 流动的河 流量 地图
     *
     * @return
     */
    @GetMapping("/flow/map")
    public ResponseResult<List<ReaBaseDto>> getFlowRiverMap() {
        List<ReaBaseDto> reaBaseDtoList = stationService.getFlowRiverMap();
        return ResponseResult.success("ok", reaBaseDtoList);
    }


}
