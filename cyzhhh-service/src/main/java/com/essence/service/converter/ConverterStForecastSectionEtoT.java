package com.essence.service.converter;

import com.essence.dao.entity.StForecastSectionDto;
import com.essence.interfaces.model.StForecastSectionEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-04-22 10:55:02
 */
@Mapper(componentModel = "spring")
public interface ConverterStForecastSectionEtoT extends BaseConverter<StForecastSectionEsu, StForecastSectionDto> {
}
