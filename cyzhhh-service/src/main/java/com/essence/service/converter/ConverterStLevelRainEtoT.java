package com.essence.service.converter;

import com.essence.dao.entity.StLevelRainDto;
import com.essence.interfaces.model.StLevelRainEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;


/**
 * @author BINX
 * @since 2023-03-08 11:31:55
 */
@Mapper(componentModel = "spring")
public interface ConverterStLevelRainEtoT extends BaseConverter<StLevelRainEsu, StLevelRainDto> {
}
