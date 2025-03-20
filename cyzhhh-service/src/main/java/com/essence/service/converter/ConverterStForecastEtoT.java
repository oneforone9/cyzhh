package com.essence.service.converter;


import com.essence.dao.entity.StForecastDto;
import com.essence.interfaces.model.StForecastEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2023-04-17 19:38:22
 */
@Mapper(componentModel = "spring")
public interface ConverterStForecastEtoT extends BaseConverter<StForecastEsu, StForecastDto> {
}
