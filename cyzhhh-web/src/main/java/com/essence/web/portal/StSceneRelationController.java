package com.essence.web.portal;


import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.essence.common.utils.ResponseResult;
import com.essence.euauth.common.SysConstant;
import com.essence.euauth.common.util.StringUtil;
import com.essence.interfaces.api.StSceneRelationService;
import com.essence.interfaces.entity.Criterion;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.*;
import com.essence.interfaces.param.StSceneRelationEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 场景关联表管理
 * @author liwy
 * @since 2023-06-01 14:48:15
 */
@RestController
@RequestMapping("/stSceneRelation")
public class StSceneRelationController extends BaseController<Long, StSceneRelationEsu, StSceneRelationEsp, StSceneRelationEsr> {
    @Autowired
    private StSceneRelationService stSceneRelationService;

    public StSceneRelationController(StSceneRelationService stSceneRelationService) {
        super(stSceneRelationService);
    }


    /**
     * 收藏摄像头到场景
     *
     * @param stSceneRelationEsu
     * @return
     */
    @PostMapping("/addStSceneRelation")
    @ResponseBody
    public ResponseResult searchByUserId(HttpServletRequest request, @RequestBody StSceneRelationEsu stSceneRelationEsu) {
        stSceneRelationEsu.setGmtCreate(new Date());
        ResponseResult result = stSceneRelationService.addStSceneRelation(stSceneRelationEsu);
        return result;
    }

    /**
     * 删除收藏的摄像头
     *
     * @param stSceneRelationEsu
     * @return
     */
    @PostMapping("/deleteStSceneRelation")
    @ResponseBody
    public ResponseResult deleteStSceneRelation(HttpServletRequest request, @RequestBody StSceneRelationEsu stSceneRelationEsu) {
        int i = stSceneRelationService.deleteById(stSceneRelationEsu.getId());
        return  ResponseResult.success("删除收藏的摄像头成功",i);
    }


}
