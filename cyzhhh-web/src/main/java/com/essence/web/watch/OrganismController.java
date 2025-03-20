package com.essence.web.watch;

import com.essence.common.dto.health.HealthRequestDto;
import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.CleatWaterService;
import com.essence.interfaces.dot.OrganismRiverInfosDto;
import com.essence.interfaces.dot.OrganismRiverRecordDto;
import com.essence.interfaces.model.WatchAreaDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 生态的河
 */
@RestController
@RequestMapping("/organism")
public class OrganismController {

    @Resource
    private CleatWaterService cleatWaterService;

    /**
     * 生态河湖 上传文件
     *
     * @return
     */
    @PostMapping("/upload")
    public ResponseResult<WatchAreaDTO> insert(MultipartFile multipartFile) throws IOException {
        InputStream inputStream = multipartFile.getInputStream();
        cleatWaterService.delOrganismExcelData( inputStream);
        return ResponseResult.success("添加成功",null);
    }

    /**
     * 生态河湖 河流排序查询
     *
     * @return
     */
    @PostMapping("/list")
    public ResponseResult<OrganismRiverInfosDto> getRiverList(@RequestBody HealthRequestDto requestDto) throws IOException {
        OrganismRiverInfosDto riverInfosDto = cleatWaterService.getRiverList(requestDto.getRvid());
        return ResponseResult.success("查询成功",riverInfosDto);
    }

    /**
     * 生态河湖 河流管理查询
     *
     * @return
     */
    @GetMapping("/manage/list")
    public ResponseResult<List<OrganismRiverRecordDto>> getManageRiverList(String year) throws IOException {
        List<OrganismRiverRecordDto> riverInfosDto = cleatWaterService.getManageRiverList(year);
        return ResponseResult.success("查询成功",riverInfosDto);
    }

    /**
     * 生态河湖 河流管理新增
     *
     * @return
     */
    @PostMapping("/manage/add")
    public ResponseResult addManageRiverList(@RequestBody List<OrganismRiverRecordDto> list) throws IOException {
        cleatWaterService.addManageRiverList(list);
        return ResponseResult.success("添加成功",null);
    }


}
