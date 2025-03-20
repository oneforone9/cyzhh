package com.essence.service.converter;


import com.essence.dao.entity.XjSxtJhDto;
import com.essence.interfaces.model.XjSxtJhEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2025-01-08 22:46:38
 */
@Mapper(componentModel = "spring")
public interface ConverterXjSxtJhEtoT extends BaseConverter<XjSxtJhEsu, XjSxtJhDto> {
}
