package com.essence.service.converter;

import com.essence.interfaces.model.UnitBaseEsr;
import com.essence.interfaces.model.UnitBaseEsrEx;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author zhy
 * @since 2022-10-20 14:16:38
 */
@Mapper(componentModel = "spring")
public interface ConverterUnitBaseRtoREX extends BaseConverter<UnitBaseEsr, UnitBaseEsrEx> {
}
