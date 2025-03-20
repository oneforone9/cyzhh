package com.essence.service.converter;

import com.essence.dao.entity.StSideGateDto;
import com.essence.interfaces.model.StSideGateEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-01-17 11:05:22
 */
@Mapper(componentModel = "spring")
public interface ConverterStSideGateEtoT extends BaseConverter<StSideGateEsu, StSideGateDto> {
}
