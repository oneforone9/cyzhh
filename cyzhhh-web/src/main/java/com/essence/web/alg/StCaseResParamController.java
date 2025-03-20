package com.essence.web.alg;


import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.StCaseResParamService;
import com.essence.interfaces.model.StCaseResParamEsr;
import com.essence.interfaces.model.StCaseResParamEsu;
import com.essence.interfaces.param.StCaseResParamEsp;
import com.essence.interfaces.vaild.Insert;
import com.essence.web.basecontroller.BaseController;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 方案执行结果表入参表
 *
 * @author BINX
 * @since 2023年4月28日 下午5:45:25
 */
@RestController
@RequestMapping("/stCaseResParam")
@Slf4j
public class StCaseResParamController extends BaseController<String, StCaseResParamEsu, StCaseResParamEsp, StCaseResParamEsr> {

    @Autowired
    private StCaseResParamService stCaseResParamService;

    public StCaseResParamController(StCaseResParamService stCaseResParamService) {
        super(stCaseResParamService);
    }

    @PostMapping("/addList")
    @ResponseBody
    public ResponseResult insert(@Validated(Insert.class) @RequestBody List<StCaseResParamEsu> list) {

        if (list == null || list.size() == 0) {
            return ResponseResult.success("添加成功", 0);
        }

        list.forEach(stCaseResParamEsu -> {
//            stCaseResParamEsu.setId(UUID.randomUUID().toString().replace("-",""));
            stCaseResParamEsu.setCreateTime(new Date());
        });
        stCaseResParamService.saveOrUpdate(list);
        return ResponseResult.success("添加成功", list.size());

    }
}
