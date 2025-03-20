package com.essence.service.converter;

import com.essence.dao.entity.RelPersonDepartment;
import com.essence.interfaces.model.RelPersonDepartmentEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author zhy
 * @since 2022-10-24 17:42:58
 */
@Mapper(componentModel = "spring")
public interface ConverterRelPersonDepartmentTtoR extends BaseConverter<RelPersonDepartment, RelPersonDepartmentEsr> {
}
