package com.essence.service.converter;


import com.essence.dao.entity.VideoDrawImageDto;
import com.essence.interfaces.model.VideoDrawImageEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-02-03 17:10:50
 */
@Mapper(componentModel = "spring")
public interface ConverterVideoDrawImageTtoR extends BaseConverter<VideoDrawImageDto, VideoDrawImageEsr> {
}
