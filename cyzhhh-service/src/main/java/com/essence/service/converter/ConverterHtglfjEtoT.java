package com.essence.service.converter;


import com.essence.dao.entity.HtglfjDto;
import com.essence.interfaces.model.HtglfjEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2024-09-10 14:02:24
 */
@Mapper(componentModel = "spring")
public interface ConverterHtglfjEtoT extends BaseConverter<HtglfjEsu, HtglfjDto> {
}
