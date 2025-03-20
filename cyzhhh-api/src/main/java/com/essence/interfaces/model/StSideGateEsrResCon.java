package com.essence.interfaces.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.essence.dao.entity.ReaBase;
import com.essence.interfaces.entity.Esr;
import lombok.Data;

/**
 * 边闸基础表返回实体
 *
 * @author zhy
 * @since 2023-01-17 11:05:24
 */

@Data
public class StSideGateEsrResCon extends Esr {

    private static final long serialVersionUID = -58077454984394602L;
    private String stnm;
    private String managementUnit;
    private String ctime;
    private String zmlb;
    private String M00;
    private String M01;
    private String M02;
    private String VD200;
    private String VD4;
    private String VD8;
    private String VD12;
    private String VD16;
    private String VD20;
    private String VD24;
    private String VD212;
    private String VD220;
    private String reaName;

    private String aVoltage;
    private String bVoltage;
    private String cVoltage;
    private String aElectric;
    private String bElectric;
    private String cElectric;
    private String electric;
    private String flowRate;
    private String pressure;
    private String yPosition;
    private String p1CountTime;
    private String p2CountTime;
    private String liquidHigh;
    private String liquidLow;
    private String p1Run;
    private String p1Hitch;
    private String p2Run;
    private String p2Hitch;




}
