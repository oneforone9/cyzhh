package com.essence.service.converter;


import com.essence.dao.entity.CyWeatherForecastDto;
import com.essence.interfaces.model.CyWeatherForecastEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-03-16 16:41:55
 */
@Mapper(componentModel = "spring")
public interface ConverterCyWeatherForecastEtoT extends BaseConverter<CyWeatherForecastEsu, CyWeatherForecastDto> {
}
