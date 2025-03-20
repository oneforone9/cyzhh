package com.essence.service.converter;


import com.essence.dao.entity.StPlanOperateRejectDto;
import com.essence.interfaces.model.StPlanOperateRejectEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-09-11 17:52:37
 */
@Mapper(componentModel = "spring")
public interface ConverterStPlanOperateRejectTtoR extends BaseConverter<StPlanOperateRejectDto, StPlanOperateRejectEsr> {
}
