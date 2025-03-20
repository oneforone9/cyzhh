package com.essence.service.converter;

import com.essence.dao.entity.SysDictionaryType;
import com.essence.interfaces.model.SysDictionaryTypeEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author zhy
 * @since 2022-11-03 17:12:29
 */
@Mapper(componentModel = "spring")
public interface ConverterSysDictionaryTypeEtoT extends BaseConverter<SysDictionaryTypeEsu, SysDictionaryType> {
}
