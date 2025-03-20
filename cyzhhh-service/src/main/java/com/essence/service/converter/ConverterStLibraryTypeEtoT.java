package com.essence.service.converter;

import com.essence.dao.entity.StLibraryTypeDto;
import com.essence.interfaces.model.StLibraryTypeEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author liwy
 * @since 2023-08-17 10:31:18
 */
@Mapper(componentModel = "spring")
public interface ConverterStLibraryTypeEtoT extends BaseConverter<StLibraryTypeEsu, StLibraryTypeDto> {
}
