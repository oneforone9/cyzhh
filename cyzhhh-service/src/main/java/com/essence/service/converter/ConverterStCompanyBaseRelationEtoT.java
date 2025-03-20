package com.essence.service.converter;

import com.essence.dao.entity.StCompanyBaseRelationDto;
import com.essence.interfaces.model.StCompanyBaseRelationEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-02-16 12:01:26
 */
@Mapper(componentModel = "spring")
public interface ConverterStCompanyBaseRelationEtoT extends BaseConverter<StCompanyBaseRelationEsu, StCompanyBaseRelationDto> {
}
