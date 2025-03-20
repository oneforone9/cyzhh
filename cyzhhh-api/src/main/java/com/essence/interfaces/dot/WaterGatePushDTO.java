package com.essence.interfaces.dot;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class WaterGatePushDTO implements Serializable {
    private String did;

    private String utime;

    private List<PublishDTO> content;
}
