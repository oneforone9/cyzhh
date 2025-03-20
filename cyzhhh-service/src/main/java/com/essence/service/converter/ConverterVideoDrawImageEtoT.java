package com.essence.service.converter;


import com.essence.dao.entity.VideoDrawImageDto;
import com.essence.interfaces.model.VideoDrawImageEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-02-03 17:10:24
 */
@Mapper(componentModel = "spring")
public interface ConverterVideoDrawImageEtoT extends BaseConverter<VideoDrawImageEsu, VideoDrawImageDto> {
}
