package com.essence.service.converter;


import com.essence.dao.entity.StWaterPortDto;
import com.essence.interfaces.model.StWaterPortEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-02-22 17:12:52
 */
@Mapper(componentModel = "spring")
public interface ConverterStWaterPortEtoT extends BaseConverter<StWaterPortEsu, StWaterPortDto> {
}
