package com.essence.service.converter;

import com.essence.dao.entity.ViewVideoXjDto;
import com.essence.interfaces.model.ViewVideoXjEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;


/**
 * @author majunjie
 * @since 2025-01-08 14:13:46
 */
@Mapper(componentModel = "spring")
public interface ConverterViewVideoXjTtoR extends BaseConverter<ViewVideoXjDto, ViewVideoXjEsr> {
}
