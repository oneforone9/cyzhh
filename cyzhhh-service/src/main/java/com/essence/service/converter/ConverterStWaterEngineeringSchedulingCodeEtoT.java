package com.essence.service.converter;


import com.essence.dao.entity.StWaterEngineeringSchedulingCodeDto;
import com.essence.interfaces.model.StWaterEngineeringSchedulingCodeEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2023-07-04 14:57:13
 */
@Mapper(componentModel = "spring")
public interface ConverterStWaterEngineeringSchedulingCodeEtoT extends BaseConverter<StWaterEngineeringSchedulingCodeEsu, StWaterEngineeringSchedulingCodeDto> {
}
