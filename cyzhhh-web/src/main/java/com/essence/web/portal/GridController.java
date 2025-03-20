package com.essence.web.portal;

import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.WatchAreaService;
import com.essence.interfaces.model.Shiduan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 降雨网格
 */
@RestController
@RequestMapping("rain")
public class GridController {

    @Autowired
    private WatchAreaService watchAreaService;

    /**
     * 获取网格雨量数据
     *
     * @return
     */
    @GetMapping("/grid")
    public ResponseResult<List<Shiduan>> getGridRainData() {
        return ResponseResult.success("查询网格数据", watchAreaService.getGridRainData());
    }
}
