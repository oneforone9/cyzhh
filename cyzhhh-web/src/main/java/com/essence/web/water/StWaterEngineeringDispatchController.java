package com.essence.web.water;

import com.essence.common.utils.ResponseResult;
import com.essence.euauth.common.util.StringUtil;
import com.essence.interfaces.api.StWaterEngineeringDispatchService;
import com.essence.interfaces.model.StWaterEngineeringDispatchEsr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 水系联调-工程调度-调度预案管理
 * @author majunjie
 * @since 2023-06-02 12:39:04
 */
@RestController
@RequestMapping("/stWaterEngineeringDispatch")
public class StWaterEngineeringDispatchController  {
    @Autowired
    private StWaterEngineeringDispatchService stWaterEngineeringDispatchService;
    /**
     * 调度预案查询
     *
     * @param stId
     * @return
     */
    @GetMapping("/stDispatch/{stId}")
    public ResponseResult<StWaterEngineeringDispatchEsr> selectDispatchByStId(@PathVariable String stId) {
        if (StringUtil.isNotBlank(stId)) {
            return ResponseResult.success("查询成功", stWaterEngineeringDispatchService.selectDispatchByStId(stId));
        } else {
            return ResponseResult.error("参数有误！");
        }
    }

}
