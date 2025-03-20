package com.essence.web.portal;


import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.essence.common.utils.ResponseResult;
import com.essence.euauth.common.SysConstant;
import com.essence.euauth.common.util.StringUtil;
import com.essence.interfaces.api.StSceneBaseService;
import com.essence.interfaces.entity.Criterion;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.StOfficeContactEsu;
import com.essence.interfaces.model.StSceneBaseEsr;
import com.essence.interfaces.model.StSceneBaseEsu;
import com.essence.interfaces.param.StSceneBaseEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 场景基本表管理
 * @author liwy
 * @since 2023-06-01 14:47:33
 */
@RestController
@RequestMapping("/stSceneBase")
public class StSceneBaseController extends BaseController<Long, StSceneBaseEsu, StSceneBaseEsp, StSceneBaseEsr> {
    @Autowired
    private StSceneBaseService stSceneBaseService;

    public StSceneBaseController(StSceneBaseService stSceneBaseService) {
        super(stSceneBaseService);
    }


    /**
     * 获取当前登录人创建的场景
     * @return
     */
    @PostMapping("/selectStSceneBase")
    @ResponseBody
    public ResponseResult selectStSceneBase(HttpServletRequest request) {
        PaginatorParam param = new PaginatorParam();
        param.setPageSize(0);
        param.setCurrentPage(0);
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        //测试数据
        //userId ="5b39b741f7d342f3ab6e64037c336e49";
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-获取场景失败");
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
        ResponseResult<List<StSceneBaseEsr>> listResponseResult = stSceneBaseService.selectStSceneBase(param);
        return listResponseResult;
    }

    /**
     * 新增/编辑场景（id不为空时编辑）
     *
     * @param stSceneBaseEsu
     * @return
     */
    @PostMapping("/addStSceneBase")
    @ResponseBody
    public ResponseResult addStSceneBase(HttpServletRequest request, @RequestBody StSceneBaseEsu stSceneBaseEsu) {
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        //测试数据
        //userId ="5b39b741f7d342f3ab6e64037c336e49";
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-新增场景失败");
        }
        stSceneBaseEsu.setUserId(userId);
        stSceneBaseEsu.setGmtCreate(new Date());
        ResponseResult result = stSceneBaseService.addStSceneBase(stSceneBaseEsu);
        return result;
    }


    /**
     * 删除场景
     *
     * @param stSceneBaseEsu
     * @return
     */
    @Transactional
    @PostMapping("/deleteStSceneBase")
    @ResponseBody
    public ResponseResult deleteStSceneBase(HttpServletRequest request, @RequestBody StSceneBaseEsu stSceneBaseEsu) {
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        //测试数据
        //userId ="5b39b741f7d342f3ab6e64037c336e49";
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-删除场景失败");
        }
        ResponseResult result = stSceneBaseService.deleteStSceneBase(stSceneBaseEsu);
        return result;
    }


}
