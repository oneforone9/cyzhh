package com.essence.web.station;

import com.essence.interfaces.api.EmergencyTeamService;
import com.essence.interfaces.model.EmergencyTeamEsr;
import com.essence.interfaces.model.EmergencyTeamEsu;
import com.essence.interfaces.param.EmergencyTeamEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 抢险队伍
 *
 * @author zhy
 * @since 2024-07-17 19:32:43
 */
@RestController
@RequestMapping("/emergencyTeam")
public class EmergencyTeamController extends BaseController<Long, EmergencyTeamEsu, EmergencyTeamEsp, EmergencyTeamEsr> {

    @Autowired
    private EmergencyTeamService emergencyTeamService;

    public EmergencyTeamController(EmergencyTeamService emergencyTeamService) {
        super(emergencyTeamService);
    }
}
