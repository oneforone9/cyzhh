package com.essence.service.converter;

import com.essence.dao.entity.CyCaseBase;
import com.essence.interfaces.model.CyCaseBaseEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author zhy
 * @since 2023-01-04 18:13:42
 */
@Mapper(componentModel = "spring")
public interface ConverterCyCaseBaseEtoT extends BaseConverter<CyCaseBaseEsu, CyCaseBase> {
}
