package com.essence.service.converter;


import com.essence.dao.entity.StPumpDataDto;
import com.essence.interfaces.model.StPumpDataEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-04-14 11:36:08
 */
@Mapper(componentModel = "spring")
public interface ConverterStPumpDataEtoT extends BaseConverter<StPumpDataEsu, StPumpDataDto> {
}
