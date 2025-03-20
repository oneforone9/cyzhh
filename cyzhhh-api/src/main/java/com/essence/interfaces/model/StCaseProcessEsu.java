package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 方案执行过程表-存放入参等信息更新实体
 *
 * @author zhy
 * @since 2023-04-17 16:30:06
 */

@Data
public class StCaseProcessEsu extends Esu {

    private static final long serialVersionUID = -32476326407112004L;

                /**
     * 主键
     */            private String id;
        
            /**
     * 方案id
     */            private String caseId;
        
            /**
     * 入参站点类型
     */            private String type;
        
            /**
     * 入参站点id
     */            private String stcd;
        
            /**
     * 入参站点名称
     */            private String stName;
        
            /**
     * 入参数的时间点位数据 
     */            private String inputData;
        
            /**
     * 新增时间
     */            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date createTime;
        


}
