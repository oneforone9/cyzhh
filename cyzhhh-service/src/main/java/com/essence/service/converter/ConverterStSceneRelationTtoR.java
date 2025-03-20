package com.essence.service.converter;


import com.essence.dao.entity.StSceneRelationDto;
import com.essence.interfaces.model.StSceneRelationEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author liwy
 * @since 2023-06-01 14:48:26
 */
@Mapper(componentModel = "spring")
public interface ConverterStSceneRelationTtoR extends BaseConverter<StSceneRelationDto, StSceneRelationEsr> {
}
