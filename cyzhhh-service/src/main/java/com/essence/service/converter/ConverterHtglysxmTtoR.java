package com.essence.service.converter;


import com.essence.dao.entity.HtglysxmDto;
import com.essence.interfaces.model.HtglysxmEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2024-09-10 14:02:59
 */
@Mapper(componentModel = "spring")
public interface ConverterHtglysxmTtoR extends BaseConverter<HtglysxmDto, HtglysxmEsr> {
}
