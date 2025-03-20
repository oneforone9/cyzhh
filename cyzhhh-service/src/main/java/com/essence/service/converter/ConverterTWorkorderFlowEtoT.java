package com.essence.service.converter;

import com.essence.dao.entity.TWorkorderFlowDto;
import com.essence.interfaces.model.TWorkorderFlowEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2023-06-07 10:31:17
 */
@Mapper(componentModel = "spring")
public interface ConverterTWorkorderFlowEtoT extends BaseConverter<TWorkorderFlowEsu, TWorkorderFlowDto> {
}
