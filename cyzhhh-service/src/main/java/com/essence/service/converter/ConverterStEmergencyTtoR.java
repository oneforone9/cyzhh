package com.essence.service.converter;

import com.essence.dao.entity.StEmergencyDto;
import com.essence.interfaces.model.StEmergencyEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author liwy
 * @since 2023-04-13 16:15:00
 */
@Mapper(componentModel = "spring")
public interface ConverterStEmergencyTtoR extends BaseConverter<StEmergencyDto, StEmergencyEsr> {
}
