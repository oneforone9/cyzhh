package com.essence.service.converter;


import com.essence.dao.entity.HtglscDto;
import com.essence.interfaces.model.HtglscEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2024-09-13 16:18:30
 */
@Mapper(componentModel = "spring")
public interface ConverterHtglscTtoR extends BaseConverter<HtglscDto, HtglscEsr> {
}
