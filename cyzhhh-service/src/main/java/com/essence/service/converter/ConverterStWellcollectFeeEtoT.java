package com.essence.service.converter;

import com.essence.dao.entity.StWellcollectFeeDto;
import com.essence.interfaces.model.StWellcollectFeeEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author bird
 * @since 2023-01-04 18:01:08
 */
@Mapper(componentModel = "spring")
public interface ConverterStWellcollectFeeEtoT extends BaseConverter<StWellcollectFeeEsu, StWellcollectFeeDto> {
}
