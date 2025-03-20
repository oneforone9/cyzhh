package com.essence.interfaces.api.third;


import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.model.StThirdUserInfoEsr;
import com.essence.interfaces.model.StThirdUserInfoEsu;
import com.essence.interfaces.param.StThirdUserInfoEsp;

/**
 * 对接第三方用户基本信息 单点登录服务层
 * @author BINX
 * @since 2023-04-03 14:31:22
 */
public interface StThirdUserInfoService extends BaseApi<StThirdUserInfoEsu, StThirdUserInfoEsp, StThirdUserInfoEsr> {
    void addOrUpdateUserInfo(StThirdUserInfoEsu stThirdUserInfoEsu);

    void deleteByThirdUserId(String thirdUserId);

    String getThirdUserIdAes(String userId,String companyName) throws Exception;
}
