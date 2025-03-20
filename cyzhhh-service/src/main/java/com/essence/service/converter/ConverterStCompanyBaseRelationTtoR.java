package com.essence.service.converter;

import com.essence.dao.entity.StCompanyBaseRelationDto;
import com.essence.interfaces.model.StCompanyBaseRelationEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-02-16 12:01:33
 */
@Mapper(componentModel = "spring")
public interface ConverterStCompanyBaseRelationTtoR extends BaseConverter<StCompanyBaseRelationDto, StCompanyBaseRelationEsr> {
}
