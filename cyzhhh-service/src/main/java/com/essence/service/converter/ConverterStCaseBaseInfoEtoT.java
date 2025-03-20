package com.essence.service.converter;


import com.essence.dao.entity.alg.StCaseBaseInfoDto;
import com.essence.interfaces.model.StCaseBaseInfoEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-04-17 16:29:54
 */
@Mapper(componentModel = "spring")
public interface ConverterStCaseBaseInfoEtoT extends BaseConverter<StCaseBaseInfoEsu, StCaseBaseInfoDto> {
}
