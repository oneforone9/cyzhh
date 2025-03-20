package com.essence.web.portal;

import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.StLevelWaterService;
import com.essence.interfaces.model.StLevelRainEsu;
import com.essence.interfaces.model.StLevelWaterEsr;
import com.essence.interfaces.model.StLevelWaterEsu;
import com.essence.interfaces.param.StLevelWaterEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 积水等级
 * @author BINX
 * @since 2023-03-08 11:32:30
 */
@RestController
@RequestMapping("/stLevelWater")
public class StLevelWaterController extends BaseController<Long, StLevelWaterEsu, StLevelWaterEsp, StLevelWaterEsr> {
    @Autowired
    private StLevelWaterService stLevelWaterService;

    public StLevelWaterController(StLevelWaterService stLevelWaterService) {
        super(stLevelWaterService);
    }


    /**
     * 编辑积水深度
     *
     * @param list
     * @return
     */
    @PostMapping("/editStLevelWater")
    public ResponseResult editStLevelWater(HttpServletRequest request, @RequestBody List<StLevelWaterEsu> list) {
        //根据主键id进行修
        return ResponseResult.success("修改成功", stLevelWaterService.editStLevelWater(list));

    }
}
