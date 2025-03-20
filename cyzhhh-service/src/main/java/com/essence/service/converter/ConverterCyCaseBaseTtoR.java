package com.essence.service.converter;

import com.essence.dao.entity.CyCaseBase;
import com.essence.interfaces.model.CyCaseBaseEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author zhy
 * @since 2023-01-04 18:13:43
 */
@Mapper(componentModel = "spring")
public interface ConverterCyCaseBaseTtoR extends BaseConverter<CyCaseBase, CyCaseBaseEsr> {
}
