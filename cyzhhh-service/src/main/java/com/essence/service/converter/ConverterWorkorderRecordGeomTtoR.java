package com.essence.service.converter;

import com.essence.dao.entity.WorkorderRecordGeom;
import com.essence.interfaces.model.WorkorderRecordGeomEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author zhy
 * @since 2022-10-30 16:57:21
 */
@Mapper(componentModel = "spring")
public interface ConverterWorkorderRecordGeomTtoR extends BaseConverter<WorkorderRecordGeom, WorkorderRecordGeomEsr> {
}
