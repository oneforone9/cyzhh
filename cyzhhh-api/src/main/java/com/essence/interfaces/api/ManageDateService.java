package com.essence.interfaces.api;

import com.essence.common.dto.AnCaseTypeDto;
import com.essence.common.dto.YearCountStatisticDto;
import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.TRewardDealEsr;
import com.essence.interfaces.model.TRewardDealEsu;
import com.essence.interfaces.param.TRewardDealEsp;

import java.io.InputStream;
import java.util.List;

public interface ManageDateService extends BaseApi<TRewardDealEsu, TRewardDealEsp, TRewardDealEsr> {
    void upload(InputStream inputStream);

    List<AnCaseTypeDto> getStatistic(String str, String end);

    YearCountStatisticDto YearCountStatisticDto(String str, String end);
}
