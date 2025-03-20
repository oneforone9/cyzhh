package com.essence.service.converter;


import com.essence.dao.entity.water.StWaterRiskForecastDto;
import com.essence.interfaces.model.StWaterRiskForecastEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
* 水系联调-模型风险预报
*
* @author BINX
* @since 2023年5月11日 下午4:00:24
*/
@Mapper(componentModel = "spring")
public interface ConverterStWaterRiskForecastTtoR extends BaseConverter<StWaterRiskForecastDto, StWaterRiskForecastEsr> {
}
