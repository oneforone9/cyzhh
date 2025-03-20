package com.essence.service.converter;


import com.essence.dao.entity.StStbprpAlarmDto;
import com.essence.interfaces.model.StStbprpAlarmEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;



/**
 * @author BINX
 * @since 2023-02-25 16:56:12
 */
@Mapper(componentModel = "spring")
public interface ConverterStStbprpAlarmEtoT extends BaseConverter<StStbprpAlarmEsu, StStbprpAlarmDto> {
}
