package com.essence.service.converter;

import com.essence.dao.entity.StCompanyBaseDto;
import com.essence.interfaces.model.StCompanyBaseEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-02-16 11:57:49
 */
@Mapper(componentModel = "spring")
public interface ConverterStCompanyBaseEtoT extends BaseConverter<StCompanyBaseEsu, StCompanyBaseDto> {
}
