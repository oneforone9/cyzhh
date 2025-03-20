package com.essence.service.converter;

import com.essence.dao.entity.StForecastSectionDto;
import com.essence.interfaces.model.StForecastSectionEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-04-22 10:55:03
 */
@Mapper(componentModel = "spring")
public interface ConverterStForecastSectionTtoR extends BaseConverter<StForecastSectionDto, StForecastSectionEsr> {
}
