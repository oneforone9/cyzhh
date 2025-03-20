package com.essence.service.converter;

import com.essence.dao.entity.FileBase;
import com.essence.interfaces.model.FileBaseEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author zhy
 * @since 2022-10-28 16:20:14
 */
@Mapper(componentModel = "spring")
public interface ConverterFileBaseEtoT extends BaseConverter<FileBaseEsu, FileBase> {
}
