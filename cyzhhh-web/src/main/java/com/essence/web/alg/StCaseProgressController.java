package com.essence.web.alg;


import com.essence.interfaces.api.StCaseProgressService;
import com.essence.interfaces.model.StCaseProgressEsr;
import com.essence.interfaces.model.StCaseProgressEsu;
import com.essence.interfaces.param.StCaseProgressEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 方案执行进度表管理
 * @author BINX
 * @since 2023-04-18 17:03:00
 */
@RestController
@RequestMapping("/stCaseProgress")
public class StCaseProgressController extends BaseController<String, StCaseProgressEsu, StCaseProgressEsp, StCaseProgressEsr> {
    @Autowired
    private StCaseProgressService stCaseProgressService;

    public StCaseProgressController(StCaseProgressService stCaseProgressService) {
        super(stCaseProgressService);
    }
}
