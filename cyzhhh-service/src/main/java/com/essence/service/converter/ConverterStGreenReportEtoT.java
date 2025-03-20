package com.essence.service.converter;

import com.essence.dao.entity.StGreenReportDto;
import com.essence.interfaces.model.StGreenReportEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;


/**
 * @author liwy
 * @since 2023-03-14 15:35:55
 */
@Mapper(componentModel = "spring")
public interface ConverterStGreenReportEtoT extends BaseConverter<StGreenReportEsu, StGreenReportDto> {
}
