package com.essence.web.caiyun;


import com.essence.interfaces.api.StCaiyunMeshService;
import com.essence.interfaces.model.StCaiyunMeshEsr;
import com.essence.interfaces.model.StCaiyunMeshEsu;
import com.essence.interfaces.param.StCaiyunMeshEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* 彩云预报网格编号
*
* @author BINX
* @since 2023年5月4日 下午3:47:15
*/
@RestController
@RequestMapping("/stCaiyunMesh")
public class StCaiyunMeshController extends BaseController<String, StCaiyunMeshEsu, StCaiyunMeshEsp, StCaiyunMeshEsr> {

    @Autowired
    private StCaiyunMeshService stCaiyunMeshService;

    public StCaiyunMeshController(StCaiyunMeshService stCaiyunMeshService) {
        super(stCaiyunMeshService);
    }
}
