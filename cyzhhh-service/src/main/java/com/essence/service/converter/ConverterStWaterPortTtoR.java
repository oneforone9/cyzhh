package com.essence.service.converter;

import com.essence.dao.entity.StWaterPortDto;
import com.essence.interfaces.model.StWaterPortEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-02-22 17:12:59
 */
@Mapper(componentModel = "spring")
public interface ConverterStWaterPortTtoR extends BaseConverter<StWaterPortDto, StWaterPortEsr> {
}
