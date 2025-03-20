package com.essence.service.converter;

import com.essence.dao.entity.StWaterGaugeDto;
import com.essence.interfaces.model.StWaterGaugeEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author liwy
 * @since 2023-05-11 18:39:44
 */
@Mapper(componentModel = "spring")
public interface ConverterStWaterGaugeTtoR extends BaseConverter<StWaterGaugeDto, StWaterGaugeEsr> {
}
