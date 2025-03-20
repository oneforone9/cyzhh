package com.essence.service.converter;

import com.essence.dao.entity.StPlanPersonDto;
import com.essence.interfaces.model.StPlanPersonEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author liwy
 * @since 2023-07-17 14:53:33
 */
@Mapper(componentModel = "spring")
public interface ConverterStPlanPersonTtoR extends BaseConverter<StPlanPersonDto, StPlanPersonEsr> {
}
