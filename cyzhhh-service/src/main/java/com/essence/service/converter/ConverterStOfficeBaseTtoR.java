package com.essence.service.converter;

import com.essence.dao.entity.StOfficeBaseDto;
import com.essence.interfaces.model.StOfficeBaseEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author liwy
 * @since 2023-03-29 14:20:59
 */
@Mapper(componentModel = "spring")
public interface ConverterStOfficeBaseTtoR extends BaseConverter<StOfficeBaseDto, StOfficeBaseEsr> {
}
