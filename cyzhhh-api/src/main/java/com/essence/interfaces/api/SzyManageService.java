package com.essence.interfaces.api;

import com.essence.dao.entity.WaterSupplyCaseDto;
import com.essence.dao.entity.WaterSupplyCaseImportDto;
import com.essence.dao.entity.WaterSupplyParamDto;
import com.essence.dao.entity.WaterTransferFlowDto;
import com.essence.interfaces.dot.WaterOverLevelStatisticsDto;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author BINX
 * @Description TODO
 * @Date 2023/5/4 18:10
 */
public interface SzyManageService {
    List<WaterSupplyCaseDto> getWaterSupplyParam(String caseId);

    Object saveWaterSupplyParam(WaterSupplyParamDto waterSupplyParam);

    Object importTimeSupply(WaterSupplyCaseImportDto param);

    void downloadTimeSupplyModel(HttpServletResponse response);

    List<WaterTransferFlowDto> getWaterTransfer();

    WaterOverLevelStatisticsDto getWaterLevel(int state);
}
