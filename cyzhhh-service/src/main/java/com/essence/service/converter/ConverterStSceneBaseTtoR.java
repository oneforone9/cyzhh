package com.essence.service.converter;


import com.essence.dao.entity.StSceneBaseDto;
import com.essence.interfaces.model.StSceneBaseEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author liwy
 * @since 2023-06-01 14:48:00
 */
@Mapper(componentModel = "spring")
public interface ConverterStSceneBaseTtoR extends BaseConverter<StSceneBaseDto, StSceneBaseEsr> {
}
