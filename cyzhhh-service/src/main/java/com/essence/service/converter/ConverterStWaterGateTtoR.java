package com.essence.service.converter;

import com.essence.dao.entity.StWaterGateDto;
import com.essence.interfaces.model.StWaterGateEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2023-04-20 15:36:30
 */
@Mapper(componentModel = "spring")
public interface ConverterStWaterGateTtoR extends BaseConverter<StWaterGateDto, StWaterGateEsr> {
}
