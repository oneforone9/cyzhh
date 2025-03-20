package com.essence.interfaces.api;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.HtglEsr;
import com.essence.interfaces.model.HtglEsu;
import com.essence.interfaces.model.HtglEsuData;
import com.essence.interfaces.param.HtglEsp;

import java.util.List;

/**
 * 合同管理服务层
 *
 * @author majunjie
 * @since 2024-09-09 17:45:36
 */
public interface NewHtglService extends BaseApi<HtglEsu, HtglEsp, HtglEsr> {


    /**
     * 查询会签单
     *
     * @param id
     * @return
     */
    HtglEsr searchHqdById(String id);

    /**
     * 新增或修改合同
     *
     * @param htglEsuData
     * @return
     */
    HtglEsr addHt(HtglEsuData htglEsuData);

    /**
     * 撤回合同
     *
     * @param userId
     * @param returnList
     * @return
     */
    int returnPrevious(String userId, List<String> returnList);

    /**
     * 回退合同
     *
     * @param esu
     * @return
     */
    int backPrevious(HtglEsu esu);

    /**
     * 判断是否是局机关
     * @param ksName
     * @param ksId
     * @return
     */
    boolean isJjg(String ksName,String ksId);


    /**
     * 判断是否需要填写委托书
     * @param htId
     * @return
     */
    Boolean judgeIsHaveWts(String htId);

}
