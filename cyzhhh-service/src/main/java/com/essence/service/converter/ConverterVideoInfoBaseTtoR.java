package com.essence.service.converter;

import com.essence.dao.entity.VideoInfoBase;
import com.essence.interfaces.model.VideoInfoBaseEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author zhy
 * @since 2022-10-20 14:20:48
 */
@Mapper(componentModel = "spring")
public interface ConverterVideoInfoBaseTtoR extends BaseConverter<VideoInfoBase, VideoInfoBaseEsr> {
}
