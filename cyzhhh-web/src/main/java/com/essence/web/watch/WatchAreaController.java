package com.essence.web.watch;

import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.WatchAreaService;
import com.essence.interfaces.dot.RiverEcologyDto;
import com.essence.interfaces.model.WatchAreaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 观鸟区
 */
@RestController
@RequestMapping("watch")
public class WatchAreaController {
    @Autowired
    private WatchAreaService watchAreaService;

    /**
     * 新增
     *
     * @param watchAreaDTO 观鸟区
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public ResponseResult<WatchAreaDTO> insert(@RequestBody WatchAreaDTO watchAreaDTO) {

        return ResponseResult.success("添加成功", watchAreaService.insert(watchAreaDTO));
    }

    /**
     * 查询观鸟区的图片 是否在观鸟 1-北小河  2-亮马河 3-坝河 4-萧太后河  空或其他则不是观鸟区视频
     *
     * @return
     */
    @GetMapping("/info")
    @ResponseBody
    public ResponseResult<List<WatchAreaDTO>> getWatchArea(String watchArea, String imageFlag) {

        return ResponseResult.success("查询观鸟区的图片或视频成功", watchAreaService.getWatchArea(watchArea, imageFlag));
    }


    /**
     * 删除
     *
     * @return
     */
    @GetMapping("/remove")
    @ResponseBody
    public ResponseResult remove(String id) {

        return ResponseResult.success("删除成功", watchAreaService.remove(id));
    }

    /**
     * 河流映像
     *
     * @return
     */
    @GetMapping("/river/ecology")
    @ResponseBody
    public ResponseResult<RiverEcologyDto> getWaterEcology(String riverId) {

        return ResponseResult.success("查询成功", watchAreaService.getRiverEcology(riverId));
    }

}
