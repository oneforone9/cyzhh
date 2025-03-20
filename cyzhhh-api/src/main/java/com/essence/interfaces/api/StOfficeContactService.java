package com.essence.interfaces.api;


import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.entity.Paginator;
import com.essence.interfaces.entity.PaginatorParam;
import com.essence.interfaces.model.StOfficeContactEsr;
import com.essence.interfaces.model.StOfficeContactEsu;
import com.essence.interfaces.model.StOfficeContactEsuParam;
import com.essence.interfaces.param.StOfficeContactEsp;

/**
 * 服务层
 * @author liwy
 * @since 2023-03-29 18:53:12
 */
public interface StOfficeContactService extends BaseApi<StOfficeContactEsu, StOfficeContactEsp, StOfficeContactEsr> {

    /**
     * 获取当前登录人的常用联系人
     * @param stOfficeContactEsuParam
     * @param param
     * @return
     */
    Paginator<StOfficeContactEsr> searchByUserId(StOfficeContactEsuParam stOfficeContactEsuParam, PaginatorParam param);

    /**
     * 收藏常用联系人
     * @param stOfficeContactEsu
     * @return
     */
    ResponseResult addStOfficeContact(StOfficeContactEsu stOfficeContactEsu);

    /**
     * 取消收藏常用联系人
     * @param id
     * @return
     */
    Object deleteStOfficeContact(Integer id);
}
