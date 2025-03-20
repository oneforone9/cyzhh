package com.essence.web.watch;

import com.essence.common.dto.health.AreaHealthDataInfoDto;
import com.essence.common.dto.health.HealthRequestDto;
import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.CleatWaterService;
import com.essence.interfaces.model.WatchAreaDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 健康的河
 */
@RestController
@RequestMapping("health")
public class HealthRiverController {
    @Resource
    private CleatWaterService cleatWaterService;

    /**
     * 获取列表统计
     *
     * @return
     */
    @PostMapping("/list")
    public ResponseResult<List<AreaHealthDataInfoDto>> getList(@RequestBody HealthRequestDto healthRequestDto) throws IOException {

        List<AreaHealthDataInfoDto> list = cleatWaterService.getHealthList(healthRequestDto);
        return ResponseResult.success("添加成功",list);
    }
}
