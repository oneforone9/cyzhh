package com.essence.service.converter;

import com.essence.dao.entity.StWaterEngineeringSchedulingLeadDto;
import com.essence.interfaces.model.StWaterEngineeringSchedulingLeadEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2023-07-03 17:24:47
 */
@Mapper(componentModel = "spring")
public interface ConverterStWaterEngineeringSchedulingLeadEtoT extends BaseConverter<StWaterEngineeringSchedulingLeadEsu, StWaterEngineeringSchedulingLeadDto> {
}
