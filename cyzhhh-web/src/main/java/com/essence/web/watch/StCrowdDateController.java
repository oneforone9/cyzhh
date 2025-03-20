package com.essence.web.watch;


import com.essence.common.dto.CrowdDateRequest;
import com.essence.common.utils.ResponseResult;
import com.essence.dao.entity.StCrowdDateDto;
import com.essence.dao.entity.StCrowdRealDto;
import com.essence.interfaces.api.StCrowdDateService;
import com.essence.interfaces.api.StCrowdRealService;
import com.essence.interfaces.dot.CrowDataDto;
import com.essence.interfaces.dot.OrganismRiverRecordDto;
import com.essence.interfaces.model.StCrowdDateEsr;
import com.essence.interfaces.model.StCrowdDateEsu;
import com.essence.interfaces.param.StCrowdDateEsp;
import com.essence.interfaces.param.StCrowdRealEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * 清水的河 - 游人热力
 * @author BINX
 * @since 2023-01-12 17:36:43
 */
@RestController
@RequestMapping("/stCrowdDate")
public class StCrowdDateController extends BaseController<String, StCrowdDateEsu, StCrowdDateEsp, StCrowdDateEsr> {
    @Autowired
    private StCrowdDateService stCrowdDateService;

    @Autowired
    private StCrowdRealService stCrowdRealService;

    public StCrowdDateController(StCrowdDateService stCrowdDateService) {
        super(stCrowdDateService);
    }

    /**
     * 用水人数量管理 热力查询
     *   @DateTimeFormat(pattern = "yyyy-MM-dd")
     *     @JsonFormat(pattern = "yyyy-MM-dd")
     *
     * @return
     */
    @PostMapping("/list")
    public ResponseResult<CrowDataDto> getManageRiverList(@RequestBody CrowdDateRequest crowdDateRequest) throws IOException {
        CrowDataDto crowDataDto = stCrowdDateService.getManageRiverList(crowdDateRequest);
        return ResponseResult.success("查询成功",crowDataDto);
    }

    /**
     * 日游客量-根据日期查询
     * @param stCrowdDateEsp
     * @return
     * @throws IOException
     */
    @PostMapping("/getStCrowdDate")
    public ResponseResult<List<StCrowdDateDto>> getStCrowdDate(@RequestBody StCrowdDateEsp stCrowdDateEsp){
        List<StCrowdDateDto> stCrowdDateDtos = stCrowdDateService.getStCrowdDate(stCrowdDateEsp);
        return ResponseResult.success("查询成功",stCrowdDateDtos);
    }

    /**
     * 实时游客客量
     * @param stCrowdRealEsp
     * @return
     */
    @PostMapping("/getStCrowdReal")
    public ResponseResult<List<StCrowdRealDto>> getStCrowdReal(@RequestBody StCrowdRealEsp stCrowdRealEsp){
        List<StCrowdRealDto> stCrowdRealDtos = stCrowdRealService.getStCrowdReal(stCrowdRealEsp);
        return ResponseResult.success("查询成功",stCrowdRealDtos);
    }
}
