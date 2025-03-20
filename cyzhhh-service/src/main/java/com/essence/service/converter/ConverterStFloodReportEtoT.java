package com.essence.service.converter;


import com.essence.dao.entity.StFloodReportDto;
import com.essence.interfaces.model.StFloodReportEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author liwy
 * @since 2023-03-13 14:26:26
 */
@Mapper(componentModel = "spring")
public interface ConverterStFloodReportEtoT extends BaseConverter<StFloodReportEsu, StFloodReportDto> {
}
