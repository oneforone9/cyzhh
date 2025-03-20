package com.essence.web.data;

import com.essence.common.dto.UserWaterStatisticDto;
import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.UserWaterService;
import com.essence.interfaces.dot.EffectCaseStatisticDto;
import com.essence.interfaces.dot.EffectRequestDto;
import com.essence.interfaces.model.UserWaterEsr;
import com.essence.interfaces.model.UserWaterEsu;
import com.essence.interfaces.param.UserWaterEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 用水户取水量管理
 * @author BINX
 * @since 2023-01-04 17:50:28
 */
@RestController
@RequestMapping("/userWater")
public class UserWaterController extends BaseController<String, UserWaterEsu, UserWaterEsp, UserWaterEsr> {
    @Autowired
    private UserWaterService userWaterService;

    public UserWaterController(UserWaterService userWaterService) {
        super(userWaterService);
    }

    /**
     * 用水户取水量管理 统计
     *
     * @return
     */
    @GetMapping("/statistic")
    public ResponseResult<List<UserWaterStatisticDto>> getStatistic() throws IOException {
        List<UserWaterStatisticDto> list = userWaterService.getStatistic();
        return ResponseResult.success("查询成功",list);
    }

    /**
     * 用水户基本数据统计
     *
     * @return
     */
    @GetMapping("/statistic2")
    public ResponseResult<List<UserWaterStatisticDto>> getStatistic2(String fileType) throws IOException {
        return ResponseResult.success("查询成功",userWaterService.getStatistic2(fileType));
    }

    @PostMapping("import")
    public void inputExcel(MultipartFile file) throws IOException {
        userWaterService.inputExcel(file);
    }
}
