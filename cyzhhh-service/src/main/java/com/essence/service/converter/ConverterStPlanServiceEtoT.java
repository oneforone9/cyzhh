package com.essence.service.converter;

import com.essence.dao.entity.StPlanServiceDto;
import com.essence.interfaces.model.StPlanServiceEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author liwy
 * @since 2023-07-13 14:46:27
 */
@Mapper(componentModel = "spring")
public interface ConverterStPlanServiceEtoT extends BaseConverter<StPlanServiceEsu, StPlanServiceDto> {
}
