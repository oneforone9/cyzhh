package com.essence.web.contract;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.io.resource.InputStreamResource;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.essence.common.utils.ResponseResult;
import com.essence.dao.HtglDao;
import com.essence.dao.HtgllcDao;
import com.essence.dao.HtglysxmDao;
import com.essence.dao.ViewOfficeBaseDao;
import com.essence.dao.entity.HtglDto;
import com.essence.euauth.common.SysConstant;
import com.essence.euauth.common.util.StringUtil;
import com.essence.interfaces.api.HtglService;
import com.essence.interfaces.api.ViewHtscService;
import com.essence.interfaces.api.ViewOfficeBaseService;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.*;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 合同管理管理
 *
 * @author majunjie
 * @since 2024-09-09 17:45:27
 */
@RestController
@RequestMapping("/htgl")
@Log4j2
public class HtglController {
    @Autowired
    private HtglService htglService;
    @Autowired
    private HtglDao htglDao;
    @Autowired
    private HtglysxmDao htglysxmDao;
    @Autowired
    private HtgllcDao htgllcDao;
    @Autowired
    private ViewOfficeBaseDao viewOfficeBaseDao;
    @Autowired
    private ViewOfficeBaseService viewOfficeBaseService;
    @Autowired
    private ViewHtscService viewHtscService;


    ////////////////////////////////////////////////////////////// 第三版本合同，还在使用中 ----------start //////////////////////////////////////////////////


    /**
     * 合同删除
     *
     * @param htDel
     * @return R
     */
    @PostMapping("/deleteHt")
    public ResponseResult deleteHt(@RequestBody HtDel htDel) {
        String type = htglService.deleteHt(htDel);
        if (StringUtils.isNotBlank(type)) {
            return ResponseResult.success("删除成功", type);
        } else {
            return ResponseResult.error("删除失败");
        }

    }

    /**
     * 合同导入
     *
     * @param file
     * @return R
     */
    @PostMapping("/importHt")
    public ResponseResult importHt(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
//        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
//        if (StringUtil.isEmpty(userId)) {
//            return ResponseResult.error("登录已过期-导入失败");
//        }
        String userId ="e0457f8dda164911a12294900efd18d8";
        String type = htglService.importHt(file, userId);
        if (StringUtils.isNotBlank(type)) {
            return ResponseResult.success("合同导入成功", type);
        } else {
            return ResponseResult.error("合同导入失败");
        }
    }


    /**
     * 合同历程根据条件分页查询
     *
     * @param param
     * @return R
     */
    @PostMapping("/htlxSearch")
    public ResponseResult<Paginator<HtgllcEsr>> htlxSearch(@RequestBody PaginatorParam param) {
        return ResponseResult.success("查询成功", htglService.htlxSearch(param));
    }


    /**
     * 合同导出数据
     *
     * @return R
     */
    @PostMapping("/exportData")
    public void exportData(HttpServletResponse response, @RequestBody PaginatorParam param) {
        try {
            Paginator<HtglEsr> byPaginator = htglService.findByPaginator(param);
            List<HtglExport> exportList = new ArrayList<>();
            if (CollectionUtil.isNotEmpty(byPaginator.getItems())) {
                for (HtglEsr item : byPaginator.getItems()) {
                    HtglExport htglExport = new HtglExport();
                    BeanUtils.copyProperties(item, htglExport);
                    exportList.add(htglExport);
                }
            }
            String fileName = URLEncoder.encode("台账数据(" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ")", "UTF-8");
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

            EasyExcel.write(response.getOutputStream(), HtglExport.class).sheet("台账数据").registerWriteHandler(horizontalCellStyleStrategy).doWrite(exportList);
        } catch (Exception e) {
            log.error("导出台账数据失败！exportData{}" + e);
        }
    }


    //////////////////////////////////////////////////////////////////// 第三版本合同，还在使用中 -------end //////////////////////////////////////////////////


    /**
     * 合同列表根据条件分页查询
     *
     * @param param
     * @return R
     */
    @PostMapping("/search")
    public ResponseResult<Paginator<HtglEsr>> selectStForecastDataList(@RequestBody PaginatorParam param) {
        return ResponseResult.success("查询成功", htglService.findByPaginator(param));
    }

    /**
     * 合同列表根据条件分页查询(收藏专用)
     *
     * @param param
     * @return R
     */
    @PostMapping("/searchSc")
    public ResponseResult<Paginator<ViewHtscEsr>> searchSc(@RequestBody PaginatorParam param) {
        return ResponseResult.success("查询成功", viewHtscService.findByPaginator(param));
    }

    /**
     * 通讯录根据条件分页查询
     *
     * @param param
     * @return R
     */
    @PostMapping("/txlSearch")
    public ResponseResult<Paginator<ViewOfficeBaseEsr>> txlSearch(@RequestBody PaginatorParam param) {
        return ResponseResult.success("查询成功", viewOfficeBaseService.findByPaginator(param));
    }

    /**
     * 合同附件根据条件分页查询
     *
     * @param param
     * @return R
     */
    @PostMapping("/htfjSearch")
    public ResponseResult<Paginator<HtglfjEsr>> htfjSearch(@RequestBody PaginatorParam param) {
        return ResponseResult.success("查询成功", htglService.htfjSearch(param));
    }

    /**
     * 合同预审项目根据条件分页查询
     *
     * @param param
     * @return R
     */
    @PostMapping("/htysxmSearch")
    public ResponseResult<Paginator<HtglysxmEsr>> htysxmSearch(@RequestBody PaginatorParam param) {
        return ResponseResult.success("查询成功", htglService.htysxmSearch(param));
    }


    /**
     * 新增(保存/提交)合同申请人填写合法性审查单
     *
     * @param htglScd
     * @return R
     */
    @PostMapping("/addHtScd")
    public ResponseResult<HtglEsr> addHtScd(@RequestBody HtglScd htglScd) {
        return ResponseResult.success("新增/修改成功", htglService.addHtScd(htglScd));
    }

    /**
     * 台账合同新增
     *
     * @param htglEsu
     * @return R
     */
    @PostMapping("/addHtByTz")
    public ResponseResult<HtglEsr> addHtByTz(@RequestBody HtglEsu htglEsu) {
        return ResponseResult.success("修改成功", htglService.addHtByTz(htglEsu));
    }

    /**
     * 合同修改(除科长确认会签单 财务室办公室科确认会签单)
     *
     * @param htglEsuData
     * @return R
     */
    @PostMapping("/addHt")
    public ResponseResult<HtglEsr> addHt(@RequestBody HtglEsuData htglEsuData) {
        return ResponseResult.success("修改成功", htglService.addHt(htglEsuData));
    }

    /**
     * 科长确认会签单
     *
     * @param htglEsuDatas
     * @return R
     */
    @PostMapping("/updateHqdKz")
    public ResponseResult<HtglEsr> updateHqdKz(@RequestBody HtglEsuDatas htglEsuDatas) {
        return ResponseResult.success("确认成功", htglService.updateHqdKz(htglEsuDatas));
    }

    /**
     * 办公室/财务室确认会签单
     *
     * @param htglEsuDatas
     * @return R
     */
    @PostMapping("/updateHqdks")
    public ResponseResult<HtglEsr> updateHqdks(@RequestBody HtglEsuDatas htglEsuDatas) {
        return ResponseResult.success("确认成功", htglService.updateHqdks(htglEsuDatas));
    }

    /**
     * 添加合同放回回收站
     *
     * @param hthsz
     * @return R
     */
    @PostMapping("/addHtHsz")
    public ResponseResult<HtglEsr> addHtHsz(@RequestBody Hthsz hthsz) {
        return ResponseResult.success("添加成功", htglService.addHtHsz(hthsz));
    }

    /**
     * 添加合同挂起
     *
     * @param hthsz
     * @return R
     */
    @PostMapping("/addHtGq")
    public ResponseResult<HtglEsr> addHtGq(@RequestBody Hthsz hthsz) {
        return ResponseResult.success("挂起成功", htglService.addHtGq(hthsz));
    }

    /**
     * 查询用户是否收藏该合同true已收藏false为收藏
     *
     * @param hthsz
     * @return R
     */
    @PostMapping("/selectHtsc")
    public ResponseResult<Boolean> selectHtsc(@RequestBody Hthsz hthsz) {
        return ResponseResult.success("查询成功", viewHtscService.selectHtsc(hthsz));
    }

    /**
     * 添加合同收藏
     *
     * @param hthsz
     * @return R
     */
    @PostMapping("/addHtSc")
    public ResponseResult<HtglEsr> addHtSc(@RequestBody Hthsz hthsz) {
        return ResponseResult.success("收藏成功", htglService.addHtSc(hthsz));
    }


    /**
     * 合同附件关联
     *
     * @param htglFjImports
     * @return R
     */
    @PostMapping("/importHtFj")
    public ResponseResult importHtFj(@RequestBody HtglFjImports htglFjImports) {
        String type = htglService.importHtFj(htglFjImports);
        if (StringUtils.isNotBlank(type)) {
            return ResponseResult.success("合同附件导入成功", type);
        } else {
            return ResponseResult.error("合同附件导入失败");
        }
    }


    /**
     * 合法性审查导出
     *
     * @return R
     */
    @PostMapping("/hfxscd/exportData")
    public void hfxscdExportData(HttpServletResponse response, @RequestParam("id") String id) {
        try {
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("合法性审查单(" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ")", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            InputStream templateFile = new ClassPathResource("HTXSCD.xlsx").getStream();
            ExcelWriter writer = EasyExcel.write(response.getOutputStream()).withTemplate(templateFile).build();
            WriteSheet sheet = EasyExcel.writerSheet().build();
            FillConfig fillConfig = FillConfig.builder().forceNewRow(true).build();
            HtglDto htglDto = htglDao.selectById(id);
            if (null != id) {
                HtglExport htglExport = new HtglExport();
                BeanUtils.copyProperties(htglDto, htglExport);
                writer.fill(htglExport, fillConfig, sheet);
            }
            writer.finish();
        } catch (Exception e) {
            log.error("合法性审查导出！exportLd{}", e);
            e.printStackTrace();
        }
    }


    /**
     * 合同管理合同编号模板
     *
     * @return R
     */
    @PostMapping("/htglmb")
    public void htsphqdMB(HttpServletResponse response, @RequestParam("id") String htglhtmb) {
        try {
            // 设置响应的内容类型和字符集
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());

            // 编码文件名以支持特殊字符
            String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String encodedFileName = URLEncoder.encode("(" + currentDate + ")", StandardCharsets.UTF_8.name());

            // 设置响应头，告诉浏览器这是一个附件并且指定文件名
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + encodedFileName + ".xlsx");

            // 从类路径资源中读取模板文件
            ClassPathResource classPathResource = new ClassPathResource(htglhtmb + ".xlsx");
            InputStream templateFile = classPathResource.getStream();

            // 使用Spring的工具类来构建ResponseEntity对象，这样可以更方便地处理流
            InputStreamResource resource = new InputStreamResource(templateFile);
            ResponseEntity<InputStreamResource> responseEntity = ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=utf-8''" + encodedFileName + ".xlsx").contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")).body(resource);

            // 将文件内容写入到HTTP响应中
            resource.writeTo(response.getOutputStream());

            // 关闭输入流
            templateFile.close();
        } catch (IOException e) {
            // 处理可能出现的异常
            e.printStackTrace();
        }
    }
}
