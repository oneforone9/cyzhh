package com.essence.service.converter;

import com.essence.dao.entity.StDesigRainPatternDto;
import com.essence.interfaces.model.StDesigRainPatternEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2023-04-24 09:57:30
 */
@Mapper(componentModel = "spring")
public interface ConverterStDesigRainPatternTtoR extends BaseConverter<StDesigRainPatternDto, StDesigRainPatternEsr> {
}
