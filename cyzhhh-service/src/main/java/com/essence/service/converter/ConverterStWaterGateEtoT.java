package com.essence.service.converter;

import com.essence.dao.entity.StWaterGateDto;
import com.essence.interfaces.model.StWaterGateEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2023-04-20 15:36:30
 */
@Mapper(componentModel = "spring")
public interface ConverterStWaterGateEtoT extends BaseConverter<StWaterGateEsu, StWaterGateDto> {
}
