package com.essence.service.converter;

import com.essence.dao.entity.StWaterEngineeringSchedulingLeadDto;
import com.essence.interfaces.model.StWaterEngineeringSchedulingLeadEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2023-07-03 17:24:50
 */
@Mapper(componentModel = "spring")
public interface ConverterStWaterEngineeringSchedulingLeadTtoR extends BaseConverter<StWaterEngineeringSchedulingLeadDto, StWaterEngineeringSchedulingLeadEsr> {
}
