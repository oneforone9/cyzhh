package com.essence.service.converter;

import com.essence.dao.entity.RadarImagesWxDto;
import com.essence.interfaces.model.RadarImagesWxEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-04-23 10:27:51
 */
@Mapper(componentModel = "spring")
public interface ConverterRadarImagesWxEtoT extends BaseConverter<RadarImagesWxEsu, RadarImagesWxDto> {
}
