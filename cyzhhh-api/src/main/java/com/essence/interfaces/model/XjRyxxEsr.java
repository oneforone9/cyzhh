package com.essence.interfaces.model;


import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 设备巡检人员信息返回实体
 *
 * @author majunjie
 * @since 2025-01-08 08:14:23
 */

@Data
public class XjRyxxEsr extends Esr {

    private static final long serialVersionUID = 398129954549495145L;

        
    /**
     * 主键
     */
        private String id;
    
    
    /**
     * 姓名
     */
        private String name;
    
    
    /**
     * 联系方式
     */
        private String lxfs;
    
    
    /**
     * 所在班组
     */
        private String bzmc;
    
    
    /**
     * 所在班组id
     */
        private String bzId;
    
    
    /**
     * 生成时间
     */
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;
    
    
    /**
     * 状态0启用1停用
     */
        private Integer type;

    /**
     * 类型0成员1班组长
     */        private Integer lx;
    /**
     * 部门id
     */        private String bmid;
}
