package com.essence.service.converter;


import com.essence.dao.entity.StSideRelationDto;
import com.essence.interfaces.model.StSideRelationEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2023-05-10 17:31:31
 */
@Mapper(componentModel = "spring")
public interface ConverterStSideRelationEtoT extends BaseConverter<StSideRelationEsu, StSideRelationDto> {
}
