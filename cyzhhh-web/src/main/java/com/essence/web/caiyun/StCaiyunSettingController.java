package com.essence.web.caiyun;


import com.essence.common.utils.ResponseResult;
import com.essence.dao.StCaiyunMeshDao;
import com.essence.dao.entity.caiyun.StCaiyunMeshDto;
import com.essence.interfaces.api.StCaiyunMeshService;
import com.essence.interfaces.model.Shiduan;
import com.essence.interfaces.model.StCaiyunMeshEsr;
import com.essence.interfaces.model.StCaiyunMeshEsu;
import com.essence.interfaces.param.StCaiyunMeshEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
* 彩云预报设置是否执行定时器
*
* @author BINX
* @since 2023年5月4日 下午3:47:15
*/
@RestController
@RequestMapping("/stCaiyunSetting")
public class StCaiyunSettingController{

    @Autowired
    private StCaiyunMeshDao stCaiyunMeshDao;

    @GetMapping("/setFlag")
    public ResponseResult<Boolean> setFlag(@RequestParam(required = false) Integer model) {
        StCaiyunMeshDto caiYunEnable = stCaiyunMeshDao.kvList("caiYunEnable");
        if(caiYunEnable == null){
            stCaiyunMeshDao.insertKeyValue("caiYunEnable",model==0?"false":"true");
        }else{
            stCaiyunMeshDao.updateKeyValue("caiYunEnable",model==0?"false":"true");
        }
        return ResponseResult.success("执行成功", model!=0?Boolean.TRUE:Boolean.FALSE);
    }

    @GetMapping("/select")
    public ResponseResult<Boolean> select() {
        StCaiyunMeshDto caiYunEnable = stCaiyunMeshDao.kvList("caiYunEnable");
        return ResponseResult.success("执行成功", Objects.equals(caiYunEnable.getLttd(), "true") ?Boolean.TRUE:Boolean.FALSE);
    }
}
