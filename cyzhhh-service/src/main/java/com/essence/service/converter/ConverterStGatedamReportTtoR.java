package com.essence.service.converter;

import com.essence.dao.entity.StGatedamReportDto;
import com.essence.interfaces.model.StGatedamReportEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author liwy
 * @since 2023-03-15 11:56:17
 */
@Mapper(componentModel = "spring")
public interface ConverterStGatedamReportTtoR extends BaseConverter<StGatedamReportDto, StGatedamReportEsr> {
}
