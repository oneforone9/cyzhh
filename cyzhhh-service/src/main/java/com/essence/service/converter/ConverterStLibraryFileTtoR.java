package com.essence.service.converter;

import com.essence.dao.entity.StLibraryFileDto;
import com.essence.interfaces.model.StLibraryFileEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;


/**
 * @author liwy
 * @since 2023-08-17 10:29:28
 */
@Mapper(componentModel = "spring")
public interface ConverterStLibraryFileTtoR extends BaseConverter<StLibraryFileDto, StLibraryFileEsr> {
}
