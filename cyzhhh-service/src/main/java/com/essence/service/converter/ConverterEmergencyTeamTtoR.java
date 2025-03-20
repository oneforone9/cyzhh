package com.essence.service.converter;

import com.essence.dao.entity.EmergencyTeam;
import com.essence.interfaces.model.EmergencyTeamEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author zhy
 * @since 2024-07-17 19:32:43
 */
@Mapper(componentModel = "spring")
public interface ConverterEmergencyTeamTtoR extends BaseConverter<EmergencyTeam, EmergencyTeamEsr> {
}
