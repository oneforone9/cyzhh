package com.essence.service.converter;

import com.essence.dao.entity.WorkorderRecordGeom;
import com.essence.interfaces.model.WorkorderRecordGeomEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author zhy
 * @since 2022-10-30 16:57:21
 */
@Mapper(componentModel = "spring")
public interface ConverterWorkorderRecordGeomEtoT extends BaseConverter<WorkorderRecordGeomEsu, WorkorderRecordGeom> {
}
