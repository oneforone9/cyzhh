package com.essence.service.converter;

import com.essence.dao.entity.WorkorderProcess;
import com.essence.interfaces.model.WorkorderProcessEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author zhy
 * @since 2022-10-27 15:26:31
 */
@Mapper(componentModel = "spring")
public interface ConverterWorkorderProcessEtoT extends BaseConverter<WorkorderProcessEsu, WorkorderProcess> {
}
