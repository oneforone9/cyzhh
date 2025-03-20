package com.essence.service.converter;


import com.essence.dao.entity.CyWeatherForecastDto;
import com.essence.interfaces.model.CyWeatherForecastEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-03-16 16:41:56
 */
@Mapper(componentModel = "spring")
public interface ConverterCyWeatherForecastTtoR extends BaseConverter<CyWeatherForecastDto, CyWeatherForecastEsr> {
}
