package com.essence.web.portal;

import com.essence.common.cache.service.RedisService;
import com.essence.common.dto.StStbprpBEntityDTO;
import com.essence.common.dto.WaterTransitDto;
import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.TransitWaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 巡查一览
 *
 * @Author BINX
 * @Description TODO
 * @Date 2023/4/24 10:39
 */
@RestController
@RequestMapping("/transitWater")
public class TransitWaterController {

    @Autowired
    TransitWaterService transitWaterService;
    @Autowired
    RedisService redisService;

    /**
     * 查询过境水量
     *
     * @return
     */
    @PostMapping("/searchFlow")
    public ResponseResult<WaterTransitDto> getWaterTransit(@RequestBody StStbprpBEntityDTO stStbprpBEntityDTO) {
        WaterTransitDto waterTransit;
        waterTransit = redisService.getCacheObject("WaterTransit");
        if (waterTransit == null) {
            waterTransit = transitWaterService.getWaterTransit(stStbprpBEntityDTO);
        }
        return ResponseResult.success("过境水量查询成功!", waterTransit);
    }
}
