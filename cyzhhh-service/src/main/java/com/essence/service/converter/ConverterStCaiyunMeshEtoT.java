package com.essence.service.converter;


import com.essence.dao.entity.caiyun.StCaiyunMeshDto;
import com.essence.interfaces.model.StCaiyunMeshEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
* 彩云预报网格编号
*
* @author BINX
* @since 2023年5月4日 下午3:47:15
*/
@Mapper(componentModel = "spring")
public interface ConverterStCaiyunMeshEtoT extends BaseConverter<StCaiyunMeshEsu, StCaiyunMeshDto> {
}
