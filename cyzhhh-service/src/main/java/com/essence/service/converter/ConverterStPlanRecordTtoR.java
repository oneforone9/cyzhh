package com.essence.service.converter;

import com.essence.dao.entity.StPlanRecordDto;
import com.essence.interfaces.model.StPlanRecordEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author liwy
 * @since 2023-07-18 11:16:20
 */
@Mapper(componentModel = "spring")
public interface ConverterStPlanRecordTtoR extends BaseConverter<StPlanRecordDto, StPlanRecordEsr> {
}
