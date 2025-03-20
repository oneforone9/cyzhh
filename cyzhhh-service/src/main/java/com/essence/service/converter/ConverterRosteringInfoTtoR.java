package com.essence.service.converter;

import com.essence.dao.entity.RosteringInfo;
import com.essence.interfaces.model.RosteringInfoEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author zhy
 * @since 2022-10-25 11:22:55
 */
@Mapper(componentModel = "spring")
public interface ConverterRosteringInfoTtoR extends BaseConverter<RosteringInfo, RosteringInfoEsr> {
}
