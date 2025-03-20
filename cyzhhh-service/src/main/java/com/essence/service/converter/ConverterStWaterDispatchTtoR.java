package com.essence.service.converter;


import com.essence.dao.entity.StWaterDispatchDto;
import com.essence.interfaces.model.StWaterDispatchEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2023-05-08 14:26:20
 */
@Mapper(componentModel = "spring")
public interface ConverterStWaterDispatchTtoR extends BaseConverter<StWaterDispatchDto, StWaterDispatchEsr> {
}
