package com.essence.service.converter;

import com.essence.dao.entity.RiverGateMaxFlowViewDto;
import com.essence.interfaces.model.RiverGateMaxFlowViewEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-04-25 13:41:08
 */
@Mapper(componentModel = "spring")
public interface ConverterRiverGateMaxFlowViewTtoR extends BaseConverter<RiverGateMaxFlowViewDto, RiverGateMaxFlowViewEsr> {
}
