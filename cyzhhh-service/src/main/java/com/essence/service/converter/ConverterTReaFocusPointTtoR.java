package com.essence.service.converter;

import com.essence.dao.entity.TReaFocusPointDto;
import com.essence.interfaces.model.TReaFocusPointEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;


/**
 * @author liwy
 * @since 2023-05-06 10:02:25
 */
@Mapper(componentModel = "spring")
public interface ConverterTReaFocusPointTtoR extends BaseConverter<TReaFocusPointDto, TReaFocusPointEsr> {
}
