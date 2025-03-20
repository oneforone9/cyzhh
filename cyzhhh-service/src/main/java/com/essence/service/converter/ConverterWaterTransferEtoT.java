package com.essence.service.converter;

import com.essence.dao.entity.WaterTransferDto;
import com.essence.interfaces.model.WaterTransferEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-05-09 10:07:48
 */
@Mapper(componentModel = "spring")
public interface ConverterWaterTransferEtoT extends BaseConverter<WaterTransferEsu, WaterTransferDto> {
}
