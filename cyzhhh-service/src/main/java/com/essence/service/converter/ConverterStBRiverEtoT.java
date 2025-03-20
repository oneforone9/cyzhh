package com.essence.service.converter;


import com.essence.dao.entity.StBRiverDto;
import com.essence.interfaces.model.StBRiverEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2025-01-09 09:08:03
 */
@Mapper(componentModel = "spring")
public interface ConverterStBRiverEtoT extends BaseConverter<StBRiverEsu, StBRiverDto> {
}
