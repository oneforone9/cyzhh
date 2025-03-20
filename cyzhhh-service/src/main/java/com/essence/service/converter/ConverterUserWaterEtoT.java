package com.essence.service.converter;


import com.essence.dao.entity.UserWaterDto;
import com.essence.interfaces.model.UserWaterEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-01-04 17:50:30
 */
@Mapper(componentModel = "spring")
public interface ConverterUserWaterEtoT extends BaseConverter<UserWaterEsu, UserWaterDto> {
}
