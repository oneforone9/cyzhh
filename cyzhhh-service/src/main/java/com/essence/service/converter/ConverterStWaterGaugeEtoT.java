package com.essence.service.converter;

import com.essence.dao.entity.StWaterGaugeDto;
import com.essence.interfaces.model.StWaterGaugeEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;


/**
 * @author liwy
 * @since 2023-05-11 18:39:33
 */
@Mapper(componentModel = "spring")
public interface ConverterStWaterGaugeEtoT extends BaseConverter<StWaterGaugeEsu, StWaterGaugeDto> {
}
