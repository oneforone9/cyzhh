package com.essence.web.portal;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.essence.common.constant.ItemConstant;
import com.essence.common.utils.ResponseResult;
import com.essence.euauth.common.SysConstant;
import com.essence.euauth.common.util.StringUtil;
import com.essence.interfaces.api.StOfficeContactService;
import com.essence.interfaces.entity.Criterion;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.StOfficeContactEsr;
import com.essence.interfaces.model.StOfficeContactEsu;
import com.essence.interfaces.model.StOfficeContactEsuParam;
import com.essence.interfaces.param.StOfficeContactEsp;
import com.essence.web.basecontroller.BaseController;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 管常用联系人表理
 * @author liwy
 * @since 2023-03-29 18:49:35
 */
@RestController
@RequestMapping("/stOfficeContact")
public class StOfficeContactController extends BaseController<Long, StOfficeContactEsu, StOfficeContactEsp, StOfficeContactEsr> {
    @Autowired
    private StOfficeContactService stOfficeContactService;

    public StOfficeContactController(StOfficeContactService stOfficeContactService) {
        super(stOfficeContactService);
    }

    /**
     * 获取当前登录人的常用联系人
     *
     * @param stOfficeContactEsuParam
     * @return
     */
    @PostMapping("/searchByUserId")
    @ResponseBody
    public ResponseResult<Paginator<StOfficeContactEsr>> searchByUserId(HttpServletRequest request, @RequestBody StOfficeContactEsuParam stOfficeContactEsuParam) {
        PaginatorParam paginatorParam;
        PaginatorParam param = new PaginatorParam();
        param.setPageSize(0);
        param.setCurrentPage(0);
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        //测试数据
        //userId ="23130f35ccf04fa28cdf058b7d67a03b";
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-收藏常用联系人失败");
        }
        List<Criterion> currency = param.getCurrency();
        if (CollectionUtils.isEmpty(currency)){
            currency = new ArrayList<>();
        }
        //获取当前登录人的
        Criterion criterion2 = new Criterion();
        criterion2.setFieldName("userId");
        criterion2.setOperator(Criterion.EQ);
        criterion2.setValue(userId);
        currency.add(criterion2);
        param.setCurrency(currency);
        return ResponseResult.success("获取常用联系人成功", stOfficeContactService.searchByUserId(stOfficeContactEsuParam, param));
    }

    /**
     * 收藏常用联系人
     *
     * @param stOfficeContactEsu
     * @return
     */
    @PostMapping("/addStOfficeContact")
    @ResponseBody
    public ResponseResult addStOfficeContact(HttpServletRequest request, @RequestBody StOfficeContactEsu stOfficeContactEsu) {
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        //测试数据
        //userId ="5b39b741f7d342f3ab6e64037c336e49";
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-收藏常用联系人失败");
        }
        stOfficeContactEsu.setUserId(userId);
        stOfficeContactEsu.setCreator(userId);
        stOfficeContactEsu.setGmtCreate(new Date());
        ResponseResult result = stOfficeContactService.addStOfficeContact(stOfficeContactEsu);
        return result;
    }



    /**
     * 取消收藏常用联系人
     *
     * @param id
     * @return
     */
    @GetMapping("/deleteStOfficeContact")
    @ResponseBody
    public ResponseResult deleteStOfficeContact(HttpServletRequest request, @RequestParam Integer id) {
        return ResponseResult.success("取消收藏常用联系人成功", stOfficeContactService.deleteStOfficeContact(id));
    }

}
