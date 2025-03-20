package com.essence.service.converter;

import com.essence.dao.entity.EventBaseStatusEntity;
import com.essence.interfaces.model.EventBaseStatusEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author zhy
 * @since 2022-10-30 18:06:23
 */
@Mapper(componentModel = "spring")
public interface ConverterEventStatusBaseTtoR extends BaseConverter<EventBaseStatusEntity, EventBaseStatusEsr> {
}
