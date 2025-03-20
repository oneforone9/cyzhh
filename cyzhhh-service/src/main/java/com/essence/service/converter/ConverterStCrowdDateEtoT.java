package com.essence.service.converter;

import com.essence.dao.entity.StCrowdDateDto;
import com.essence.interfaces.model.StCrowdDateEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-01-12 17:36:45
 */
@Mapper(componentModel = "spring")
public interface ConverterStCrowdDateEtoT extends BaseConverter<StCrowdDateEsu, StCrowdDateDto> {
}
