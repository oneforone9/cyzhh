package com.essence.web.portal;

import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.StLevelRainService;
import com.essence.interfaces.model.StCompanyBaseEsu;
import com.essence.interfaces.model.StLevelRainEsr;
import com.essence.interfaces.model.StLevelRainEsu;
import com.essence.interfaces.param.StLevelRainEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


/**
 * 雨量等级
 * @author BINX
 * @since 2023-03-08 11:31:33
 */
@RestController
@RequestMapping("/stLevelRain")
public class StLevelRainController extends BaseController<Long, StLevelRainEsu, StLevelRainEsp, StLevelRainEsr> {
    @Autowired
    private StLevelRainService stLevelRainService;

    public StLevelRainController(StLevelRainService stLevelRainService) {
        super(stLevelRainService);
    }


    /**
     * 编辑小时降雨量
     *
     * @param list
     * @return
     */
    @PostMapping("/editStLevelRain")
    public ResponseResult editStLevelRain(HttpServletRequest request, @RequestBody List<StLevelRainEsu> list) {
        //根据主键id进行修
        return ResponseResult.success("修改成功", stLevelRainService.editStLevelRain(list));

    }
}
