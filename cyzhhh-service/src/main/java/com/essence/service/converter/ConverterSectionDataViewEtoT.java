package com.essence.service.converter;


import com.essence.dao.entity.SectionDataViewDto;
import com.essence.interfaces.model.SectionDataViewEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-01-05 14:59:48
 */
@Mapper(componentModel = "spring")
public interface ConverterSectionDataViewEtoT extends BaseConverter<SectionDataViewEsu, SectionDataViewDto> {
}
