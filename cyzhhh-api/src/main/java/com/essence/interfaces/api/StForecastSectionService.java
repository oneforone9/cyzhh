package com.essence.interfaces.api;

import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.StForecastSectionEsr;
import com.essence.interfaces.model.StForecastSectionEsu;
import com.essence.interfaces.model.StForecastSectionExport;
import com.essence.interfaces.param.StForecastSectionEsp;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 服务层
 * @author BINX
 * @since 2023-04-22 10:55:03
 */
public interface StForecastSectionService extends BaseApi<StForecastSectionEsu, StForecastSectionEsp, StForecastSectionEsr> {
    void importStForecastSectionZQ(String caseId, MultipartFile file,String type);

    void importStForecastSectionZZ(String caseId, MultipartFile file,String type);

    void saveForecastSection(List<StForecastSectionEsr> stForecastSectionEsrList, String caseId,String type);

    List<StForecastSectionExport> exportStForecastSectionZQ();

    List<StForecastSectionExport> exportStForecastSectionZZ();
}
