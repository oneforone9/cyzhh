package com.essence.service.converter;

import com.essence.dao.entity.StPlanPersonDto;
import com.essence.interfaces.model.StPlanPersonEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author liwy
 * @since 2023-07-17 14:53:07
 */
@Mapper(componentModel = "spring")
public interface ConverterStPlanPersonEtoT extends BaseConverter<StPlanPersonEsu, StPlanPersonDto> {
}
