package com.essence.service.converter;

import com.essence.dao.entity.FloodIncident;
import com.essence.interfaces.model.FloodIncidentEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author zhy
 * @since 2024-07-17 19:32:42
 */
@Mapper(componentModel = "spring")
public interface ConverterFloodIncidentEtoT extends BaseConverter<FloodIncidentEsu, FloodIncident> {
}
