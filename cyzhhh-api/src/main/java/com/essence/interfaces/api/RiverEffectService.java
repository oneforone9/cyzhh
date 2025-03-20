package com.essence.interfaces.api;

import com.essence.common.utils.PageUtil;
import com.essence.common.utils.PageUtils;
import com.essence.interfaces.dot.*;

import java.util.List;

public interface RiverEffectService {
    List<EffectCaseStatisticDto> getCaseTypeStatistic(EffectRequestDto effectRequestDto);

    PageUtil<EffectMarkStatistic> getCaseMarkStatistic(EffectRequestDto effectRequestDto);

    List<EffectMarkStatistic> getCaseMarkStatisticList(EffectRequestDto effectRequestDto);

    List<EffectCaseStatisticDto> getCaseChannelStatistic(EffectRequestDto effectRequestDto);

    List<EffectRiverDto> getRiverStatistic(EffectRequestDto effectRequestDto);

    PageUtils<EffectRiverListDto> getRiverStatisticList(EffectRequestDto effectRequestDto);

    PageUtil<EffectGeomStatisticDto> getReaGeomMarkStatistic(EffectRequestDto effectRequestDto);

    List<EffectExportRiverListDto> getRiverStatisticExportList(EffectRequestDto effectRequestDto);
}
