package com.essence.interfaces.api;

import com.essence.common.dto.SectionManageDto;
import com.essence.common.dto.SectionWaterQualityDTO;
import com.essence.dao.entity.SectionWaterQuality;
import com.essence.dao.entity.SectionWaterQualityEx;
import com.essence.interfaces.entity.PieChartDto;
import com.essence.interfaces.model.SectionWaterQualityEsu;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * SectionWaterQualityService
 *
 * @author bird
 * @date 2022/11/19 15:21
 * @Description
 */
public interface SectionWaterQualityService {


    /**
     * 根据期间，查询断面水质情况
     * @param period
     * @return
     */
    List<SectionWaterQualityDTO> queryAllSectionWaterQualityByPeriod(String period);


    /**
     * 根据期间，查询断面水质状况比列
     * @param period
     * @return
     */
    List<PieChartDto<SectionWaterQualityDTO>> querySectionWaterQualityByQualityLevel(String  period);


    /**
     * 根据文件上传，更新或插入数据
     * @param importData
     * @param userId
     */
    void insertUpdateData(List<SectionWaterQualityEsu> importData,String userId);

    /**
     * 根据时间查询断面水质状况
     * @param period
     * @return
     */
    List<SectionWaterQualityDTO> findBytime(String period);

    List<SectionManageDto> getSectionManageList(String period);

    void delete(String id);

    void dealTownsWord(InputStream inputStream) throws IOException;

    List<SectionWaterQuality> findYear(String year, String sectionId);
}
