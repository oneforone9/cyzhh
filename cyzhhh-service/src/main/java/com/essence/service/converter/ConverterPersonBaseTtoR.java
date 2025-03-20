package com.essence.service.converter;

import com.essence.dao.entity.PersonBase;
import com.essence.interfaces.model.PersonBaseEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author zhy
 * @since 2022-10-20 15:07:23
 */
@Mapper(componentModel = "spring")
public interface ConverterPersonBaseTtoR extends BaseConverter<PersonBase, PersonBaseEsr> {
}
