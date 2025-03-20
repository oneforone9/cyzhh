package com.essence.service.converter;
import com.essence.dao.entity.TWorkorderProcessNewestDto;
import com.essence.interfaces.model.TWorkorderProcessNewestEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author liwy
 * @since 2023-08-01 16:59:21
 */
@Mapper(componentModel = "spring")
public interface ConverterTWorkorderProcessNewestTtoR extends BaseConverter<TWorkorderProcessNewestDto, TWorkorderProcessNewestEsr> {
}
