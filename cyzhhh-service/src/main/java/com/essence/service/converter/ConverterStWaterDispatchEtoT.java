package com.essence.service.converter;

import com.essence.dao.entity.StWaterDispatchDto;
import com.essence.interfaces.model.StWaterDispatchEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2023-05-08 14:26:19
 */
@Mapper(componentModel = "spring")
public interface ConverterStWaterDispatchEtoT extends BaseConverter<StWaterDispatchEsu, StWaterDispatchDto> {
}
