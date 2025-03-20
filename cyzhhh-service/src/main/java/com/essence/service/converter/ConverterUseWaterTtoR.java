package com.essence.service.converter;


import com.essence.dao.entity.UseWaterDto;
import com.essence.interfaces.model.UseWaterEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-01-04 17:18:03
 */
@Mapper(componentModel = "spring")
public interface ConverterUseWaterTtoR extends BaseConverter<UseWaterDto, UseWaterEsr> {
}
