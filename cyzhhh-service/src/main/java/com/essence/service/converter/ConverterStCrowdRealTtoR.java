package com.essence.service.converter;

import com.essence.dao.entity.StCrowdRealDto;
import com.essence.interfaces.model.StCrowdRealEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;


/**
 * @author BINX
 * @since 2023-02-28 11:44:35
 */
@Mapper(componentModel = "spring")
public interface ConverterStCrowdRealTtoR extends BaseConverter<StCrowdRealDto, StCrowdRealEsr> {
}
