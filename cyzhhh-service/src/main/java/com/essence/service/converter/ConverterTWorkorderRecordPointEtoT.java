package com.essence.service.converter;

import com.essence.dao.entity.TWorkorderRecordPointDto;
import com.essence.interfaces.model.TWorkorderRecordPointEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;


/**
 * @author liwy
 * @since 2023-05-07 12:11:50
 */
@Mapper(componentModel = "spring")
public interface ConverterTWorkorderRecordPointEtoT extends BaseConverter<TWorkorderRecordPointEsu, TWorkorderRecordPointDto> {
}
