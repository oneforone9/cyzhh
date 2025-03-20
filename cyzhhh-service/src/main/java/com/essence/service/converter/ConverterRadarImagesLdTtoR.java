package com.essence.service.converter;

import com.essence.dao.entity.RadarImagesLdDto;
import com.essence.interfaces.model.RadarImagesLdEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-04-23 10:28:31
 */
@Mapper(componentModel = "spring")
public interface ConverterRadarImagesLdTtoR extends BaseConverter<RadarImagesLdDto, RadarImagesLdEsr> {
}
