package com.essence.service.converter;


import com.essence.dao.entity.caiyun.StCaiyunMeshDto;
import com.essence.interfaces.model.StCaiyunMeshEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
* 彩云预报网格编号
*
* @author BINX
* @since 2023年5月4日 下午6:27:28
*/
@Mapper(componentModel = "spring")
public interface ConverterStCaiyunMeshTtoR extends BaseConverter<StCaiyunMeshDto, StCaiyunMeshEsr> {
}
