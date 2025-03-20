package com.essence.service.converter;

import com.essence.dao.entity.WorkorderTrack;
import com.essence.interfaces.model.WorkorderTrackEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author zhy
 * @since 2022-11-09 17:49:06
 */
@Mapper(componentModel = "spring")
public interface ConverterWorkorderTrackEtoT extends BaseConverter<WorkorderTrackEsu, WorkorderTrack> {
}
