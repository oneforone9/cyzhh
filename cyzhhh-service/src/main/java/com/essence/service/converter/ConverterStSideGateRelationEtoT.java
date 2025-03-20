package com.essence.service.converter;

import com.essence.dao.entity.StSideGateRelationDto;
import com.essence.interfaces.model.StSideGateRelationEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author liwy
 * @since 2023-04-13 17:51:07
 */
@Mapper(componentModel = "spring")
public interface ConverterStSideGateRelationEtoT extends BaseConverter<StSideGateRelationEsu, StSideGateRelationDto> {
}
