package com.essence.service.converter;


import com.essence.dao.entity.XjrwlcDto;
import com.essence.interfaces.model.XjrwlcEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2025-01-09 15:09:29
 */
@Mapper(componentModel = "spring")
public interface ConverterXjrwlcEtoT extends BaseConverter<XjrwlcEsu, XjrwlcDto> {
}
