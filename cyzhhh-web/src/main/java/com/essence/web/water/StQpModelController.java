package com.essence.web.water;


import com.essence.interfaces.api.StQpModelService;
import com.essence.interfaces.model.StQpModelEsr;
import com.essence.interfaces.model.StQpModelEsu;
import com.essence.interfaces.param.StQpModelEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* 水系联通-预报水位-河段断面关联表
*
* @author BINX
* @since 2023年5月11日 下午4:34:54
*/
@RestController
@RequestMapping("/stQpModel")
public class StQpModelController extends BaseController<String, StQpModelEsu, StQpModelEsp, StQpModelEsr> {

    @Autowired
    private StQpModelService stQpModelService;

    public StQpModelController(StQpModelService stQpModelService) {
        super(stQpModelService);
    }
}
