package com.essence.service.converter;

import com.essence.dao.entity.StGreenReportRelationDto;
import com.essence.interfaces.model.StGreenReportRelationEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author liwy
 * @since 2023-03-17 17:19:50
 */
@Mapper(componentModel = "spring")
public interface ConverterStGreenReportRelationEtoT extends BaseConverter<StGreenReportRelationEsu, StGreenReportRelationDto> {
}
