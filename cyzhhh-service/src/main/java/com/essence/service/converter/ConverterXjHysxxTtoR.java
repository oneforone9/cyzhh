package com.essence.service.converter;

import com.essence.dao.entity.XjHysxxDto;
import com.essence.interfaces.model.XjHysxxEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2025-01-08 08:14:05
 */
@Mapper(componentModel = "spring")
public interface ConverterXjHysxxTtoR extends BaseConverter<XjHysxxDto, XjHysxxEsr> {
}
