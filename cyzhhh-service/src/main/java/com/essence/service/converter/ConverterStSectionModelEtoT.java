package com.essence.service.converter;


import com.essence.dao.entity.StSectionModelDto;
import com.essence.interfaces.model.StSectionModelEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-04-19 18:15:43
 */
@Mapper(componentModel = "spring")
public interface ConverterStSectionModelEtoT extends BaseConverter<StSectionModelEsu, StSectionModelDto> {
}
