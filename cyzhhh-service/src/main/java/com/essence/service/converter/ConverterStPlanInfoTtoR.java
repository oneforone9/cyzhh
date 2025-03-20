package com.essence.service.converter;


import com.essence.dao.entity.StPlanInfoDto;
import com.essence.interfaces.model.StPlanInfoEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author liwy
 * @since 2023-07-13 14:40:47
 */
@Mapper(componentModel = "spring")
public interface ConverterStPlanInfoTtoR extends BaseConverter<StPlanInfoDto, StPlanInfoEsr> {
}
