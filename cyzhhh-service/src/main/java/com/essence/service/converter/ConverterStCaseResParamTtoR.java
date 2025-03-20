package com.essence.service.converter;


import com.essence.dao.entity.alg.StCaseResParamDto;
import com.essence.interfaces.model.StCaseResParamEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
* 方案执行结果表入参表
*
* @author BINX
* @since 2023年4月28日 下午5:45:25
*/
@Mapper(componentModel = "spring")
public interface ConverterStCaseResParamTtoR extends BaseConverter<StCaseResParamDto, StCaseResParamEsr> {
}
