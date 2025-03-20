package com.essence.service.converter;


import com.essence.dao.entity.StCaseProgressDto;
import com.essence.interfaces.model.StCaseProgressEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-04-18 17:03:04
 */
@Mapper(componentModel = "spring")
public interface ConverterStCaseProgressEtoT extends BaseConverter<StCaseProgressEsu, StCaseProgressDto> {
}
