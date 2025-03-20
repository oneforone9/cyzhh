package com.essence.service.converter;

import com.essence.dao.entity.ViewOfficeBaseDto;
import com.essence.interfaces.model.ViewOfficeBaseEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2024-09-11 14:21:33
 */
@Mapper(componentModel = "spring")
public interface ConverterViewOfficeBaseTtoR extends BaseConverter<ViewOfficeBaseDto, ViewOfficeBaseEsr> {
}
