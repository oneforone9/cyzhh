package com.essence.service.converter;

import com.essence.dao.entity.ReaFocusGeom;
import com.essence.interfaces.model.ReaFocusGeomEsr;
import com.essence.interfaces.model.WorkorderRecordGeomEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author zhy
 * @since 2022-10-26 14:06:38
 */
@Mapper(componentModel = "spring")
public interface ConverterrecordGeom extends BaseConverter<ReaFocusGeomEsr, WorkorderRecordGeomEsu> {
}
