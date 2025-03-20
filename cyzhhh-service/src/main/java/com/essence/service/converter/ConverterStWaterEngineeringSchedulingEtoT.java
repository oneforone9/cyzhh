package com.essence.service.converter;


import com.essence.dao.entity.water.StWaterEngineeringSchedulingDto;
import com.essence.interfaces.model.StWaterEngineeringSchedulingEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
* 水系联调-工程调度
*
* @author BINX
* @since 2023年5月13日 下午3:50:06
*/
@Mapper(componentModel = "spring")
public interface ConverterStWaterEngineeringSchedulingEtoT extends BaseConverter<StWaterEngineeringSchedulingEsu, StWaterEngineeringSchedulingDto> {
}
