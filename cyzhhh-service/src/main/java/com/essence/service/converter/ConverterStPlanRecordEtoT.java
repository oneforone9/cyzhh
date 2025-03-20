package com.essence.service.converter;

import com.essence.dao.entity.StPlanRecordDto;
import com.essence.interfaces.model.StPlanRecordEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;


/**
 * @author liwy
 * @since 2023-07-18 11:16:17
 */
@Mapper(componentModel = "spring")
public interface ConverterStPlanRecordEtoT extends BaseConverter<StPlanRecordEsu, StPlanRecordDto> {
}
