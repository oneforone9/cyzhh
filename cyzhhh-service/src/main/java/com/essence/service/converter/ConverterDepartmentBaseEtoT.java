package com.essence.service.converter;

import com.essence.dao.entity.DepartmentBase;
import com.essence.interfaces.model.DepartmentBaseEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author zhy
 * @since 2022-10-24 16:50:29
 */
@Mapper(componentModel = "spring")
public interface ConverterDepartmentBaseEtoT extends BaseConverter<DepartmentBaseEsu, DepartmentBase> {
}
