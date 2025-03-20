package com.essence.web.portal;

import com.alibaba.excel.EasyExcel;
import com.essence.common.dto.SectionManageDto;
import com.essence.common.dto.SectionWaterQualityDTO;
import com.essence.common.dto.clear.TownCheckStatisticDTO;
import com.essence.common.utils.ResponseResult;
import com.essence.dao.entity.SectionWaterQuality;
import com.essence.dao.entity.SectionWaterQualityEx;
import com.essence.euauth.common.SysConstant;
import com.essence.euauth.common.util.StringUtil;
import com.essence.interfaces.api.SectionWaterQualityService;
import com.essence.interfaces.entity.PieChartDto;
import com.essence.interfaces.model.SectionWaterQualityEsu;
import com.essence.web.listener.SectionWaterQualityExportListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 断面水质
 *
 * @author bird
 * @date 2022/11/19 13:59
 * @Description
 */
@RestController
@RequestMapping("/sectionWaterQuality")
public class SectionWaterQualityController {


    @Autowired
    private SectionWaterQualityService sectionWaterQualityService;


    /**
     * 根据期间，查询断面水质状况比列
     *
     * @param period 期间 （2022-12）
     * @return
     */
    @GetMapping("/find/{period}")
    @ResponseBody
    public ResponseResult<List<PieChartDto<SectionWaterQualityDTO>>> find(@PathVariable String period) {
        return ResponseResult.success("查询成功", sectionWaterQualityService.querySectionWaterQualityByQualityLevel(period));
    }


    /**
     * 根据期间，查询断面水质情况
     *
     * @param period 期间 （2022-12）
     * @return
     */
    @GetMapping("/findAll/{period}")
    @ResponseBody
    public ResponseResult<List<SectionWaterQualityDTO>> findAll(@PathVariable String period) {
        return ResponseResult.success("查询成功", sectionWaterQualityService.queryAllSectionWaterQualityByPeriod(period));
    }

    /**
     * 国考市考 根据日期查询数据
     *
     * @param year 期间 （2022）
     * @return
     */
    @GetMapping("/find/year")
    public ResponseResult<List<SectionWaterQuality>> findYear(String year, String sectionId) {
        return ResponseResult.success("查询成功", sectionWaterQualityService.findYear(year,sectionId));
    }


    /**
     * 根据文件上传，更新或插入数据
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("upload")
    public ResponseResult upload(MultipartFile file,HttpServletRequest request) throws IOException {
        String userId = (String) request.getSession().getAttribute(SysConstant.CURRENT_USER_ID);
        if (StringUtil.isEmpty(userId)) {
            return ResponseResult.error("登录已过期-添加失败");
        }
        EasyExcel.read(file.getInputStream(), SectionWaterQualityEsu.class,
                new SectionWaterQualityExportListener(sectionWaterQualityService,userId)).sheet().doRead();
        return ResponseResult.success("上传成功",null);
    }


    /**
     * 根据时间查询断面水质状况
     *
     * @param period 期间 （2022-12）
     * @return
     */
    @GetMapping("/findBytime/{period}")
    @ResponseBody
    public ResponseResult<List<SectionWaterQualityDTO>> findBytime(@PathVariable String period) {
        return ResponseResult.success("查询成功", sectionWaterQualityService.findBytime(period));
    }


    /**
     * 清洁的河 国控 市控 断面
     * 根据时间查询断面水质状况
     *
     * @param period 期间 （2022）
     * @return
     */
    @GetMapping("/manage/list")
    @ResponseBody
    public ResponseResult<List<SectionManageDto>> getSectionManageList(String period) {
        return ResponseResult.success("查询成功", sectionWaterQualityService.getSectionManageList(period));
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public ResponseResult deleteSectionData(String id) {
        sectionWaterQualityService.delete(id);
        return ResponseResult.success("删除成功",null);
    }

    /**
     * 乡镇考 上传文件
     *
     * @return
     */
    @PostMapping("/word/upload")
    public ResponseResult insertTownsUpload(MultipartFile multipartFile) throws IOException {
        InputStream inputStream = multipartFile.getInputStream();
        sectionWaterQualityService.dealTownsWord( inputStream);
        return ResponseResult.success("添加成功",null);
    }

}
