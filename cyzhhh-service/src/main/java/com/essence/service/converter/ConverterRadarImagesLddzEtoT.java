package com.essence.service.converter;

import com.essence.dao.entity.RadarImagesLddzDto;
import com.essence.interfaces.model.RadarImagesLddzEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-04-23 10:29:03
 */
@Mapper(componentModel = "spring")
public interface ConverterRadarImagesLddzEtoT extends BaseConverter<RadarImagesLddzEsu, RadarImagesLddzDto> {
}
