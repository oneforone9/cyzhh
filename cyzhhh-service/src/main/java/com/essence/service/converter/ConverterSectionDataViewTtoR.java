package com.essence.service.converter;


import com.essence.dao.entity.SectionDataViewDto;
import com.essence.interfaces.model.SectionDataViewEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-01-05 14:59:49
 */
@Mapper(componentModel = "spring")
public interface ConverterSectionDataViewTtoR extends BaseConverter<SectionDataViewDto, SectionDataViewEsr> {
}
