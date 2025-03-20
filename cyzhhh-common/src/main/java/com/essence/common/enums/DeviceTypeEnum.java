package com.essence.common.enums;

public enum DeviceTypeEnum {

    ZZ("ZZ","水位"),
    ZQ("ZQ","流量");

    private String type;

    private String text;

    DeviceTypeEnum(String type, String text) {
        this.type = type;
        this.text = text;
    }
}
