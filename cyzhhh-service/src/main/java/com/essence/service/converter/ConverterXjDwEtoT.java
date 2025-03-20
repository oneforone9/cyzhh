package com.essence.service.converter;

import com.essence.dao.entity.XjDwDto;
import com.essence.interfaces.model.XjDwEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2025-01-09 08:56:30
 */
@Mapper(componentModel = "spring")
public interface ConverterXjDwEtoT extends BaseConverter<XjDwEsu, XjDwDto> {
}
