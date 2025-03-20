package com.essence.service.converter;

import com.essence.dao.entity.OrderRosteringRecord;
import com.essence.interfaces.model.OrderRosteringRecordEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author zhy
 * @since 2022-10-31 17:53:19
 */
@Mapper(componentModel = "spring")
public interface ConverterOrderRosteringRecordTtoR extends BaseConverter<OrderRosteringRecord, OrderRosteringRecordEsr> {
}
