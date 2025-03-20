package com.essence.web.alg;


import com.essence.interfaces.api.StSectionModelService;
import com.essence.interfaces.model.StSectionModelEsr;
import com.essence.interfaces.model.StSectionModelEsu;
import com.essence.interfaces.param.StSectionModelEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 模型断面基础表管理
 * @author BINX
 * @since 2023-04-19 18:15:39
 */
@RestController
@RequestMapping("/stSectionModel")
public class StSectionModelController extends BaseController<String, StSectionModelEsu, StSectionModelEsp, StSectionModelEsr> {
    @Autowired
    private StSectionModelService stSectionModelService;

    public StSectionModelController(StSectionModelService stSectionModelService) {
        super(stSectionModelService);
    }
}
