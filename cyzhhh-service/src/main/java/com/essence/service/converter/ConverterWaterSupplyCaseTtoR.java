package com.essence.service.converter;

import com.essence.dao.entity.WaterSupplyCaseDto;
import com.essence.interfaces.model.WaterSupplyCaseEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-05-04 19:17:59
 */
@Mapper(componentModel = "spring")
public interface ConverterWaterSupplyCaseTtoR extends BaseConverter<WaterSupplyCaseDto, WaterSupplyCaseEsr> {
}
