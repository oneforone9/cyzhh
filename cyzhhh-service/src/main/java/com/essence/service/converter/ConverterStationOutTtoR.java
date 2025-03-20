package com.essence.service.converter;

import com.essence.dao.entity.StationOutDto;
import com.essence.interfaces.model.StationOutEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-05-11 10:35:47
 */
@Mapper(componentModel = "spring")
public interface ConverterStationOutTtoR extends BaseConverter<StationOutDto, StationOutEsr> {
}
