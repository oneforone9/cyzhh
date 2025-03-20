package com.essence.interfaces.api;

import com.essence.interfaces.baseapi.BaseApi;
import com.essence.interfaces.param.StCaiyunMeshEsp;
import com.essence.interfaces.model.StCaiyunMeshEsu;
import com.essence.interfaces.model.StCaiyunMeshEsr;

/**
* 彩云预报网格编号 (st_caiyun_mesh)表数据库服务层
*
* @author BINX
* @since 2023年5月4日 下午3:47:15
*/
public interface StCaiyunMeshService extends BaseApi<StCaiyunMeshEsu, StCaiyunMeshEsp, StCaiyunMeshEsr> {
}