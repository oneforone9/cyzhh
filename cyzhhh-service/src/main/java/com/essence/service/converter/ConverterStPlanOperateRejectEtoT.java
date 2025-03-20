package com.essence.service.converter;


import com.essence.dao.entity.StPlanOperateRejectDto;
import com.essence.interfaces.model.StPlanOperateRejectEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-09-11 17:52:36
 */
@Mapper(componentModel = "spring")
public interface ConverterStPlanOperateRejectEtoT extends BaseConverter<StPlanOperateRejectEsu, StPlanOperateRejectDto> {
}
