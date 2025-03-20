package com.essence.common.dto.health;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class HealthRiverReciveDto implements Serializable {

    private Integer statuscode;

    private Integer success ;

    private String message ;

    private List<DataInfoDto> result;
}
