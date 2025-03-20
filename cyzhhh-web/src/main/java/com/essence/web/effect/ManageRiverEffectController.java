package com.essence.web.effect;

import com.alibaba.excel.EasyExcel;
import com.essence.common.utils.PageUtil;
import com.essence.common.utils.PageUtils;
import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.RiverEffectService;
import com.essence.interfaces.dot.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 管河成效
 */
@RestController
@RequestMapping("/effect/board")
public class ManageRiverEffectController {
    @Resource
    private RiverEffectService riverEffectService;

    /**
     * 管河成效-案件类型统计/河道管理所管和成效统计
     *
     * @return
     */
    @GetMapping("/case/statistic")
    public ResponseResult<List<EffectCaseStatisticDto>> getCaseTypeStatistic(EffectRequestDto effectRequestDto) throws IOException {
        List<EffectCaseStatisticDto> caseTypeStatistic = riverEffectService.getCaseTypeStatistic(effectRequestDto);
        return ResponseResult.success("查询成功",caseTypeStatistic);
    }

    /**
     * 管河成效-综合看板/-分管河段统计
     *
     * @param effectRequestDto
     * @return
     */
    @PostMapping("/case/statisticList")
    public ResponseResult<PageUtils<EffectRiverListDto>> getRiverStatisticList(@RequestBody EffectRequestDto effectRequestDto){
        PageUtils<EffectRiverListDto> effectRiverListDtoList = riverEffectService.getRiverStatisticList(effectRequestDto);
        return ResponseResult.success("查询成功",effectRiverListDtoList);
    }

    /**
     * 管河成效-综合看板/-excel导出
     *
     * @param effectRequestDto
     * @return
     */
    @PostMapping("/case/export")
    public ResponseResult getRiverStatisticExportList(@RequestBody EffectRequestDto effectRequestDto, HttpServletResponse response) throws IOException {
        List<EffectExportRiverListDto> effectRiverListDtoList = riverEffectService.getRiverStatisticExportList(effectRequestDto);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName ="分管河段成效统计";
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xls");
        EasyExcel.write(response.getOutputStream(), EffectExportRiverListDto.class).sheet("分管河段成效统计").doWrite(effectRiverListDtoList);
        return ResponseResult.success("查询成功",null);
    }

    /**
     * 管河成效-/河道管理所管和成效统计-河段案件种类统计
     *
     * @return
     */
    @PostMapping("/river/statistic")
    public ResponseResult<List<EffectRiverDto>> getRiverStatistic(@RequestBody EffectRequestDto effectRequestDto) throws IOException {
        List<EffectRiverDto> caseTypeStatistic = riverEffectService.getRiverStatistic(effectRequestDto);
        return ResponseResult.success("查询成功",caseTypeStatistic);
    }

    /**
     * 管河成效-案件渠道统计
     *
     * @return
     */
    @GetMapping("/case/channel/statistic")
    public ResponseResult<List<EffectCaseStatisticDto>> getCaseChannelStatistic(EffectRequestDto effectRequestDto) throws IOException {
        List<EffectCaseStatisticDto> caseTypeStatistic = riverEffectService.getCaseChannelStatistic(effectRequestDto);
        return ResponseResult.success("查询成功",caseTypeStatistic);
    }

    /**
     * 留痕统计
     *
     * @return
     */
    @GetMapping("/mark")
    public ResponseResult<PageUtil<EffectMarkStatistic>> getCaseMarkStatistic(EffectRequestDto effectRequestDto) throws IOException {
        PageUtil<EffectMarkStatistic> caseTypeStatistic = riverEffectService.getCaseMarkStatistic(effectRequestDto);
        return ResponseResult.success("查询成功",caseTypeStatistic);
    }

    /**
     * 重点河段统计
     *
     * @return
     */
    @GetMapping("/rea/geom")
    public ResponseResult<PageUtil<EffectGeomStatisticDto>> getReaGeomMarkStatistic(EffectRequestDto effectRequestDto) throws IOException {
        PageUtil<EffectGeomStatisticDto> caseTypeStatistic = riverEffectService.getReaGeomMarkStatistic(effectRequestDto);
        return ResponseResult.success("查询成功",caseTypeStatistic);
    }

    /**
     * 留痕统计
     *
     * @return
     */
    @GetMapping("/mark/export")
    public ResponseResult getCaseMarkStatisticExport(EffectRequestDto effectRequestDto, HttpServletResponse response) throws IOException {
        List<EffectMarkStatistic> caseTypeStatistic = riverEffectService.getCaseMarkStatisticList(effectRequestDto);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName ="巡查留痕";
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xls");
        EasyExcel.write(response.getOutputStream(), EffectMarkStatistic.class).sheet("巡查留痕").doWrite(caseTypeStatistic);
        return ResponseResult.success("查询成功",null);
    }

}
