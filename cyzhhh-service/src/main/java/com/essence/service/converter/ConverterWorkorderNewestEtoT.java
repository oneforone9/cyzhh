package com.essence.service.converter;

import com.essence.dao.entity.WorkorderNewest;
import com.essence.interfaces.model.WorkorderNewestEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author zhy
 * @since 2022-10-27 15:26:28
 */
@Mapper(componentModel = "spring")
public interface ConverterWorkorderNewestEtoT extends BaseConverter<WorkorderNewestEsu, WorkorderNewest> {
}
