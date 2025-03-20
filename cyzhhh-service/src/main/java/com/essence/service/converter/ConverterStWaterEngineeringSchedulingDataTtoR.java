package com.essence.service.converter;

import com.essence.dao.entity.StWaterEngineeringSchedulingDataDto;
import com.essence.interfaces.model.StWaterEngineeringSchedulingDataEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2023-05-14 18:15:42
 */
@Mapper(componentModel = "spring")
public interface ConverterStWaterEngineeringSchedulingDataTtoR extends BaseConverter<StWaterEngineeringSchedulingDataDto, StWaterEngineeringSchedulingDataEsr> {
}
