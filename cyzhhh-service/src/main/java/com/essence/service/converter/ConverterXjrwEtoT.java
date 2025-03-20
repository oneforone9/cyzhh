package com.essence.service.converter;


import com.essence.dao.entity.XjrwDto;
import com.essence.interfaces.model.XjrwEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2025-01-09 15:09:05
 */
@Mapper(componentModel = "spring")
public interface ConverterXjrwEtoT extends BaseConverter<XjrwEsu, XjrwDto> {
}
