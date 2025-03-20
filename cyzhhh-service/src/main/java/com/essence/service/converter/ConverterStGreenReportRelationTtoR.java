package com.essence.service.converter;

import com.essence.dao.entity.StGreenReportRelationDto;
import com.essence.interfaces.model.StGreenReportRelationEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author liwy
 * @since 2023-03-17 17:19:58
 */
@Mapper(componentModel = "spring")
public interface ConverterStGreenReportRelationTtoR extends BaseConverter<StGreenReportRelationDto, StGreenReportRelationEsr> {
}
