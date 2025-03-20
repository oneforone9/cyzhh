package com.essence.service.converter;

import com.essence.dao.entity.VideoStatusRecord;
import com.essence.interfaces.model.VideoStatusRecordEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author zhy
 * @since 2022-10-21 16:36:45
 */
@Mapper(componentModel = "spring")
public interface ConverterVideoStatusRecordEtoT extends BaseConverter<VideoStatusRecordEsu, VideoStatusRecord> {
}
