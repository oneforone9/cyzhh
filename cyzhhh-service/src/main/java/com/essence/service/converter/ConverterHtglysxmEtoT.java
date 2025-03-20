package com.essence.service.converter;

import com.essence.dao.entity.HtglysxmDto;
import com.essence.interfaces.model.HtglysxmEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2024-09-10 14:02:59
 */
@Mapper(componentModel = "spring")
public interface ConverterHtglysxmEtoT extends BaseConverter<HtglysxmEsu, HtglysxmDto> {
}
