package com.essence.service.converter;

import com.essence.dao.entity.StOfficeBaseDto;
import com.essence.interfaces.model.StOfficeBaseEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author liwy
 * @since 2023-03-29 14:20:56
 */
@Mapper(componentModel = "spring")
public interface ConverterStOfficeBaseEtoT extends BaseConverter<StOfficeBaseEsu, StOfficeBaseDto> {
}
