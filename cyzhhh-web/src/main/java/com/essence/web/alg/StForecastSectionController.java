package com.essence.web.alg;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.StForecastSectionService;
import com.essence.interfaces.model.StForecastSectionEsr;
import com.essence.interfaces.model.StForecastSectionEsu;
import com.essence.interfaces.model.StForecastSectionExport;
import com.essence.interfaces.param.StForecastSectionEsp;
import com.essence.web.basecontroller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 站点断面预测预报数据填写以及查询
 *
 * @author cuirx
 * @since 2023-04-22 10:54:47
 */
@RestController
@RequestMapping("/stForecastSection")
@Slf4j
public class StForecastSectionController extends BaseController<String, StForecastSectionEsu, StForecastSectionEsp, StForecastSectionEsr> {
    @Autowired
    private StForecastSectionService stForecastSectionService;

    public StForecastSectionController(StForecastSectionService stForecastSectionService) {
        super(stForecastSectionService);
    }

    /**
     * 导入流量边界/水位边界-预报数 流量1水位2
     *@param caseId 方案id
     * @param type
     *  @param file   文件
     * @return
     */
    @PostMapping("/import/stForecastSectionZZ")
    public ResponseResult importStForecastSection(@RequestParam("caseId") String caseId, @RequestParam("type") String type, @RequestParam("file") MultipartFile file) {

        if (null == file || (!file.getOriginalFilename().endsWith(".xlsx") && !file.getOriginalFilename().endsWith(".xls"))) {
            return ResponseResult.error("导入失败-文件格式不正确");
        }
        if (StringUtils.isBlank(type)) {
            return ResponseResult.error("type类型参数有误");
        }
        if (type.equals("1")) {
            stForecastSectionService.importStForecastSectionZQ(caseId, file,type);
        } else {
            stForecastSectionService.importStForecastSectionZZ(caseId, file,type);
        }

        return ResponseResult.success("导入成功", null);
    }

    /**
     * 导出流量边界/水位边界-预报数据模板流量1水位2
     *
     * @param type
     * @return
     */
    @GetMapping("/export/stForecastSectionZZ")
    public void exportStForecastSection(HttpServletResponse response, String type) {
        try {
            List<StForecastSectionExport> list = new ArrayList<>();
            String fileName="";
            if (type.equals("1")) {
                list=stForecastSectionService.exportStForecastSectionZQ();
                 fileName = URLEncoder.encode("流量边界预报数据(" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ")", "UTF-8");

            } else {
                list=stForecastSectionService.exportStForecastSectionZZ();
                 fileName = URLEncoder.encode("水位边界预报数据(" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ")", "UTF-8");

            }
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
           response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            WriteCellStyle headWriteCellStyle = new WriteCellStyle();
            //设置头居中
            headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
            //内容策略
            WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
            //设置 水平居中
            contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
            HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

            EasyExcel.write(response.getOutputStream(), StForecastSectionExport.class).sheet("边界预报数据").registerWriteHandler(horizontalCellStyleStrategy).doWrite(list);
        } catch (Exception e) {
            log.error("导出边界预报数据模板失败！exportWaterPersonNumberList{}" + e);
        }
    }
}
