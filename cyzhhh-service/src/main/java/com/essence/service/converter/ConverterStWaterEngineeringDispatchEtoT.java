package com.essence.service.converter;


import com.essence.dao.entity.StWaterEngineeringDispatchDto;
import com.essence.interfaces.model.StWaterEngineeringDispatchEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2023-06-02 12:39:08
 */
@Mapper(componentModel = "spring")
public interface ConverterStWaterEngineeringDispatchEtoT extends BaseConverter<StWaterEngineeringDispatchEsu, StWaterEngineeringDispatchDto> {
}
