package com.essence.web.portal;

import com.essence.common.cache.service.RedisService;
import com.essence.common.utils.ResponseResult;
import com.essence.dao.entity.WaterSupplyCaseDto;
import com.essence.dao.entity.WaterSupplyCaseImportDto;
import com.essence.dao.entity.WaterSupplyParamDto;
import com.essence.dao.entity.WaterTransferFlowDto;
import com.essence.interfaces.api.SzyManageService;
import com.essence.interfaces.dot.WaterOverLevelStatisticsDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 水资源调度 - (补水口 + 调水)
 *
 * @Author BINX
 * @Description TODO
 * @Date 2023/5/4 17:50
 */
@RestController
@RequestMapping("/szyManage")
public class SzyManageController {

    @Autowired
    SzyManageService szyManageService;
    @Autowired
    RedisService redisService;

    /**
     * 补水口列表入参查询 (无传参默认取基础补水口数据, 有传参获取对应的案件入参数据)
     */
    @GetMapping("/getWaterSupplyParam")
    public ResponseResult<List<WaterSupplyCaseDto>> getWaterSupplyParam(@Param("caseId") String caseId) {
        return ResponseResult.success("补水口列表查询成功!", szyManageService.getWaterSupplyParam(caseId));
    }

    /**
     * 补水口模型入参保存
     */
    @PostMapping("/saveWaterSupplyParam")
    public ResponseResult saveWaterSupplyParam(@RequestBody WaterSupplyParamDto waterSupplyParam) {
        return ResponseResult.success("补水口模型入参保存成功!", szyManageService.saveWaterSupplyParam(waterSupplyParam));
    }

    /**
     * 补水口模型入参时间序列流量导入
     */
    @PostMapping("/importTimeSupply")
    public ResponseResult importTimeSupply(WaterSupplyCaseImportDto param) {
        return ResponseResult.success("导入成功!", szyManageService.importTimeSupply(param));
    }

    /**
     * 下载模板
     */
    @GetMapping("/downloadTimeSupplyModel")
    public void downloadTimeSupplyModel(HttpServletResponse response) {
        szyManageService.downloadTimeSupplyModel(response);
    }

    /**
     * 全区河道水位态势
     *
     * @param state 状态筛选 0 - 全部 ; 1 - 正常; 2 -高于; 3 - 低于; 4 - 无数据
     * @return
     */
    @GetMapping("/getWaterLevel")
    public ResponseResult<WaterOverLevelStatisticsDto> getWaterLevel(@RequestParam int state) {
        WaterOverLevelStatisticsDto waterLevel = redisService.getCacheObject("szyManageService.getWaterLevel@" + state);
        if (waterLevel == null) {
            waterLevel = szyManageService.getWaterLevel(state);
        }
        return ResponseResult.success("全区河道水位态势查询成功!", waterLevel);
    }

    /**
     * 全区水资源调水态势
     *
     * @return
     */
    @PostMapping("/getWaterTransfer")
    public ResponseResult<List<WaterTransferFlowDto>> getWaterTransfer() {
        List<WaterTransferFlowDto> waterTransfer = redisService.getCacheObject("WaterTransfer");
        if (waterTransfer == null) {
            waterTransfer = szyManageService.getWaterTransfer();
        }
        return ResponseResult.success("全区水资源调水态势查询成功!", waterTransfer);
    }
}
