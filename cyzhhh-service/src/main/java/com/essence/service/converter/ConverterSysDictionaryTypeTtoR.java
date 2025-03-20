package com.essence.service.converter;

import com.essence.dao.entity.SysDictionaryType;
import com.essence.interfaces.model.SysDictionaryTypeEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author zhy
 * @since 2022-11-03 17:12:30
 */
@Mapper(componentModel = "spring")
public interface ConverterSysDictionaryTypeTtoR extends BaseConverter<SysDictionaryType, SysDictionaryTypeEsr> {
}
