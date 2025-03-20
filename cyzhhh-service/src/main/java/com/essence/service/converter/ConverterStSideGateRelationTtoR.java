package com.essence.service.converter;

import com.essence.dao.entity.StSideGateRelationDto;
import com.essence.interfaces.model.StSideGateRelationEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;


/**
 * @author liwy
 * @since 2023-04-13 17:51:10
 */
@Mapper(componentModel = "spring")
public interface ConverterStSideGateRelationTtoR extends BaseConverter<StSideGateRelationDto, StSideGateRelationEsr> {
}
