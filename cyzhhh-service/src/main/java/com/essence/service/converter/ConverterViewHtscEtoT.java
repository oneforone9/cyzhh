package com.essence.service.converter;


import com.essence.dao.entity.ViewHtscDto;
import com.essence.interfaces.model.ViewHtscEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2024-09-13 16:41:09
 */
@Mapper(componentModel = "spring")
public interface ConverterViewHtscEtoT extends BaseConverter<ViewHtscEsu, ViewHtscDto> {
}
