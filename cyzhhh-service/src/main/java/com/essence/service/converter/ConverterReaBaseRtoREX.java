package com.essence.service.converter;

import com.essence.interfaces.model.ReaBaseEsr;
import com.essence.interfaces.model.ReaBaseEsrEx;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author zhy
 * @since 2022-10-18 17:22:25
 */
@Mapper(componentModel = "spring")
public interface ConverterReaBaseRtoREX extends BaseConverter<ReaBaseEsr, ReaBaseEsrEx> {
}
