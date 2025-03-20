package com.essence.service.converter;

import com.essence.dao.entity.StFloodReportDto;
import com.essence.interfaces.model.StFloodReportEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author liwy
 * @since 2023-03-13 14:26:36
 */
@Mapper(componentModel = "spring")
public interface ConverterStFloodReportTtoR extends BaseConverter<StFloodReportDto, StFloodReportEsr> {
}
