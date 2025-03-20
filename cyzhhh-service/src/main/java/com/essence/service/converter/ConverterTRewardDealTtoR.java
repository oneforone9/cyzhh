package com.essence.service.converter;


import com.essence.dao.entity.RewardDealEntity;
import com.essence.interfaces.model.TRewardDealEsr;
import com.essence.service.baseconverter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author BINX
 * @since 2023-01-04 10:39:20
 */
@Mapper(componentModel = "spring")
public interface ConverterTRewardDealTtoR extends BaseConverter<RewardDealEntity, TRewardDealEsr> {
}
