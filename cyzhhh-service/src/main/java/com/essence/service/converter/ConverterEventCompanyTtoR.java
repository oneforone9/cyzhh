package com.essence.service.converter;

import com.essence.dao.entity.EventCompanyDto;
import com.essence.interfaces.model.EventCompanyEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;


/**
 * @author majunjie
 * @since 2023-06-05 11:24:47
 */
@Mapper(componentModel = "spring")
public interface ConverterEventCompanyTtoR extends BaseConverter<EventCompanyDto, EventCompanyEsr> {
}
