package com.essence.service.converter;

import com.essence.dao.entity.StOpenBaseDto;
import com.essence.interfaces.model.StOpenBaseEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;


/**
 * @author liwy
 * @since 2023-08-23 14:28:44
 */
@Mapper(componentModel = "spring")
public interface ConverterStOpenBaseTtoR extends BaseConverter<StOpenBaseDto, StOpenBaseEsr> {
}
