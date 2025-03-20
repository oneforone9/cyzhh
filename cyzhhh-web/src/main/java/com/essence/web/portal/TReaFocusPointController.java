package com.essence.web.portal;

import com.essence.interfaces.api.TReaFocusPointService;
import com.essence.interfaces.model.TReaFocusPointEsr;
import com.essence.interfaces.model.TReaFocusPointEsu;
import com.essence.interfaces.param.TReaFocusPointEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 打卡点位管理
 * @author liwy
 * @since 2023-05-06 10:01:54
 */
@RestController
@RequestMapping("/tReaFocusPoint")
public class TReaFocusPointController extends BaseController<Long, TReaFocusPointEsu, TReaFocusPointEsp, TReaFocusPointEsr> {
    @Autowired
    private TReaFocusPointService tReaFocusPointService;

    public TReaFocusPointController(TReaFocusPointService tReaFocusPointService) {
        super(tReaFocusPointService);
    }
}
