package com.essence.service.converter;


import com.essence.dao.entity.StForecastDataDto;
import com.essence.interfaces.model.StForecastDataEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2023-04-17 19:39:00
 */
@Mapper(componentModel = "spring")
public interface ConverterStForecastDataTtoR extends BaseConverter<StForecastDataDto, StForecastDataEsr> {
}
