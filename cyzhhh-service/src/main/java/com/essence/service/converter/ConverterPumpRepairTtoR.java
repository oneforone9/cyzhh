package com.essence.service.converter;


import com.essence.dao.entity.PumpRepairEntity;
import com.essence.dao.entity.StRainDateDto;
import com.essence.interfaces.model.PumpRepairEsr;
import com.essence.interfaces.model.StRainDateEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-02-20 14:33:09
 */
@Mapper(componentModel = "spring")
public interface ConverterPumpRepairTtoR extends BaseConverter<PumpRepairEntity, PumpRepairEsr> {
}
