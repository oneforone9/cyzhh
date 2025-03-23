package com.essence.service.converter;

import com.essence.dao.entity.FloodIncident;
import com.essence.interfaces.model.FloodIncidentEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author zhy
 * @since 2022-10-30 18:06:23
 */
@Mapper(componentModel = "spring")
public interface ConverterFloodIncidentTtoR extends BaseConverter<FloodIncident, FloodIncidentEsr> {
}
