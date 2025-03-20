package com.essence.service.converter;


import com.essence.dao.entity.StWaterEngineeringSchedulingCodeDto;
import com.essence.interfaces.model.StWaterEngineeringSchedulingCodeEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2023-07-04 14:57:17
 */
@Mapper(componentModel = "spring")
public interface ConverterStWaterEngineeringSchedulingCodeTtoR extends BaseConverter<StWaterEngineeringSchedulingCodeDto, StWaterEngineeringSchedulingCodeEsr> {
}
