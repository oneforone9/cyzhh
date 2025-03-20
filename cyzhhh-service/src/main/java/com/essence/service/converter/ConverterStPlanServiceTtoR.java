package com.essence.service.converter;

import com.essence.dao.entity.StPlanServiceDto;
import com.essence.interfaces.model.StPlanServiceEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author liwy
 * @since 2023-07-13 14:46:27
 */
@Mapper(componentModel = "spring")
public interface ConverterStPlanServiceTtoR extends BaseConverter<StPlanServiceDto, StPlanServiceEsr> {
}
