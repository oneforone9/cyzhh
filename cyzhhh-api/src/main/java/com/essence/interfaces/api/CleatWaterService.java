package com.essence.interfaces.api;

import com.essence.common.dto.TownCheckDto;
import com.essence.common.dto.clear.ClearWeekCheckDTO;
import com.essence.common.dto.clear.ClearWeekStatisticDTO;
import com.essence.common.dto.clear.TownCheckStatisticDTO;
import com.essence.common.dto.health.AreaHealthDataInfoDto;
import com.essence.common.dto.health.HealthRequestDto;
import com.essence.interfaces.dot.OrganismRiverInfosDto;
import com.essence.interfaces.dot.OrganismRiverRecordDto;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface CleatWaterService {
    void delExcelData( InputStream inputStream) throws IOException;

    ClearWeekStatisticDTO getWeekStatistic(ClearWeekStatisticDTO clearWeekStatisticDTO);

    /**
     * 乡镇 考 解析上传word
     * @param inputStream
     */
    void dealTownsWord(InputStream inputStream) throws IOException;

    /**
     * 乡镇考
     * @param townCheckStatisticDTO
     * @return
     */
    TownCheckStatisticDTO getTownsStatistic(TownCheckStatisticDTO townCheckStatisticDTO);

    /**
     * 生态 河 导入excel 文件
     * @param inputStream
     */
    void delOrganismExcelData(InputStream inputStream) throws IOException;

    OrganismRiverInfosDto getRiverList(String rvid);

    List<AreaHealthDataInfoDto> getHealthList(HealthRequestDto healthRequestDto);

    List<OrganismRiverRecordDto> getManageRiverList(String year);

    void addManageRiverList(List<OrganismRiverRecordDto> list);

    List<ClearWeekCheckDTO> getWeekData(String week);

    List<TownCheckDto> townList(TownCheckStatisticDTO townCheckStatisticDTO);

    Map<String, Integer> getRankStatistic(String year);
}
