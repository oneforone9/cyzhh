package com.essence.common.dto.health;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class HealthRiverBelongDto implements Serializable {

    private Integer statuscode;

    private Integer success ;

    private String message ;

    private List<ManageBelongDto> result;
}
