package com.essence.interfaces.model;

import com.essence.common.constant.ItemConstant;
import lombok.Data;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 点buffer的范围是否与图形相交
 * @author zhy
 * @since 2022/11/10 14:52
 */
@Data
public class GraphIntersects {
    /**
     * 点坐标格式WKT
     * @mock POINT (8.671532846715365 59.255474452554836)
     */
    @NotEmpty(message = "点坐标不能为空")
    private String point;
    /**
     * 面坐标格式WKT
     * @mock MULTIPOLYGON(((8.671532846715365 59.255474452554836,113.67123343062497 64.67799420101966,119.47445255474474 -47.69343065693431,14.474751970835143 -53.11595040539913,8.671532846715365 59.255474452554836)))
     */
    @NotEmpty(message = "面坐标不能为空")
    private String polygon;
    /**
     * 距离 单位米
     * @mock 111.1
     */
    @NotNull(message = "距离不能为空")
    private Double distance;

    @AssertTrue(message = "点坐标格式格式错误")
    private boolean  isPoint(){
        if (null == this.point){
            return true;
        }
        if (point.startsWith("POINT")){
            return true;
        }
        return false;
    }

    @AssertTrue(message = "面坐标格式格式错误")
    private boolean  isPolygon(){
        if (null == this.polygon){
            return true;
        }
        if (polygon.startsWith("MULTIPOLYGON")){
            return true;
        }
        return false;
    }
}
