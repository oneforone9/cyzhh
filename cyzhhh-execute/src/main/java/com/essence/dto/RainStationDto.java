package com.essence.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class RainStationDto implements Serializable {

    private String IsAllYear;

    private String EleNum;

    private String name;

    private String Lon;

    private String StID;

    private String Lat;
}
