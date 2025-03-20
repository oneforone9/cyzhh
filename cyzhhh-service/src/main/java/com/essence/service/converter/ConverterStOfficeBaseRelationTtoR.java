package com.essence.service.converter;

import com.essence.dao.entity.StOfficeBaseRelationDto;
import com.essence.interfaces.model.StOfficeBaseRelationEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author liwy
 * @since 2023-03-29 14:21:27
 */
@Mapper(componentModel = "spring")
public interface ConverterStOfficeBaseRelationTtoR extends BaseConverter<StOfficeBaseRelationDto, StOfficeBaseRelationEsr> {
}
