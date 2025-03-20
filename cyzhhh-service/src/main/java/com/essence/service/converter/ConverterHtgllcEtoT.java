package com.essence.service.converter;

import com.essence.dao.entity.HtgllcDto;
import com.essence.interfaces.model.HtgllcEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2024-09-10 14:02:42
 */
@Mapper(componentModel = "spring")
public interface ConverterHtgllcEtoT extends BaseConverter<HtgllcEsu, HtgllcDto> {
}
