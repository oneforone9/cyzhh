package com.essence.service.converter;


import com.essence.dao.entity.XjHysJhDto;
import com.essence.interfaces.model.XjHysJhEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2025-01-09 10:22:49
 */
@Mapper(componentModel = "spring")
public interface ConverterXjHysJhEtoT extends BaseConverter<XjHysJhEsu, XjHysJhDto> {
}
