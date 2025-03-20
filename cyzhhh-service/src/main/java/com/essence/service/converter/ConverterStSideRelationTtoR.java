package com.essence.service.converter;


import com.essence.dao.entity.StSideRelationDto;
import com.essence.interfaces.model.StSideRelationEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2023-05-10 17:31:33
 */
@Mapper(componentModel = "spring")
public interface ConverterStSideRelationTtoR extends BaseConverter<StSideRelationDto, StSideRelationEsr> {
}
