package com.essence.service.converter;


import com.essence.dao.entity.caiyun.StCaiyunPrecipitationRealDto;
import com.essence.interfaces.model.StCaiyunPrecipitationRealEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
* 彩云预报实时数据
*
* @author BINX
* @since 2023年5月4日 下午3:47:15
*/
@Mapper(componentModel = "spring")
public interface ConverterStCaiyunPrecipitationRealEtoT extends BaseConverter<StCaiyunPrecipitationRealEsu, StCaiyunPrecipitationRealDto> {
}
