package com.essence.service.converter;


import com.essence.dao.entity.VPumpDynameicDataDto;
import com.essence.interfaces.model.VPumpDynameicDataEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-04-20 17:29:35
 */
@Mapper(componentModel = "spring")
public interface ConverterVPumpDynameicDataEtoT extends BaseConverter<VPumpDynameicDataEsu, VPumpDynameicDataDto> {
}
