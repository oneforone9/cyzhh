package com.essence.service.converter;

import com.essence.dao.entity.StPlanOperateDto;
import com.essence.interfaces.model.StPlanOperateEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author liwy
 * @since 2023-07-24 14:16:52
 */
@Mapper(componentModel = "spring")
public interface ConverterStPlanOperateEtoT extends BaseConverter<StPlanOperateEsu, StPlanOperateDto> {
}
