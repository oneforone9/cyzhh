package com.essence.service.converter;

import com.essence.dao.entity.StGatedamReportDto;
import com.essence.interfaces.model.StGatedamReportEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author liwy
 * @since 2023-03-15 11:56:11
 */
@Mapper(componentModel = "spring")
public interface ConverterStGatedamReportEtoT extends BaseConverter<StGatedamReportEsu, StGatedamReportDto> {
}
