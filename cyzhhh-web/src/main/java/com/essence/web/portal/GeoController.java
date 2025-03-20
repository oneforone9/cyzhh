package com.essence.web.portal;

import com.essence.common.utils.Geotools;
import com.essence.common.utils.ResponseResult;
import com.essence.interfaces.model.GraphIntersects;
import org.locationtech.jts.io.ParseException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * geo工具
 *
 * @author zhy
 * @since 2022/11/10 14:501
 */
@RestController
@RequestMapping("/geo")
public class GeoController {
    /**
     * 判断点是否在面的指定距离内
     * @param graphIntersects
     * @return
     * @throws ParseException
     */
    @PostMapping("/dopost")
    public ResponseResult<Boolean> doPost(@Validated @RequestBody GraphIntersects graphIntersects) throws ParseException {
        return ResponseResult.success("计算完成", Geotools.intersects(graphIntersects.getPoint(), graphIntersects.getPolygon(), graphIntersects.getDistance()));
    }
}
