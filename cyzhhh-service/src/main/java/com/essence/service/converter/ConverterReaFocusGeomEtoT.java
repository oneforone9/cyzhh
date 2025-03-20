package com.essence.service.converter;

import com.essence.dao.entity.ReaFocusGeom;
import com.essence.interfaces.model.ReaFocusGeomEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author zhy
 * @since 2022-10-26 14:06:37
 */
@Mapper(componentModel = "spring")
public interface ConverterReaFocusGeomEtoT extends BaseConverter<ReaFocusGeomEsu, ReaFocusGeom> {
}
