package com.essence.service.converter;

import com.essence.dao.entity.StEmergencyDto;
import com.essence.interfaces.model.StEmergencyEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;


/**
 * @author liwy
 * @since 2023-04-13 16:14:56
 */
@Mapper(componentModel = "spring")
public interface ConverterStEmergencyEtoT extends BaseConverter<StEmergencyEsu, StEmergencyDto> {
}
