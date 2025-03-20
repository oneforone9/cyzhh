package com.essence.service.converter;

import com.essence.dao.entity.StLevelRainDto;
import com.essence.interfaces.model.StLevelRainEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;



/**
 * @author BINX
 * @since 2023-03-08 11:31:59
 */
@Mapper(componentModel = "spring")
public interface ConverterStLevelRainTtoR extends BaseConverter<StLevelRainDto, StLevelRainEsr> {
}
