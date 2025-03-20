package com.essence.service.converter;

import com.essence.dao.entity.TWorkorderProcessNewestDto;
import com.essence.interfaces.model.TWorkorderProcessNewestEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;


/**
 * @author liwy
 * @since 2023-08-01 16:59:19
 */
@Mapper(componentModel = "spring")
public interface ConverterTWorkorderProcessNewestEtoT extends BaseConverter<TWorkorderProcessNewestEsu, TWorkorderProcessNewestDto> {
}
