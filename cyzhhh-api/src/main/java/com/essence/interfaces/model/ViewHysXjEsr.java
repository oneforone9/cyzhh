package com.essence.interfaces.model;


import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 返回实体
 *
 * @author majunjie
 * @since 2025-01-09 10:22:29
 */

@Data
public class ViewHysXjEsr extends Esr {

    private static final long serialVersionUID = -49194069361759795L;

        
    /**
     * 主键
     */
        private String id;
    
    
    /**
     * 会议室名称
     */
        private String mc;
    
    
    /**
     * 生成时间
     */
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;
    
    
    /**
     * 主键
     */
        private String jhid;
    
    
    /**
     * 巡检内容
     */
        private String xjnr;
    
    
    /**
     * 负责人
     */
        private String fzr;
    
    
    /**
     * 负责人id
     */
        private String fzrId;
    
    
    /**
     * 巡检日期巡检日期(1,2,3,4,5,6,7)
     */
        private String xjRq;
    
    
    /**
     * 巡检单位id
     */
        private String xjdwId;
    
    
    /**
     * 班组id
     */
        private String bzId;

    /**
     * 单位名称
     */
    private String dwmc;

    /**
     * 班组名称
     */
    private String bzmc;
    /**
     * 经度
     */
    private Double lgtd;


    /**
     * 纬度
     */
    private Double lttd;
}
