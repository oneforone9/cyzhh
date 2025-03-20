package com.essence.service.converter;

import com.essence.dao.entity.SysDictionaryData;
import com.essence.interfaces.model.SysDictionaryDataEsrEX;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author zhy
 * @since 2022-11-03 17:12:27
 */
@Mapper(componentModel = "spring")
public interface ConverterSysDictionaryDataTtoEX extends BaseConverter<SysDictionaryData, SysDictionaryDataEsrEX> {
}
