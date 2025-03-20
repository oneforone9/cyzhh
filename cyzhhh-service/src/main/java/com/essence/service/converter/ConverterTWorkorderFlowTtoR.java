package com.essence.service.converter;


import com.essence.dao.entity.TWorkorderFlowDto;
import com.essence.interfaces.model.TWorkorderFlowEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2023-06-07 10:31:18
 */
@Mapper(componentModel = "spring")
public interface ConverterTWorkorderFlowTtoR extends BaseConverter<TWorkorderFlowDto, TWorkorderFlowEsr> {
}
