package com.essence.service.converter;


import com.essence.dao.entity.StSceneBaseDto;
import com.essence.interfaces.model.StSceneBaseEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;


/**
 * @author liwy
 * @since 2023-06-01 14:47:56
 */
@Mapper(componentModel = "spring")
public interface ConverterStSceneBaseEtoT extends BaseConverter<StSceneBaseEsu, StSceneBaseDto> {
}
