package com.essence.service.converter;


import com.essence.dao.entity.alg.StCaseResDto;
import com.essence.interfaces.model.StCaseResEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-04-18 14:39:14
 */
@Mapper(componentModel = "spring")
public interface ConverterStCaseResTtoR extends BaseConverter<StCaseResDto, StCaseResEsr> {
}
