package com.essence.service.converter;


import com.essence.dao.entity.XjSxtJhTimeDto;
import com.essence.interfaces.model.XjSxtJhTimeEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2025-01-08 17:51:56
 */
@Mapper(componentModel = "spring")
public interface ConverterXjSxtJhTimeEtoT extends BaseConverter<XjSxtJhTimeEsu, XjSxtJhTimeDto> {
}
