package com.essence.service.converter;

import com.essence.dao.entity.StOfficeContactDto;
import com.essence.interfaces.model.StOfficeContactEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author liwy
 * @since 2023-03-29 18:49:56
 */
@Mapper(componentModel = "spring")
public interface ConverterStOfficeContactEtoT extends BaseConverter<StOfficeContactEsu, StOfficeContactDto> {
}
