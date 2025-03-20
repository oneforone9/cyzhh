package com.essence.service.converter;

import com.essence.dao.entity.RelReaDepartment;
import com.essence.interfaces.model.RelReaDepartmentEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author zhy
 * @since 2022-10-24 17:43:26
 */
@Mapper(componentModel = "spring")
public interface ConverterRelReaDepartmentTtoR extends BaseConverter<RelReaDepartment, RelReaDepartmentEsr> {
}
