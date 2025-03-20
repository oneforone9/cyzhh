package com.essence.service.converter;


import com.essence.dao.entity.VideoDrawLineDto;
import com.essence.service.baseconverter.BaseConverter;
import com.essence.interfaces.model.VideoDrawLineEsu;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-02-03 14:51:00
 */
@Mapper(componentModel = "spring")
public interface ConverterVideoDrawLineEtoT extends BaseConverter<VideoDrawLineEsu, VideoDrawLineDto> {
}
