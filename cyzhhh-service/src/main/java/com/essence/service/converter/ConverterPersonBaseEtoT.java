package com.essence.service.converter;

import com.essence.dao.entity.PersonBase;
import com.essence.interfaces.model.PersonBaseEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author zhy
 * @since 2022-10-20 15:07:22
 */
@Mapper(componentModel = "spring")
public interface ConverterPersonBaseEtoT extends BaseConverter<PersonBaseEsu, PersonBase> {
}
