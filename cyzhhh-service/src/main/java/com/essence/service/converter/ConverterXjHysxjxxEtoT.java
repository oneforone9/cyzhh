package com.essence.service.converter;

import com.essence.dao.entity.XjHysxjxxDto;
import com.essence.interfaces.model.XjHysxjxxEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2025-01-09 14:00:53
 */
@Mapper(componentModel = "spring")
public interface ConverterXjHysxjxxEtoT extends BaseConverter<XjHysxjxxEsu, XjHysxjxxDto> {
}
