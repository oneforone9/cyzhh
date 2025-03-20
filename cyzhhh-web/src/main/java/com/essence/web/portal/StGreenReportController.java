package com.essence.web.portal;

import com.essence.common.constant.ItemConstant;
import com.essence.common.utils.PageUtil;
import com.essence.common.utils.ResponseResult;
import com.essence.dao.entity.StGreenReportDto;
import com.essence.euauth.common.SysConstant;
import com.essence.euauth.common.util.StringUtil;
import com.essence.interfaces.api.StGreenReportService;
import com.essence.interfaces.entity.Criterion;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.StGreenReportEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 绿化保洁工作日志上报表管理
 * @author liwy
 * @since 2023-03-14 15:34:09
 */
@RestController
@RequestMapping("/stGreenReport")
public class StGreenReportController extends BaseController<Long, StGreenReportEsu, StGreenReportEsp, StGreenReportEsr> {
    @Autowired
    private StGreenReportService stGreenReportService;

    public StGreenReportController(StGreenReportService stGreenReportService) {
        super(stGreenReportService);
    }

    /**
     * 新增日志
     *
     * @param stGreenReportEsu
     * @return
     */
    @PostMapping("/addStGreenReport")
    public ResponseResult addStGreenReport(HttpServletRequest request, @RequestBody StGreenReportEsu stGreenReportEsu) {
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        //测试数据
        //userId ="d361baabb572458faea54fe25d152556";
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-添加失败");
        }

        //先新增绿化保洁工作日志上报表
        stGreenReportEsu.setCreator(userId);
        stGreenReportEsu.setGmtCreate(new Date());
        return ResponseResult.success("新增日志成功", stGreenReportService.addStGreenReport(stGreenReportEsu));

    }


    /**
     * 日志详情
     *
     * @param id
     * @return
     */
    @PostMapping("/searchById/{id}")
    public ResponseResult searchById(HttpServletRequest request, @PathVariable String id) {
        return ResponseResult.success("查看日志详情成功", stGreenReportService.searchById(id));

    }


    /**
     *小程序-获取当前登录人上报的绿化保洁工作日志
     *
     * @param param
     * @return
     */
    @PostMapping("/searchByUserId")
    @ResponseBody
    public ResponseResult<Paginator<StGreenReportEsr>> searchByUserId(HttpServletRequest request, @RequestBody PaginatorParam param) {
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
        return ResponseResult.success("查询成功", stGreenReportService.findByPaginator(param));
    }

    /**
     *PC端-绿化保洁工作报告汇总列表
     *
     * @param stGreenReportParam
     * @return
     */
    @PostMapping("/searchAll")
    @ResponseBody
    public ResponseResult<Paginator<StGreenReportEsr> > searchAll(@RequestBody StGreenReportParam stGreenReportParam) {
        return ResponseResult.success("查询成功", stGreenReportService.searchAll(stGreenReportParam));
    }

    /**
     * 作业日历
     * @param request
     * @param stGreenReportParam
     * @return
     */
    @PostMapping("/searchByCondition")
    public ResponseResult searchByCondition(HttpServletRequest request, @RequestBody StGreenReportParam2 stGreenReportParam) {
        return ResponseResult.success("查看作业日历成功", stGreenReportService.searchByCondition(stGreenReportParam));

    }
}
