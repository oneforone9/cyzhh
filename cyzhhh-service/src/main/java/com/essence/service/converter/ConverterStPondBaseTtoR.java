package com.essence.service.converter;

import com.essence.dao.entity.StPondBaseDto;
import com.essence.interfaces.model.StPondBaseEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;


/**
 * @author liwy
 * @since 2023-04-03 14:45:32
 */
@Mapper(componentModel = "spring")
public interface ConverterStPondBaseTtoR extends BaseConverter<StPondBaseDto, StPondBaseEsr> {
}
