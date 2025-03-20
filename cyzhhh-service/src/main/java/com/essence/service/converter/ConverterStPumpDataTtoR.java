package com.essence.service.converter;

import com.essence.dao.entity.StPumpDataDto;
import com.essence.interfaces.model.StPumpDataEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-04-14 11:36:08
 */
@Mapper(componentModel = "spring")
public interface ConverterStPumpDataTtoR extends BaseConverter<StPumpDataDto, StPumpDataEsr> {
}
