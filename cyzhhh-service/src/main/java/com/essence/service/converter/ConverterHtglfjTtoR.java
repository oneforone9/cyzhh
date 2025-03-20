package com.essence.service.converter;


import com.essence.dao.entity.HtglfjDto;
import com.essence.interfaces.model.HtglfjEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2024-09-10 14:02:24
 */
@Mapper(componentModel = "spring")
public interface ConverterHtglfjTtoR extends BaseConverter<HtglfjDto, HtglfjEsr> {
}
