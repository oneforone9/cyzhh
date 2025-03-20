package com.essence.service.converter;


import com.essence.dao.entity.TDeepWaterDto;
import com.essence.interfaces.model.TDeepWaterEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-01-04 14:46:13
 */
@Mapper(componentModel = "spring")
public interface ConverterTDeepWaterTtoR extends BaseConverter<TDeepWaterDto, TDeepWaterEsr> {
}
