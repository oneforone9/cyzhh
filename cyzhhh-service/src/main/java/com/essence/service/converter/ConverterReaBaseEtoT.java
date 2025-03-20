package com.essence.service.converter;

import com.essence.dao.entity.ReaBase;
import com.essence.interfaces.model.ReaBaseEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author zhy
 * @since 2022-10-18 17:22:24
 */
@Mapper(componentModel = "spring")
public interface ConverterReaBaseEtoT extends BaseConverter<ReaBaseEsu, ReaBase> {
}
