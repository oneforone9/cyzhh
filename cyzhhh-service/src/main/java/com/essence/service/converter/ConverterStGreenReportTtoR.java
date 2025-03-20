package com.essence.service.converter;

import com.essence.dao.entity.StGreenReportDto;
import com.essence.interfaces.model.StGreenReportEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;


/**
 * @author liwy
 * @since 2023-03-14 15:36:11
 */
@Mapper(componentModel = "spring")
public interface ConverterStGreenReportTtoR extends BaseConverter<StGreenReportDto, StGreenReportEsr> {
}
