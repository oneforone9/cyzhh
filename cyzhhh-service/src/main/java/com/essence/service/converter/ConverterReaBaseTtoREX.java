package com.essence.service.converter;

import com.essence.dao.entity.ReaBase;
import com.essence.interfaces.model.ReaBaseEsrEx;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author zhy
 * @since 2022-10-18 17:22:25
 */
@Mapper(componentModel = "spring")
public interface ConverterReaBaseTtoREX extends BaseConverter<ReaBase, ReaBaseEsrEx> {
}
