package com.essence.service.converter;

import com.essence.dao.entity.WorkorderBase;
import com.essence.interfaces.model.WorkorderBaseEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author zhy
 * @since 2022-10-27 15:26:28
 */
@Mapper(componentModel = "spring")
public interface ConverterWorkorderBaseTtoR extends BaseConverter<WorkorderBase, WorkorderBaseEsr> {
}
