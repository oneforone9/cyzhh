package com.essence.service.converter;

import com.essence.dao.entity.ViewHtscDto;
import com.essence.interfaces.model.ViewHtscEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2024-09-13 16:41:09
 */
@Mapper(componentModel = "spring")
public interface ConverterViewHtscTtoR extends BaseConverter<ViewHtscDto, ViewHtscEsr> {
}
