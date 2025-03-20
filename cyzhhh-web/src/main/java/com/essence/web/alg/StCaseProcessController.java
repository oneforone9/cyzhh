package com.essence.web.alg;


import com.essence.interfaces.api.StCaseProcessService;
import com.essence.interfaces.model.StCaseProcessEsr;
import com.essence.interfaces.model.StCaseProcessEsu;
import com.essence.interfaces.param.StCaseProcessEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 方案执行过程表-存放入参等信息管理
 * @author BINX
 * @since 2023-04-17 16:30:06
 */
@RestController
@RequestMapping("/stCaseProcess")
public class StCaseProcessController extends BaseController<String, StCaseProcessEsu, StCaseProcessEsp, StCaseProcessEsr> {
    @Autowired
    private StCaseProcessService stCaseProcessService;

    public StCaseProcessController(StCaseProcessService stCaseProcessService) {
        super(stCaseProcessService);
    }
}
