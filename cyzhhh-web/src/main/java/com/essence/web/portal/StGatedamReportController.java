package com.essence.web.portal;


import com.essence.common.constant.ItemConstant;
import com.essence.common.utils.ResponseResult;
import com.essence.euauth.common.SysConstant;
import com.essence.euauth.common.util.StringUtil;
import com.essence.interfaces.api.StGatedamReportService;
import com.essence.interfaces.entity.Criterion;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.StGatedamReportEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 闸坝运行维保日志上报表管理
 * @author liwy
 * @since 2023-03-15 11:55:47
 */
@RestController
@RequestMapping("/stGatedamReport")
public class StGatedamReportController extends BaseController<Long, StGatedamReportEsu, StGatedamReportEsp, StGatedamReportEsr> {
    @Autowired
    private StGatedamReportService stGatedamReportService;

    public StGatedamReportController(StGatedamReportService stGatedamReportService) {
        super(stGatedamReportService);
    }

    /**
     * 新增养维护修日志
     *
     * @param stGatedamReportEsu
     * @return
     */
    @PostMapping("/addStGatedamReport")
    public ResponseResult addStGatedamReport(HttpServletRequest request, @RequestBody StGatedamReportEsu stGatedamReportEsu) {
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        //测试数据
        //userId ="d361baabb572458faea54fe25d152556";
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-添加失败");
        }
        //先新增闸坝运行维保日志上报表
        stGatedamReportEsu.setCreator(userId);
        stGatedamReportEsu.setGmtCreate(new Date());
        return ResponseResult.success("新增养维护修日志成功", stGatedamReportService.addStGatedamReport(stGatedamReportEsu));
    }

    /**
     * 养维护休日志详情
     *
     * @param id
     * @return
     */
    @PostMapping("/searchById/{id}")
    public ResponseResult searchById(HttpServletRequest request, @PathVariable String id) {
        return ResponseResult.success("查看日志详情成功", stGatedamReportService.searchById(id));
    }


    /**
     *小程序-获取当前登录人上报的闸坝运行维保日志上报
     *
     * @param param
     * @return
     */
    @PostMapping("/searchByUserId")
    @ResponseBody
    public ResponseResult<Paginator<StGatedamReportEsr>> searchByUserId(HttpServletRequest request, @RequestBody PaginatorParam param) {
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        //测试数据
        //userId ="d361baabb572458faea54fe25d152556";
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-添加失败");
        }
        List<Criterion> currency = param.getCurrency();
        if (CollectionUtils.isEmpty(currency)){
            currency = new ArrayList<>();
        }
        Criterion criterion = new Criterion();
        criterion.setFieldName("isDelete");
        criterion.setOperator(Criterion.EQ);
        criterion.setValue(ItemConstant.REPORT_NO_DELETE);
        currency.add(criterion);
        //获取当前登录人的
        Criterion criterion2 = new Criterion();
        criterion2.setFieldName("creator");
        criterion2.setOperator(Criterion.EQ);
        criterion2.setValue(userId);
        currency.add(criterion2);
        param.setCurrency(currency);
        return ResponseResult.success("查询成功", stGatedamReportService.findByPaginator(param));
    }

    /**
     *PC端-闸坝运行养护工作报告汇总列表
     *
     * @param stGatedamReportParam
     * @return
     */
    @PostMapping("/searchAll")
    @ResponseBody
    public ResponseResult<Paginator<StGatedamReportEsr>> searchAll(@RequestBody StGatedamReportParam stGatedamReportParam) {
        return ResponseResult.success("查询成功", stGatedamReportService.searchAll(stGatedamReportParam));
    }

    /**
     * 作业日历
     * @param request
     * @param stGatedamReportParam
     * @return
     */
    @PostMapping("/searchByCondition")
    public ResponseResult searchByCondition(HttpServletRequest request, @RequestBody StGatedamReportParam2 stGatedamReportParam) {
        return ResponseResult.success("查看作业日历成功", stGatedamReportService.searchByCondition(stGatedamReportParam));
    }


    /**
     * 闸坝运行养护统计
     * @param request
     * @param stBRiverId
     * @return
     */

    @GetMapping("/searchCount")
    public ResponseResult searchCount(HttpServletRequest request, @RequestParam String stBRiverId, @RequestParam String unitId) {
        return ResponseResult.success("闸坝运行养护统计成功", stGatedamReportService.searchCount(stBRiverId, unitId));
    }

    /**
     * 闸坝运行养护统计_从闸坝养护计划工单获取数据
     * @param request
     * @param stBRiverId
     * @return
     */

    @GetMapping("/searchCountNew")
    public ResponseResult searchCountNew(HttpServletRequest request, @RequestParam String stBRiverId, @RequestParam String unitId) {
        return ResponseResult.success("闸坝运行养护统计成功", stGatedamReportService.searchCountNew(stBRiverId, unitId));
    }


    /**
     * 获取单个测站最新运行养护记录
     * @param request
     * @param stcd
     * @param stnm
     * @return
     */
    @GetMapping("/searchNewReport")
    public ResponseResult searchReportNew(HttpServletRequest request, @RequestParam String stcd,@RequestParam String stnm) {
        return ResponseResult.success("获取单个测站最新运行养护记录成功", stGatedamReportService.searchReportNew(stcd, stnm));
    }

    /**
     * 获取单个测站最新运行养护记录_从闸坝养护计划工单获取数据
     * @param request
     * @param stcd
     * @param stnm
     * @return
     */
    @GetMapping("/searchNewReportYh")
    public ResponseResult searchNewReportYh(HttpServletRequest request, @RequestParam String stcd,@RequestParam String stnm) {
        return ResponseResult.success("获取单个测站最新运行养护记录成功", stGatedamReportService.searchNewReportYh(stcd, stnm));
    }


}
