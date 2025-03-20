package com.essence.service.converter;


import com.essence.dao.entity.StThirdUserInfoDto;
import com.essence.interfaces.model.StThirdUserInfoEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-04-03 14:31:22
 */
@Mapper(componentModel = "spring")
public interface ConverterStThirdUserInfoTtoR extends BaseConverter<StThirdUserInfoDto, StThirdUserInfoEsr> {
}
