package com.essence.service.converter;


import com.essence.dao.entity.XjSxtJhDto;
import com.essence.interfaces.model.XjSxtJhEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2025-01-08 22:46:38
 */
@Mapper(componentModel = "spring")
public interface ConverterXjSxtJhTtoR extends BaseConverter<XjSxtJhDto, XjSxtJhEsr> {
}
