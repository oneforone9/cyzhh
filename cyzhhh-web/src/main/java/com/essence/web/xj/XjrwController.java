package com.essence.web.xj;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.essence.common.utils.ResponseResult;
import com.essence.euauth.common.SysConstant;
import com.essence.euauth.common.util.StringUtil;
import com.essence.interfaces.api.XjrwService;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 设备巡检任务管理0108
 * @author majunjie
 * @since 2025-01-09 15:19:32
 */
@RestController
@RequestMapping("/xjrw")
@Slf4j
public class XjrwController {
    @Autowired
    private XjrwService xjrwService;
    /**
     * 巡检任务/问题追踪根据条件分页查询
     *
     * @param param
     * @return Paginator<R>
     */
    @PostMapping("/searchXjrw")
    public ResponseResult<Paginator<XjrwEsr>> searchVideo(@RequestBody PaginatorParam param) {
        return ResponseResult.success("查询成功", xjrwService.findByPaginator(param));
    }
    /**
     * 巡检任务新增
     *
     * @param xjrwEsu
     * @return
     */
    @PostMapping("/addXjrw")
    public ResponseResult<XjrwEsr> addXjrw(HttpServletRequest request, @RequestBody XjrwEsu xjrwEsu) {
       String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        String userName = (String) request.getSession().getAttribute(SysConstant.CURRENT_USERNAME);
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-添加失败");
        }
        return ResponseResult.success("新增成功", xjrwService.addXjrw(xjrwEsu,userName));
    }
    /**
     * 巡检任务修改
     *
     * @param xjrwEsu
     * @return
     */
    @PostMapping("/updateXjrw")
    public ResponseResult<XjrwEsr> updateXjrw(HttpServletRequest request,@RequestBody XjrwEsu xjrwEsu) {
       String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        String userName = (String) request.getSession().getAttribute(SysConstant.CURRENT_USERNAME);
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-添加失败");
        }
        return ResponseResult.success("修改成功", xjrwService.updateXjrw(xjrwEsu,userName));
    }
    /**
     * 巡检流程(工单全流程)根据条件分页查询
     *
     * @param param
     * @return Paginator<R>
     */
    @PostMapping("/searchXjlc")
    public ResponseResult<Paginator<XjrwlcEsr>> searchXjlc(@RequestBody PaginatorParam param) {
        return ResponseResult.success("查询成功", xjrwService.searchXjlc(param));
    }

    private String findLy(String ly){
        String lyStr="";
        switch (ly){
            case "0":
                lyStr="计划生成";
                break;
            case "1":
                lyStr="临时生成";
                break;
            case "2":
                lyStr="问题上报";
                break;
            default:
                break;
        }
        return lyStr;
    }
    private String findWcqk(String wcqk){
        String wcqkStr="";
        switch (wcqk){
            case "0":
                wcqkStr="未完成";
                break;
            case "1":
                wcqkStr="已完成";
                break;
            case "2":
                wcqkStr="超期完成";
                break;
            default:
                break;
        }
        return wcqkStr;
    }
    /**
     * 摄像头巡检任务-导出
     *
     * @param response
     * @param param
     */
    @PostMapping("/exportSxtXjrw")
    public void exportSxtXjrw(HttpServletResponse response, @RequestBody PaginatorParam param) {
        try {
            Paginator<XjrwEsr> byPaginator = xjrwService.findByPaginator(param);
            List<XjrwEsr> items = byPaginator.getItems();
          List<XjrwSxt>xjrwSxts=new ArrayList<>();
            if (!CollectionUtils.isEmpty(items)){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                for (XjrwEsr xjrwEsr : items) {
                    XjrwSxt xjrwSxt=new XjrwSxt();
                    xjrwSxt.setBh(xjrwEsr.getBh());
                    xjrwSxt.setLy(findLy(xjrwEsr.getLy()));
                    xjrwSxt.setMc(xjrwEsr.getMc());
                    xjrwSxt.setRiverName(xjrwEsr.getRiverName());
                    xjrwSxt.setFzr(xjrwEsr.getFzr());
                    xjrwSxt.setJhsj(sdf.format(xjrwEsr.getJhsj()));
                    if (null!=xjrwEsr.getSjsj()){
                        xjrwSxt.setSjsj(sdf.format(xjrwEsr.getSjsj()));
                    }
                    xjrwSxt.setWcqk(findWcqk(String.valueOf(xjrwEsr.getWcqk())));
                    xjrwSxt.setFxwt(String.valueOf(xjrwEsr.getFxwt()).equals("0")?"否":"是");
                    xjrwSxts.add(xjrwSxt);
                }
            }
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("摄像头巡检任务(" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ")", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            WriteCellStyle headWriteCellStyle = new WriteCellStyle();
            headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
            WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
            contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
            HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
            EasyExcel.write(response.getOutputStream(), XjrwSxt.class).sheet("摄像头巡检任务")
                    .registerWriteHandler(horizontalCellStyleStrategy).doWrite(xjrwSxts);
        } catch (Exception e) {
            log.error("摄像头巡检任务导出异常！exportSxtXjrw{}" + e);
        }
    }
    /**
     * 会议室巡检任务-导出
     *
     * @param response
     * @param param
     */
    @PostMapping("/exportXysXjrw")
    public void exportXysXjrw(HttpServletResponse response, @RequestBody PaginatorParam param) {
        try {
            Paginator<XjrwEsr> byPaginator = xjrwService.findByPaginator(param);
            List<XjrwEsr> items = byPaginator.getItems();
            List<XjrwHys>xjrwHyss=new ArrayList<>();
            if (!CollectionUtils.isEmpty(items)){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                for (XjrwEsr xjrwEsr : items) {
                    XjrwHys xjrwHys=new XjrwHys();
                    xjrwHys.setBh(xjrwEsr.getBh());
                    xjrwHys.setLy(findLy(xjrwEsr.getLy()));
                    xjrwHys.setMc(xjrwEsr.getMc());
                    xjrwHys.setFzr(xjrwEsr.getFzr());
                    xjrwHys.setJhsj(sdf.format(xjrwEsr.getJhsj()));
                    if (null!=xjrwEsr.getSjsj()){
                        xjrwHys.setSjsj(sdf.format(xjrwEsr.getSjsj()));
                    }
                    xjrwHys.setWcqk(findWcqk(String.valueOf(xjrwEsr.getWcqk())));
                    xjrwHys.setFxwt(String.valueOf(xjrwEsr.getFxwt()).equals("0")?"否":"是");
                    xjrwHyss.add(xjrwHys);
                }
            }
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("会议室巡检任务(" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ")", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            WriteCellStyle headWriteCellStyle = new WriteCellStyle();
            headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
            WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
            contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
            HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
            EasyExcel.write(response.getOutputStream(), XjrwHys.class).sheet("会议室巡检任务")
                    .registerWriteHandler(horizontalCellStyleStrategy).doWrite(xjrwHyss);
        } catch (Exception e) {
            log.error("会议室巡检任务导出异常！exportXysXjrw{}" + e);
        }
    }
    /**
     * 问题巡检任务-导出
     *
     * @param response
     * @param param
     */
    @PostMapping("/exportWt")
    public void exportWt(HttpServletResponse response, @RequestBody PaginatorParam param) {
        try {
            Paginator<XjrwEsr> byPaginator = xjrwService.findByPaginator(param);
            List<XjrwEsr> items = byPaginator.getItems();
            List<XjrwSxtWt>xjrwSxtWts=new ArrayList<>();
            if (!CollectionUtils.isEmpty(items)){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                for (XjrwEsr xjrwEsr : items) {
                    XjrwSxtWt xjrwSxtWt=new XjrwSxtWt();
                    xjrwSxtWt.setBh(xjrwEsr.getBh());
                    xjrwSxtWt.setMc(xjrwEsr.getMc());
                    xjrwSxtWt.setRiverName(xjrwEsr.getRiverName());
                    xjrwSxtWt.setWtms(xjrwEsr.getWtms());
                    xjrwSxtWt.setDkdz(xjrwEsr.getDkdz());
                    xjrwSxtWt.setFxsj(sdf.format(xjrwEsr.getFxsj()));
                    if (null!=xjrwEsr.getPfsj()){
                        xjrwSxtWt.setPfsj(sdf.format(xjrwEsr.getPfsj()));
                    }
                    if (null!=xjrwEsr.getJjsj()){
                        xjrwSxtWt.setJjsj(sdf.format(xjrwEsr.getJjsj()));
                    }
                    xjrwSxtWt.setWcqk(findWcqk(String.valueOf(xjrwEsr.getWcqk())));
                    xjrwSxtWts.add(xjrwSxtWt);
                }
            }
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("问题巡检任务(" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ")", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            WriteCellStyle headWriteCellStyle = new WriteCellStyle();
            headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
            WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
            contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
            HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
            EasyExcel.write(response.getOutputStream(), XjrwSxtWt.class).sheet("问题巡检任务")
                    .registerWriteHandler(horizontalCellStyleStrategy).doWrite(xjrwSxtWts);
        } catch (Exception e) {
            log.error("问题巡检任务导出异常！exportWt{}" + e);
        }
    }
}
