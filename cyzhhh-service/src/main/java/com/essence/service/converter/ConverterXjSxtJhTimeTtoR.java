package com.essence.service.converter;


import com.essence.dao.entity.XjSxtJhTimeDto;
import com.essence.interfaces.model.XjSxtJhTimeEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2025-01-08 17:51:57
 */
@Mapper(componentModel = "spring")
public interface ConverterXjSxtJhTimeTtoR extends BaseConverter<XjSxtJhTimeDto, XjSxtJhTimeEsr> {
}
