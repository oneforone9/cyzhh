package com.essence.service.converter;

import com.essence.dao.entity.StRainDateHour;
import com.essence.interfaces.model.StRainDateHourEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author tyy
 * @since 2024-07-20 11:04:28
 */
@Mapper(componentModel = "spring")
public interface ConverterStRainDateHourEtoT extends BaseConverter<StRainDateHourEsu, StRainDateHour> {
}
