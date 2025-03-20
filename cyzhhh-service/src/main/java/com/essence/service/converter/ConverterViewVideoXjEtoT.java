package com.essence.service.converter;

import com.essence.dao.entity.ViewVideoXjDto;
import com.essence.interfaces.model.ViewVideoXjEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;



/**
 * @author majunjie
 * @since 2025-01-08 14:13:46
 */
@Mapper(componentModel = "spring")
public interface ConverterViewVideoXjEtoT extends BaseConverter<ViewVideoXjEsu, ViewVideoXjDto> {
}
