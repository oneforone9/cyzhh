package com.essence.common.dto.weather;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName WeatherDto
 * @Description TODO
 * @Author zhichao.xing
 * @Date 2020/8/24 18:29
 * @Version 1.0
 **/
public class ReturnForecastDto implements Serializable {
    private String msg;
    private String dateTime;
    private String code;
    private String type;
    private List<QixiangImageDto> data;
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<QixiangImageDto> getData() {
        return data;
    }

    public void setData(List<QixiangImageDto> data) {
        this.data = data;
    }
}
