package com.essence.web.portal;

import com.essence.common.utils.ResponseResult;
import com.essence.dao.entity.StStbprpAlarmDto;
import com.essence.interfaces.api.StStbprpAlarmService;
import com.essence.interfaces.model.StStbprpAlarmEsr;
import com.essence.interfaces.model.StStbprpAlarmEsu;
import com.essence.interfaces.param.StStbprpAlarmEsp;
import com.essence.web.basecontroller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 水位流量站报警阀值配置表管理
 *
 * @author BINX
 * @since 2023-02-25 16:53:14
 */
@RestController
@RequestMapping("/stStbprpAlarm")
public class StStbprpAlarmController extends BaseController<Long, StStbprpAlarmEsu, StStbprpAlarmEsp, StStbprpAlarmEsr> {
    @Autowired
    private StStbprpAlarmService stStbprpAlarmService;

    public StStbprpAlarmController(StStbprpAlarmService stStbprpAlarmService) {
        super(stStbprpAlarmService);
    }


    /**
     * 编辑数据配置
     *
     * @param stStbprpAlarmEsu
     * @return
     */
    @PostMapping("/editStStbprpAlarm")
    public ResponseResult editStStbprpAlarm(@RequestBody StStbprpAlarmEsu stStbprpAlarmEsu) {
        StStbprpAlarmDto stStbprpAlarmDto = stStbprpAlarmService.selectStStbprpAlarm(stStbprpAlarmEsu);
        if (null != stStbprpAlarmDto) {
            stStbprpAlarmEsu.setId(stStbprpAlarmDto.getId());
            return ResponseResult.success("编辑数据配置成功", stStbprpAlarmService.update(stStbprpAlarmEsu));
        } else {
            return ResponseResult.success("编辑数据配置成功", stStbprpAlarmService.insert(stStbprpAlarmEsu));
        }
    }


    /**
     * 删除数据配置
     *
     * @param stStbprpAlarmEsu
     * @return
     */
    @PostMapping("/deleteStStbprpAlarm")
    public ResponseResult deleteStStbprpAlarm(@RequestBody StStbprpAlarmEsu stStbprpAlarmEsu) {
        return ResponseResult.success("删除数据配置成功", stStbprpAlarmService.deleteStStbprpAlarm(stStbprpAlarmEsu.getStcd()));

    }
}
