package com.essence.web.portal;

import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.StEcologyWaterService;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.StCompanyBaseEsr;
import com.essence.interfaces.model.StEcologyWaterEsr;
import com.essence.interfaces.model.StEcologyWaterEsu;
import com.essence.interfaces.param.StEcologyWaterEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 生态需水量
 * @author BINX
 * @since 2023-02-21 16:33:33
 */
@RestController
@RequestMapping("/stEcologyWater")
public class StEcologyWaterController extends BaseController<Long, StEcologyWaterEsu, StEcologyWaterEsp, StEcologyWaterEsr> {
    @Autowired
    private StEcologyWaterService stEcologyWaterService;

    public StEcologyWaterController(StEcologyWaterService stEcologyWaterService) {
        super(stEcologyWaterService);
    }

    /**
     * 北部、中部、南部 汇总查询
     * @param request
     * @return
     */
    @GetMapping("/count")
    public ResponseResult count(HttpServletRequest request) {
            return ResponseResult.success("查询成功", stEcologyWaterService.count());

    }

}
