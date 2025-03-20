package com.essence.service.converter;


import com.essence.dao.entity.XjBzxxDto;
import com.essence.interfaces.model.XjBzxxEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2025-01-08 08:13:25
 */
@Mapper(componentModel = "spring")
public interface ConverterXjBzxxEtoT extends BaseConverter<XjBzxxEsu, XjBzxxDto> {
}
