package com.essence.service.converter;

import com.essence.dao.entity.StMaterialBaseDto;
import com.essence.interfaces.model.StMaterialBaseEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;


/**
 * @author liwy
 * @since 2023-04-13 14:59:35
 */
@Mapper(componentModel = "spring")
public interface ConverterStMaterialBaseTtoR extends BaseConverter<StMaterialBaseDto, StMaterialBaseEsr> {
}
