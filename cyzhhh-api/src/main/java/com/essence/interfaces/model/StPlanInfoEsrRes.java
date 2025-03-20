package com.essence.interfaces.model;

import com.essence.dao.entity.StPlanInfoDto;
import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;


/**
 * 闸坝计划排班信息表返回实体
 *
 * @author liwy
 * @since 2023-07-13 14:46:18
 */

@Data
public class StPlanInfoEsrRes extends Esr {

    private static final long serialVersionUID = -83559748789308836L;

    /**
     * 边闸表的主键
     */
    private Integer sideGateId;


    /**
     * 测站名称：测站编码所代表测站的中文名称。
     */
    private String stnm;


    /**
     * 河道名称
     */
    private String riverName;


    /**
     * 公司名称
     */
    private String company;



    /**
     * 负责人
     */
    private String name;


    /**
     * 负责人手机号
     */
    private String phone;

    /**
     * 设施设备名称等
     */
    private List<StPlanInfoDto> stPlanInfoEsrlist;





}
