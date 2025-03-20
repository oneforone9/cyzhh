package com.essence.interfaces.api;

import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.HtglfjEsr;
import com.essence.interfaces.model.HtglfjEsu;
import com.essence.interfaces.param.HtglfjEsp;

import java.util.List;
import java.util.Map;

/**
 * 合同管理附件服务层
 * @author majunjie
 * @since 2024-09-10 14:02:25
 */
public interface HtglfjService extends BaseApi<HtglfjEsu, HtglfjEsp, HtglfjEsr> {
    void saveFjData(List<HtglfjEsu> fjData, String htid);
    void saveFjDatas(List<HtglfjEsu> fjData);

    void deleteData(List<String> ids);
}
