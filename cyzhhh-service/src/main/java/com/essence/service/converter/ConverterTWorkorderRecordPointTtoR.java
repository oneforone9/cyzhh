package com.essence.service.converter;

import com.essence.dao.entity.TWorkorderRecordPointDto;
import com.essence.interfaces.model.TWorkorderRecordPointEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;


/**
 * @author liwy
 * @since 2023-05-07 12:12:08
 */
@Mapper(componentModel = "spring")
public interface ConverterTWorkorderRecordPointTtoR extends BaseConverter<TWorkorderRecordPointDto, TWorkorderRecordPointEsr> {
}
