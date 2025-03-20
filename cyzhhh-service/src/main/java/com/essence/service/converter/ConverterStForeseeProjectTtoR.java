package com.essence.service.converter;


import com.essence.dao.entity.StForeseeProjectDto;
import com.essence.interfaces.model.StForeseeProjectEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2023-04-24 11:21:10
 */
@Mapper(componentModel = "spring")
public interface ConverterStForeseeProjectTtoR extends BaseConverter<StForeseeProjectDto, StForeseeProjectEsr> {
}
