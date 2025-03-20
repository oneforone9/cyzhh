package com.essence.service.converter;


import com.essence.dao.entity.StSectionModelDto;
import com.essence.interfaces.model.StSectionModelEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-04-19 18:15:44
 */
@Mapper(componentModel = "spring")
public interface ConverterStSectionModelTtoR extends BaseConverter<StSectionModelDto, StSectionModelEsr> {
}
