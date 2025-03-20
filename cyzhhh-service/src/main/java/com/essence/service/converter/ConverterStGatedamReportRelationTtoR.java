package com.essence.service.converter;


import com.essence.dao.entity.StGatedamReportRelationDto;
import com.essence.interfaces.model.StGatedamReportRelationEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author liwy
 * @since 2023-03-15 11:57:07
 */
@Mapper(componentModel = "spring")
public interface ConverterStGatedamReportRelationTtoR extends BaseConverter<StGatedamReportRelationDto, StGatedamReportRelationEsr> {
}
