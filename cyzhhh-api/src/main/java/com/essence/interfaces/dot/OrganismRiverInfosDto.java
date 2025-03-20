package com.essence.interfaces.dot;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrganismRiverInfosDto implements Serializable {
    /**
     * 河长
     */
    private List<OrganismRiverRecordDto> riverLong;
    /**
     * 河面积
     */
    private List<OrganismRiverRecordDto> riverWide;
}
