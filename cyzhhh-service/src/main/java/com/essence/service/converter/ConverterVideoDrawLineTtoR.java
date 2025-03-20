package com.essence.service.converter;


import com.essence.dao.entity.VideoDrawLineDto;
import com.essence.interfaces.model.VideoDrawLineEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-02-03 14:51:04
 */
@Mapper(componentModel = "spring")
public interface ConverterVideoDrawLineTtoR extends BaseConverter<VideoDrawLineDto, VideoDrawLineEsr> {
}
