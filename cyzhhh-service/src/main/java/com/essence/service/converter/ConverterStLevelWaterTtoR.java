package com.essence.service.converter;

import com.essence.dao.entity.StLevelWaterDto;
import com.essence.interfaces.model.StLevelWaterEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;



/**
 * @author BINX
 * @since 2023-03-08 11:32:40
 */
@Mapper(componentModel = "spring")
public interface ConverterStLevelWaterTtoR extends BaseConverter<StLevelWaterDto, StLevelWaterEsr> {
}
