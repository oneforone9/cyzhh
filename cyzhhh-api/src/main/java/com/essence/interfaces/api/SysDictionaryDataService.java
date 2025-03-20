package com.essence.interfaces.api;

import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.SysDictionaryDataEsr;
import com.essence.interfaces.model.SysDictionaryDataEsrEX;
import com.essence.interfaces.model.SysDictionaryDataEsu;
import com.essence.interfaces.param.SysDictionaryDataEsp;

import java.util.List;

/**
 * 字典数据表服务层
 *
 * @author zhy
 * @since 2022-11-03 17:12:26
 */
public interface SysDictionaryDataService extends BaseApi<SysDictionaryDataEsu, SysDictionaryDataEsp, SysDictionaryDataEsr> {

    /**
     * 根据字典类型查询字典数据返回树形结构
     * @param type
     * @return
     */
    List<SysDictionaryDataEsrEX> findTreeByType(String type);
}
