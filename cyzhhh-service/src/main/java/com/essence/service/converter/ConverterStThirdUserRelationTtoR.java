package com.essence.service.converter;


import com.essence.dao.entity.StThirdUserRelationDto;
import com.essence.interfaces.model.StThirdUserRelationEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-04-04 14:45:11
 */
@Mapper(componentModel = "spring")
public interface ConverterStThirdUserRelationTtoR extends BaseConverter<StThirdUserRelationDto, StThirdUserRelationEsr> {
}
