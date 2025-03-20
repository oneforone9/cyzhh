package com.essence.service.converter;


import com.essence.dao.entity.StCrowdDateDto;
import com.essence.interfaces.model.StCrowdDateEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-01-12 17:36:46
 */
@Mapper(componentModel = "spring")
public interface ConverterStCrowdDateTtoR extends BaseConverter<StCrowdDateDto, StCrowdDateEsr> {
}
