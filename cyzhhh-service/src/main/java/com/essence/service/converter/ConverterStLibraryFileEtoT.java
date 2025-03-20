package com.essence.service.converter;

import com.essence.dao.entity.StLibraryFileDto;
import com.essence.interfaces.model.StLibraryFileEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;


/**
 * @author liwy
 * @since 2023-08-17 10:28:16
 */
@Mapper(componentModel = "spring")
public interface ConverterStLibraryFileEtoT extends BaseConverter<StLibraryFileEsu, StLibraryFileDto> {
}
