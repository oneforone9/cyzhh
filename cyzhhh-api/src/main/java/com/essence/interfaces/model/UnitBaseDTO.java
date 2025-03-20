package com.essence.interfaces.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 河道管理所
 */
@Data
public class UnitBaseDTO implements Serializable {

    /**
     * 主键 河道管理所
     */
    private String id;

    /**
     * 名称 河道管理所
     */
    private String unitName;
    /**
     * 河道信息
     */
    private List<ReaBaseDTO> reaBaseDTOList;

}
