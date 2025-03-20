package com.essence.web.data;

import com.essence.common.dto.AnCaseTypeDto;
import com.essence.common.dto.YearCountStatisticDto;
import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.api.ManageDateService;
import com.essence.interfaces.model.TRewardDealEsr;
import com.essence.interfaces.model.TRewardDealEsu;
import com.essence.interfaces.param.TRewardDealEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/reward/deal")
public class ManageDataController extends BaseController<String, TRewardDealEsu, TRewardDealEsp, TRewardDealEsr> {
    @Resource
    private ManageDateService manageDateService;
    public ManageDataController(ManageDateService manageDateService) {
        super(manageDateService);
    }

    /**
     * 接诉即办
     *
     * @return
     */
    @PostMapping("upload")
    public ResponseResult getCaseTypeStatistic(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        manageDateService.upload(inputStream);
        return ResponseResult.success("上传成功",null);
    }

    /**
     * 接诉即办 yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    @GetMapping("statistic")
    public ResponseResult<List<AnCaseTypeDto>> getStatistic(String str,String end) throws IOException {

        List<AnCaseTypeDto> list = manageDateService.getStatistic(str,end);
        return ResponseResult.success("查询成功",list);
    }

    /**
     * 年度汇总 yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    @GetMapping("year/statistic")
    public ResponseResult<YearCountStatisticDto> getYearStatistic(String str, String end) throws IOException {

        YearCountStatisticDto yearCountStatisticDto = manageDateService.YearCountStatisticDto(str,end);
        return ResponseResult.success("查询成功",yearCountStatisticDto);
    }




}
