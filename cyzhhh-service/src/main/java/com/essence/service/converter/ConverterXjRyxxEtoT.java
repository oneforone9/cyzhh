package com.essence.service.converter;


import com.essence.dao.entity.XjRyxxDto;
import com.essence.interfaces.model.XjRyxxEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2025-01-08 08:14:23
 */
@Mapper(componentModel = "spring")
public interface ConverterXjRyxxEtoT extends BaseConverter<XjRyxxEsu, XjRyxxDto> {
}
