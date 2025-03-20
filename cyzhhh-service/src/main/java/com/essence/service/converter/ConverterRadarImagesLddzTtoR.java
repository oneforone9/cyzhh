package com.essence.service.converter;

import com.essence.dao.entity.RadarImagesLddzDto;
import com.essence.interfaces.model.RadarImagesLddzEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-04-23 10:29:04
 */
@Mapper(componentModel = "spring")
public interface ConverterRadarImagesLddzTtoR extends BaseConverter<RadarImagesLddzDto, RadarImagesLddzEsr> {
}
