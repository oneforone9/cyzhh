package com.essence.web.third;


import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.third.StThirdUserInfoService;
import com.essence.interfaces.api.third.StThirdUserRelationService;
import com.essence.interfaces.model.StThirdUserRelationEsr;
import com.essence.interfaces.model.StThirdUserRelationEsu;
import com.essence.interfaces.model.SysDictionaryDataEsr;
import com.essence.interfaces.model.SysDictionaryDataEsu;
import com.essence.interfaces.param.StThirdUserRelationEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 管理
 * @author BINX
 * @since 2023-04-04 14:45:08
 */
@RestController
@RequestMapping("/stThirdUserRelation")
public class StThirdUserRelationController extends BaseController<String, StThirdUserRelationEsu, StThirdUserRelationEsp, StThirdUserRelationEsr> {

    @Autowired
    private StThirdUserInfoService stThirdUserInfoService;

    public StThirdUserRelationController(StThirdUserRelationService stThirdUserRelationService) {
        super(stThirdUserRelationService);
    }

    /**
     * 新增123
     *
     * @param sysDictionaryDataEsu
     * @return
     */
    @PostMapping("/adds")
    public ResponseResult<SysDictionaryDataEsr> addSysDictionaryData(HttpServletRequest request, @RequestBody SysDictionaryDataEsu sysDictionaryDataEsu) {

        return ResponseResult.success("添加成功", null);
    }


}
