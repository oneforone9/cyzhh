package com.essence.service.converter;

import com.essence.dao.entity.EventCompanyDto;
import com.essence.interfaces.model.EventCompanyEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;



/**
 * @author majunjie
 * @since 2023-06-05 11:24:46
 */
@Mapper(componentModel = "spring")
public interface ConverterEventCompanyEtoT extends BaseConverter<EventCompanyEsu, EventCompanyDto> {
}
