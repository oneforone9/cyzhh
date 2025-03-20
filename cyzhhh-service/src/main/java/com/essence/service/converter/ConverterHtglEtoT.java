package com.essence.service.converter;

import com.essence.dao.entity.HtglDto;
import com.essence.interfaces.model.HtglEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2024-09-09 17:45:35
 */
@Mapper(componentModel = "spring")
public interface ConverterHtglEtoT extends BaseConverter<HtglEsu, HtglDto> {
}
