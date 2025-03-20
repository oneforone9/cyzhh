package com.essence.web.portal;

import com.essence.common.constant.ItemConstant;
import com.essence.common.utils.ResponseResult;
import com.essence.euauth.common.SysConstant;
import com.essence.euauth.common.util.StringUtil;
import com.essence.interfaces.api.StFloodReportService;
import com.essence.interfaces.entity.Criterion;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.StFloodReportEsr;
import com.essence.interfaces.model.StFloodReportEsu;
import com.essence.interfaces.param.StFloodReportEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 汛情上报表管理
 * @author liwy
 * @since 2023-03-13 14:25:21
 */
@RestController
@RequestMapping("/stFloodReport")
public class StFloodReportController extends BaseController<Long, StFloodReportEsu, StFloodReportEsp, StFloodReportEsr> {
    @Autowired
    private StFloodReportService stFloodReportService;

    public StFloodReportController(StFloodReportService stFloodReportService) {
        super(stFloodReportService);
    }


    /**
     * 添加汛情
     *
     * @param stFloodReportEsu
     * @return
     */
    @PostMapping("/addStFloodReport")
    public ResponseResult addStFloodReport(HttpServletRequest request, @RequestBody StFloodReportEsu stFloodReportEsu) {
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        String userName= (String) request.getSession().getAttribute(SysConstant.CURRENT_USERNAME);
        //userId ="11";
        //先新增汛情上报表
        stFloodReportEsu.setReportPerson(userName);
        stFloodReportEsu.setCreator(userId);
        stFloodReportEsu.setGmtCreate(new Date());
        return ResponseResult.success("添加汛情成功", stFloodReportService.addStFloodReport(stFloodReportEsu));

    }


    /**
     * 汛情详情
     *
     * @param id
     * @return
     */
    @PostMapping("/searchById/{id}")
    public ResponseResult searchById(HttpServletRequest request, @PathVariable String id) {
        return ResponseResult.success("查看汛情成功", stFloodReportService.searchById(id));

    }

    /**
     *小程序-获取当前登录人上报的汛情
     *
     * @param param
     * @return
     */
    @PostMapping("/searchByUserId")
    @ResponseBody
    public ResponseResult<Paginator<StFloodReportEsr>> searchByUserId(HttpServletRequest request, @RequestBody PaginatorParam param) {
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        //userId ="11";
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
        return ResponseResult.success("查询成功", stFloodReportService.findByPaginator(param));
    }


    /**
     *PC端-取所有上报的汛情
     *
     * @param param
     * @return
     */
    @PostMapping("/searchAll")
    @ResponseBody
    public ResponseResult<Paginator<StFloodReportEsr>> searchAll(HttpServletRequest request, @RequestBody PaginatorParam param) {
        List<Criterion> currency = param.getCurrency();
        if (CollectionUtils.isEmpty(currency)){
            currency = new ArrayList<>();
        }
        Criterion criterion = new Criterion();
        criterion.setFieldName("isDelete");
        criterion.setOperator(Criterion.EQ);
        criterion.setValue(ItemConstant.REPORT_NO_DELETE);
        currency.add(criterion);
        return ResponseResult.success("查询成功", stFloodReportService.findByPaginator(param));
    }
}
