package com.essence.service.converter;


import com.essence.dao.entity.water.StQpModelDto;
import com.essence.interfaces.model.StQpModelEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
* 水系联通-预报水位-河段断面关联表
*
* @author BINX
* @since 2023年5月11日 下午4:34:54
*/
@Mapper(componentModel = "spring")
public interface ConverterStQpModelTtoR extends BaseConverter<StQpModelDto, StQpModelEsr> {
}
