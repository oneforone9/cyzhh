package com.essence.web.portal;

import com.essence.common.dto.StatisticsDto;
import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.CyCaseBaseService;
import com.essence.interfaces.model.CyCaseBaseEsr;
import com.essence.interfaces.model.CyCaseBaseEsu;
import com.essence.interfaces.param.CyCaseBaseEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 案件基础表管理
 *
 * @author zhy
 * @since 2023-01-04 18:13:36
 */
@Controller
@RequestMapping("/case")
public class CyCaseBaseController extends BaseController<String, CyCaseBaseEsu, CyCaseBaseEsp, CyCaseBaseEsr> {
    @Autowired
    private CyCaseBaseService cyCaseBaseService;

    public CyCaseBaseController(CyCaseBaseService cyCaseBaseService) {
        super(cyCaseBaseService);
    }

    /**
     * 根据案件类型统计
     * @return
     */
    @ResponseBody
    @GetMapping("/statistics/casetype")
    public ResponseResult<List<StatisticsDto>> statisticsByCasetype() {
        return ResponseResult.success("查询成功", cyCaseBaseService.statisticsByCasetype());

    }

    /**
     * 统计一般案件的结案状态
     * @apiNote 最后一个通过差值计算
     * @return
     */
    @ResponseBody
    @GetMapping("/statistics/closingstatus")
    public ResponseResult<List<StatisticsDto>> statisticsByClosingstatus() {
        List<StatisticsDto> statisticsDtos = cyCaseBaseService.statisticsByClosingstatus();
        List<StatisticsDto> list = new ArrayList<>();
        return ResponseResult.success("查询成功", statisticsDtos);
    }
}
