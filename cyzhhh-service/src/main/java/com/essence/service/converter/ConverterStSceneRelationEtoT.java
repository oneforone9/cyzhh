package com.essence.service.converter;

import com.essence.dao.entity.StSceneRelationDto;
import com.essence.interfaces.model.StSceneRelationEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;


/**
 * @author liwy
 * @since 2023-06-01 14:48:24
 */
@Mapper(componentModel = "spring")
public interface ConverterStSceneRelationEtoT extends BaseConverter<StSceneRelationEsu, StSceneRelationDto> {
}
