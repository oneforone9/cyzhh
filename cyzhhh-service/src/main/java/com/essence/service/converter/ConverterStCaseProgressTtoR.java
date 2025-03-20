package com.essence.service.converter;


import com.essence.dao.entity.StCaseProgressDto;
import com.essence.interfaces.model.StCaseProgressEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-04-18 17:03:05
 */
@Mapper(componentModel = "spring")
public interface ConverterStCaseProgressTtoR extends BaseConverter<StCaseProgressDto, StCaseProgressEsr> {
}
