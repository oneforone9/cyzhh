package com.essence.service.converter;


import com.essence.dao.entity.VGateDataDto;
import com.essence.interfaces.model.VGateDataEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2023-04-20 17:35:44
 */
@Mapper(componentModel = "spring")
public interface ConverterVGateDataEtoT extends BaseConverter<VGateDataEsu, VGateDataDto> {
}
