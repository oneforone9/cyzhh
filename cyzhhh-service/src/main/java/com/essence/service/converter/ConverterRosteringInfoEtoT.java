package com.essence.service.converter;

import com.essence.dao.entity.RosteringInfo;
import com.essence.interfaces.model.RosteringInfoEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author zhy
 * @since 2022-10-25 11:22:54
 */
@Mapper(componentModel = "spring")
public interface ConverterRosteringInfoEtoT extends BaseConverter<RosteringInfoEsu, RosteringInfo> {
}
