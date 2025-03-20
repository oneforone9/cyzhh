package com.essence.service.converter;


import com.essence.dao.entity.StEcologyWaterDto;
import com.essence.interfaces.model.StEcologyWaterEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-02-21 16:34:17
 */
@Mapper(componentModel = "spring")
public interface ConverterStEcologyWaterEtoT extends BaseConverter<StEcologyWaterEsu, StEcologyWaterDto> {
}
