package com.essence.web.data;

import com.essence.common.dto.UserWaterStatisticDto;
import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.UserWaterBaseInfoService;
import com.essence.interfaces.api.UserWaterService;
import com.essence.interfaces.model.UserWaterBaseInfoEsr;
import com.essence.interfaces.model.UserWaterBaseInfoEsu;
import com.essence.interfaces.param.UserWaterBaseInfoEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * 用水户取水量管理
 * @author BINX
 * @since 2023-01-04 17:50:28
 */
@RestController
@RequestMapping("/userWaterBaseInfo")
public class UserWaterBaseInfoController extends BaseController<String, UserWaterBaseInfoEsu, UserWaterBaseInfoEsp, UserWaterBaseInfoEsr> {
    @Autowired
    private UserWaterBaseInfoService userWaterBaseInfoService;

    public UserWaterBaseInfoController(UserWaterBaseInfoService userWaterBaseInfoService) {
        super(userWaterBaseInfoService);
    }

    /**
     * 用水户取水量管理 统计
     *
     * @return
     */
    @GetMapping("/statistic")
    public ResponseResult<List<UserWaterStatisticDto>> getStatistic(String type) throws IOException {
        return ResponseResult.success("查询成功",userWaterBaseInfoService.selectByType(type));
    }
}
