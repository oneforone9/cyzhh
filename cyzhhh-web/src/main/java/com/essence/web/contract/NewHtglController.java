package com.essence.web.contract;


import com.essence.common.utils.ResponseResult;
import com.essence.euauth.common.SysConstant;
import com.essence.euauth.common.util.StringUtil;
import com.essence.framework.util.DateUtil;
import com.essence.interfaces.api.NewHtglService;
import com.essence.interfaces.model.HtglEsr;
import com.essence.interfaces.model.HtglEsu;
import com.essence.interfaces.model.HtglEsuData;
import com.essence.interfaces.model.RunStyle;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 新版本合同管理
 *
 * @author majunjie
 * @since 2024-09-09 17:45:27
 */
@RestController
@RequestMapping("/newhtgl")
@Log4j2
public class NewHtglController {

    @Autowired
    private NewHtglService newHtglService;


    /**
     * 填充会签单模板并下载
     */
    @PostMapping("/fillHqdForDownload")
    public void fillHqdForDownload(@RequestBody HtglEsu htglEsu, HttpServletResponse response) {
        try {
            // 转换数据
            Map<String, String> data = transferMap(htglEsu);

            // 查询一下，是否是基层单位
            boolean jjg = newHtglService.isJjg(htglEsu.getQdks(), htglEsu.getQdksId());

            // 生成填充后的文档字节数组
            byte[] docBytes = fillWordTemplate(data, jjg);

            // 设置响应头
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("utf-8");
            // 设置文件名，进行URL编码防止中文乱码
            String fileName = URLEncoder.encode("会签单.docx", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);

            // 写入响应流
            response.getOutputStream().write(docBytes);
            response.getOutputStream().flush();

        } catch (Exception e) {
            // 发生异常时设置响应状态和错误信息
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try {
                response.getWriter().write("处理Word文档失败：" + e.getMessage());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    /**
     * 查询会签单，基本信息 (合同草稿)
     */
    @GetMapping("/searchHqdById/{id}")
    public ResponseResult<HtglEsr> searchHqdById(@PathVariable String id) {
        return ResponseResult.success("查询成功", newHtglService.searchHqdById(id));
    }


    /**
     * 合同新增，修改
     *
     * @param htglEsuData
     * @return R
     */
    @PostMapping("/addHt")
    public ResponseResult<HtglEsr> addHt(@RequestBody HtglEsuData htglEsuData) {
        return ResponseResult.success("修改成功", newHtglService.addHt(htglEsuData));
    }


    /**
     * 批量撤回
     *
     * @param idList
     * @return
     */
    @PostMapping("/returnPrevious")
    public ResponseResult returnPrevious(HttpServletRequest request, @RequestBody List<String> idList) {
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-无法撤回");
        }
        return ResponseResult.success("撤回成功", newHtglService.returnPrevious(userId, idList));
    }


    /**
     * 回退上一个人
     * @param request
     * @param htglEsu
     * @return
     */
    @PostMapping("/backPrevious")
    public ResponseResult backPrevious(HttpServletRequest request, @RequestBody HtglEsu htglEsu) {
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-无法回退");
        }
        htglEsu.setXyczry(userId);
        return ResponseResult.success("回退成功", newHtglService.backPrevious(htglEsu));
    }


    /**
     * 判断是否，需要授权委托书
     * @param htId
     * @return
     */
    @GetMapping("/judgeIsHaveWts/{htId}")
    public ResponseResult<Boolean> judgeIsHaveWts(@PathVariable String htId) {
        return ResponseResult.success("查询成功", newHtglService.judgeIsHaveWts(htId));
    }


    /**
     * 填充Word模板
     *
     * @param data 需要替换的数据
     * @return 文档字节数组
     */
    public byte[] fillWordTemplate(Map<String, String> data, boolean jjg) throws IOException {

        String templatePath = jjg ? "/templates/hqdmb.docx" : "/templates/hqdmbJC.docx";
        // 使用ClassPathResource读取模板
        try (InputStream inputStream = new ClassPathResource(templatePath).getInputStream()) {
            XWPFDocument document = new XWPFDocument(inputStream);

            try {
                // 替换段落中的文本
                for (XWPFParagraph paragraph : document.getParagraphs()) {
                    replaceInParagraph(paragraph, data);
                }

                // 替换表格中的文本
                for (XWPFTable table : document.getTables()) {
                    replaceInTable(table, data);
                }

                // 将文档转换为字节数组
                return convertDocumentToBytes(document);
            } finally {
                // 确保文档被关闭
                document.close();
            }
        } catch (Exception e) {
            log.error("填充Word模板失败", e);
            throw new IOException("处理文档时发生错误", e);
        }
    }

    /**
     * 替换段落中的文本并保持样式
     */
    private void replaceInParagraph(XWPFParagraph paragraph, Map<String, String> data) {
        List<XWPFRun> runs = paragraph.getRuns();

        // 收集需要处理的运行块
        for (int i = 0; i < runs.size(); i++) {
            XWPFRun run = runs.get(i);
            String text = run.getText(0);

            if (text != null) {
                // 保存原始样式信息
                RunStyle originalStyle = saveRunStyle(run);

                // 替换文本
                boolean replaced = false;
                for (Map.Entry<String, String> entry : data.entrySet()) {
                    String placeholder = "${" + entry.getKey() + "}";
                    if (text.contains(placeholder)) {
                        String replace = entry.getValue() != null ? entry.getValue() : "";
                        text = text.replace(placeholder, replace);
                        replaced = true;
                    }
                }

                if (replaced) {
                    // 设置新文本
                    run.setText(text, 0);
                    // 恢复原始样式
                    applyRunStyle(run, originalStyle);
                }
            }
        }
    }


    /**
     * 替换表格中的文本
     */
    private void replaceInTable(XWPFTable table, Map<String, String> data) {
        for (XWPFTableRow row : table.getRows()) {
            for (XWPFTableCell cell : row.getTableCells()) {
                for (XWPFParagraph paragraph : cell.getParagraphs()) {
                    replaceInParagraph(paragraph, data);
                }
            }
        }
    }

    /**
     * 保存运行块的样式
     */
    private RunStyle saveRunStyle(XWPFRun run) {
        RunStyle style = new RunStyle();
        try {
            style.setFontFamily(run.getFontFamily());
            style.setFontSize(run.getFontSize());
            style.setBold(run.isBold());
            style.setItalic(run.isItalic());
            style.setStrike(run.isStrike());
            style.setColor(run.getColor());
            style.setTextPosition(run.getTextPosition());
            style.setUnderline(run.getUnderline());
            style.setCharacterSpacing(run.getCharacterSpacing());
        } catch (Exception e) {
            log.warn("保存样式时发生异常", e);
        }
        return style;
    }

    /**
     * 应用运行块的样式
     */
    private void applyRunStyle(XWPFRun run, RunStyle style) {
        try {
            if (style.getFontFamily() != null) {
                run.setFontFamily(style.getFontFamily());
            }
            if (style.getFontSize() != -1) {
                run.setFontSize(style.getFontSize());
            }
            run.setBold(style.isBold());
            run.setItalic(style.isItalic());
            run.setStrike(style.isStrike());
            if (style.getColor() != null) {
                run.setColor(style.getColor());
            }
            run.setTextPosition(style.getTextPosition());
            if (style.getUnderline() != null) {
                run.setUnderline(style.getUnderline());
            }
            run.setCharacterSpacing(style.getCharacterSpacing());
        } catch (Exception e) {
            log.warn("应用样式时发生异常", e);
        }
    }

    /**
     * 将文档转换为字节数组
     */
    private byte[] convertDocumentToBytes(XWPFDocument document) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            document.write(outputStream);
            return outputStream.toByteArray();
        }
    }


    private Map<String, String> transferMap(HtglEsu htglEsu) {
        Map<String, String> data = new HashMap<>();
        data.put("htmc", htglEsu.getHtmc());
        data.put("htjf", htglEsu.getHtjf());
        data.put("htyf", htglEsu.getHtyf());
        data.put("qtf", htglEsu.getQtf());
        data.put("htfs", htglEsu.getHtfs() + "");
        data.put("lsscbh", htglEsu.getLsscbh());
        data.put("htnr", htglEsu.getHtnr());
        data.put("qdks", htglEsu.getQdks());
        data.put("jbrmc", htglEsu.getJbrmc());
        Date dateByStringDay = null;
        if (StringUtil.isNotEmpty(htglEsu.getHtsqrq())) {
            dateByStringDay = DateUtil.getDateByStringDay(htglEsu.getHtsqrq());
        }
        String htsqrq = "";
        if (dateByStringDay != null) {
            htsqrq = DateUtil.dateToStringDay(dateByStringDay).replaceFirst("-", "年").replaceFirst("-", "月") + "日";
        }
        data.put("htsqrq", htsqrq);
        data.put("ysqk", htglEsu.getYsqk());
        data.put("zdsxjttlqk", htglEsu.getZdsxjttlqk());
        return data;
    }


}
