package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.HtglysxmEsr;
import com.essence.interfaces.model.HtglysxmEsu;
import com.essence.interfaces.param.HtglysxmEsp;

import java.util.List;


/**
 * 合同管理预审项目服务层
 * @author majunjie
 * @since 2024-09-10 14:02:59
 */
public interface HtglysxmService extends BaseApi<HtglysxmEsu, HtglysxmEsp, HtglysxmEsr> {
    void saveYsxnData(List<HtglysxmEsu> ysxmData, String htid);

    void deleteData(List<String> ids);
}
