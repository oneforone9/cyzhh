package com.essence.service.converter;

import com.essence.dao.entity.StOfficeContactDto;
import com.essence.interfaces.model.StOfficeContactEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author liwy
 * @since 2023-03-29 18:52:57
 */
@Mapper(componentModel = "spring")
public interface ConverterStOfficeContactTtoR extends BaseConverter<StOfficeContactDto, StOfficeContactEsr> {
}
