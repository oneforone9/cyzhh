package com.essence.interfaces.api;

import com.essence.common.dto.UserWaterStatisticDto;
import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.UserWaterBaseInfoEsr;
import com.essence.interfaces.model.UserWaterBaseInfoEsu;
import com.essence.interfaces.param.UserWaterBaseInfoEsp;

import java.util.List;

/**
 * @author BINX
 * @since 2023-01-04 17:50:31
 */
public interface UserWaterBaseInfoService extends BaseApi<UserWaterBaseInfoEsu, UserWaterBaseInfoEsp, UserWaterBaseInfoEsr> {

    List<UserWaterStatisticDto> selectByType(String type);
}
