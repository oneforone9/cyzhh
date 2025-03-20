package com.essence.service.converter;


import com.essence.dao.entity.caiyun.StCaiyunPrecipitationHistoryDto;
import com.essence.interfaces.model.StCaiyunPrecipitationHistoryEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
* 彩云预报历史数据
*
* @author BINX
* @since 2023年5月4日 下午3:47:15
*/
@Mapper(componentModel = "spring")
public interface ConverterStCaiyunPrecipitationHistoryEtoT extends BaseConverter<StCaiyunPrecipitationHistoryEsu, StCaiyunPrecipitationHistoryDto> {
}
