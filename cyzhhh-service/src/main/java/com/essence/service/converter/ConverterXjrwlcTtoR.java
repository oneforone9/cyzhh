package com.essence.service.converter;

import com.essence.dao.entity.XjrwlcDto;
import com.essence.interfaces.model.XjrwlcEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2025-01-09 15:09:29
 */
@Mapper(componentModel = "spring")
public interface ConverterXjrwlcTtoR extends BaseConverter<XjrwlcDto, XjrwlcEsr> {
}
