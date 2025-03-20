package com.essence.service.converter;


import com.essence.dao.entity.XjZjhDto;
import com.essence.interfaces.model.XjZjhEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2025-01-08 15:52:40
 */
@Mapper(componentModel = "spring")
public interface ConverterXjZjhTtoR extends BaseConverter<XjZjhDto, XjZjhEsr> {
}
