package com.essence.service.converter;

import com.essence.dao.entity.StRainDateDto;
import com.essence.interfaces.model.StRainDateEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-02-20 14:33:08
 */
@Mapper(componentModel = "spring")
public interface ConverterStRainDateEtoT extends BaseConverter<StRainDateEsu, StRainDateDto> {
}
