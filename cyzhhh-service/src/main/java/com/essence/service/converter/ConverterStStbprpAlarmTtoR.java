package com.essence.service.converter;

import com.essence.dao.entity.StStbprpAlarmDto;
import com.essence.interfaces.model.StStbprpAlarmEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-02-25 16:56:20
 */
@Mapper(componentModel = "spring")
public interface ConverterStStbprpAlarmTtoR extends BaseConverter<StStbprpAlarmDto, StStbprpAlarmEsr> {
}
