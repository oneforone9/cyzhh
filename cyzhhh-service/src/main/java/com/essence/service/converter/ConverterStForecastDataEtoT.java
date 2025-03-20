package com.essence.service.converter;


import com.essence.dao.entity.StForecastDataDto;
import com.essence.interfaces.model.StForecastDataEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2023-04-17 19:38:59
 */
@Mapper(componentModel = "spring")
public interface ConverterStForecastDataEtoT extends BaseConverter<StForecastDataEsu, StForecastDataDto> {
}
