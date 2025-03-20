package com.essence.service.converter;

import com.essence.dao.entity.StMaterialBaseDto;
import com.essence.interfaces.model.StMaterialBaseEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author liwy
 * @since 2023-04-13 14:59:26
 */
@Mapper(componentModel = "spring")
public interface ConverterStMaterialBaseEtoT extends BaseConverter<StMaterialBaseEsu, StMaterialBaseDto> {
}
