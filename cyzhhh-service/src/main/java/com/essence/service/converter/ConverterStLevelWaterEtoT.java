package com.essence.service.converter;

import com.essence.dao.entity.StLevelWaterDto;
import com.essence.interfaces.model.StLevelWaterEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;


/**
 * @author BINX
 * @since 2023-03-08 11:32:38
 */
@Mapper(componentModel = "spring")
public interface ConverterStLevelWaterEtoT extends BaseConverter<StLevelWaterEsu, StLevelWaterDto> {
}
