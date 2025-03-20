package com.essence.service.converter;

import com.essence.dao.entity.StOpenBaseDto;
import com.essence.interfaces.model.StOpenBaseEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;


/**
 * @author liwy
 * @since 2023-08-23 14:28:36
 */
@Mapper(componentModel = "spring")
public interface ConverterStOpenBaseEtoT extends BaseConverter<StOpenBaseEsu, StOpenBaseDto> {
}
