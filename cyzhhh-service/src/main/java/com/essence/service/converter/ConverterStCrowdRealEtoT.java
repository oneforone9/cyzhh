package com.essence.service.converter;

import com.essence.dao.entity.StCrowdRealDto;
import com.essence.interfaces.model.StCrowdRealEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;


/**
 * @author BINX
 * @since 2023-02-28 11:44:27
 */
@Mapper(componentModel = "spring")
public interface ConverterStCrowdRealEtoT extends BaseConverter<StCrowdRealEsu, StCrowdRealDto> {
}
