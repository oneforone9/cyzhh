package com.essence.service.converter;

import com.essence.dao.entity.ViewHysXjDto;
import com.essence.interfaces.model.ViewHysXjEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author majunjie
 * @since 2025-01-09 10:22:27
 */
@Mapper(componentModel = "spring")
public interface ConverterViewHysXjEtoT extends BaseConverter<ViewHysXjEsu, ViewHysXjDto> {
}
