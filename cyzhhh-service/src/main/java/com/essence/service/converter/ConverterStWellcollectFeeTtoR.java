package com.essence.service.converter;

import com.essence.dao.entity.StWellcollectFeeDto;
import com.essence.interfaces.model.StWellcollectFeeEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author bird
 * @since 2023-01-04 18:01:10
 */
@Mapper(componentModel = "spring")
public interface ConverterStWellcollectFeeTtoR extends BaseConverter<StWellcollectFeeDto, StWellcollectFeeEsr> {
}
