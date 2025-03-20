package com.essence.service.converter;


import com.essence.dao.entity.alg.StCaseProcessDto;
import com.essence.interfaces.model.StCaseProcessEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-04-17 16:30:06
 */
@Mapper(componentModel = "spring")
public interface ConverterStCaseProcessEtoT extends BaseConverter<StCaseProcessEsu, StCaseProcessDto> {
}
