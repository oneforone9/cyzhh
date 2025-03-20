package com.essence.service.converter;

import com.essence.dao.entity.StCompanyBaseDto;
import com.essence.interfaces.model.StCompanyBaseEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-02-16 11:58:27
 */
@Mapper(componentModel = "spring")
public interface ConverterStCompanyBaseTtoR extends BaseConverter<StCompanyBaseDto, StCompanyBaseEsr> {
}
