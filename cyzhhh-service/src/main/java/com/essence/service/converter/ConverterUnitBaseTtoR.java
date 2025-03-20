package com.essence.service.converter;

import com.essence.dao.entity.UnitBase;
import com.essence.interfaces.model.UnitBaseEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author zhy
 * @since 2022-10-20 14:16:38
 */
@Mapper(componentModel = "spring")
public interface ConverterUnitBaseTtoR extends BaseConverter<UnitBase, UnitBaseEsr> {
}
