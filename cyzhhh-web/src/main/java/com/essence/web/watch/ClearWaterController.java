package com.essence.web.watch;

import com.essence.common.dto.TownCheckDto;
import com.essence.common.dto.clear.ClearWeekCheckDTO;
import com.essence.common.dto.clear.ClearWeekStatisticDTO;
import com.essence.common.dto.clear.TownCheckStatisticDTO;
import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.CleatWaterService;
import com.essence.interfaces.model.WatchAreaDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 清洁的河
 */
@RestController
@RequestMapping("clear")
public class ClearWaterController {
    @Resource
    private CleatWaterService cleatWaterService;


    /**
     * 读取周测数据
     *
     * @return
     */
    @PostMapping("/add")
    public ResponseResult<WatchAreaDTO> insert(MultipartFile multipartFile) throws IOException {
        InputStream inputStream = multipartFile.getInputStream();
        cleatWaterService.delExcelData( inputStream);
        return ResponseResult.success("添加成功",null);
    }

    /**
     * 查询 周测数据
     * week  格式 yyyy-周（43）
     * @return
     */
    @GetMapping("/week/list")
    public ResponseResult<List<ClearWeekCheckDTO> > getWeekData(String week) throws IOException {

        List<ClearWeekCheckDTO> list = cleatWaterService.getWeekData( week);
        return ResponseResult.success("添加成功",list);
    }


    /**
     * 获取周测统计数据
     *
     * @return
     */
    @PostMapping("/week/statistic")
    public ResponseResult<ClearWeekStatisticDTO> getWeekStatistic(@RequestBody ClearWeekStatisticDTO clearWeekStatisticDTO) throws IOException {
        ClearWeekStatisticDTO weekStatisticDTO =  cleatWaterService.getWeekStatistic(clearWeekStatisticDTO);
        return ResponseResult.success("添加成功",weekStatisticDTO);
    }

    /**
     * 乡镇考 上传文件
     *
     * @return
     */
    @PostMapping("/towns/upload")
    public ResponseResult insertTownsUpload(MultipartFile multipartFile) throws IOException {
        InputStream inputStream = multipartFile.getInputStream();
        cleatWaterService.dealTownsWord( inputStream);
        return ResponseResult.success("添加成功",null);
    }

    /**
     * 乡镇考 列表查询 只需要传递 time 年-月
     *
     * @return
     */
    @PostMapping("/towns/list")
    public ResponseResult<List<TownCheckDto>> townList(@RequestBody TownCheckStatisticDTO townCheckStatisticDTO) throws IOException {

        List<TownCheckDto> townList = cleatWaterService.townList( townCheckStatisticDTO);
        return ResponseResult.success("添加成功",townList);
    }

    /**
     * 乡镇考 圆环统计
     *
     * @return key 水质等级  v 水质等级个数
     */
    @GetMapping("/towns/rank/statistic")
    public ResponseResult<Map<String,Integer>> getRankStatistic(String year) throws IOException {

        Map<String,Integer> map = cleatWaterService.getRankStatistic(year);
        return ResponseResult.success("添加成功",map);
    }





    /**
     * 乡镇考 统计查询 只需传参 time
     *
     * @return
     */
    @PostMapping("/towns/statistic")
    public ResponseResult<TownCheckStatisticDTO> getTownsStatistic(@RequestBody TownCheckStatisticDTO townCheckStatisticDTO) throws IOException {
        TownCheckStatisticDTO checkStatisticDTO = cleatWaterService.getTownsStatistic( townCheckStatisticDTO);
        return ResponseResult.success("添加成功",checkStatisticDTO);
    }


}
