package com.essence.service.converter;


import com.essence.dao.entity.RewardDealEntity;
import com.essence.interfaces.model.TRewardDealEsu;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-01-04 10:39:19
 */
@Mapper(componentModel = "spring")
public interface ConverterTRewardDealEtoT extends BaseConverter<TRewardDealEsu, RewardDealEntity> {
}
