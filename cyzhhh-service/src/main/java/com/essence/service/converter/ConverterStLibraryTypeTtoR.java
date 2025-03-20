package com.essence.service.converter;

import com.essence.dao.entity.StLibraryTypeDto;
import com.essence.interfaces.model.StLibraryTypeEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author liwy
 * @since 2023-08-17 10:31:22
 */
@Mapper(componentModel = "spring")
public interface ConverterStLibraryTypeTtoR extends BaseConverter<StLibraryTypeDto, StLibraryTypeEsr> {
}
