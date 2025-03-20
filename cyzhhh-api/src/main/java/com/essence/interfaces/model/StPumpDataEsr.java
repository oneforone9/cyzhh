package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 返回实体
 *
 * @author zhy
 * @since 2023-04-14 11:36:10
 */

@Data
public class StPumpDataEsr extends Esr {

    private static final long serialVersionUID = 774240548585410512L;

            
    /**
     * 主键
     */
                private String id;
    
        
    /**
     * 设备物理地址
     */
                private String deviceAddr;
    
        
    /**
     * 泵站1 反馈
     */
                private String p1FeedBack;
    
        
    /**
     * 泵站2 反馈
     */
                private String p2FeedBack;
    
        
    /**
     * 流量
     */
                private String flowRate;
    
        
    /**
     * 压力
     */
                private String pressure;
    
        
    /**
     * 液位
     */
                private String yPosition;
    
        
    /**
     * 泵站1 计时
     */
                private String p1CountTime;
    
        
    /**
     * 泵2 计时
     */
                private String p2CountTime;
    
        
    /**
     * A相电压
     */
                private String aVoltage;
    
        
    /**
     * B相电压
     */
                private String bVoltage;
    
        
    /**
     *  C相电压
     */
                private String cVoltage;
    
        
    /**
     * 电流A
     */
                private String aElectric;
    
        
    /**
     * 电流B
     */
                private String bElectric;
    
        
    /**
     * 电流C
     */
                private String cElectric;
    
        
    /**
     * 电能
     */
                private String electric;
    
        
    /**
     * 累计流量
     */
                private String addFlowRate;
    
        
    /**
     * 液位 高
     */
                private String liquidHigh;
    
        
    /**
     * 液位 低
     */
                private String liquidLow;
    
        
    /**
     * 泵1 远程
     */
                private String p1Remote;
    
        
    /**
     * 泵1 运行
     */
                private String p1Run;
    
        
    /**
     * 泵1 故障
     */
                private String p1Hitch;
    
        
    /**
     * 泵2 远程
     */
                private String p2Remote;
    
        
    /**
     * 泵2 运行
     */
                private String p2Run;
    
        
    /**
     * 泵2 故障
     */
                private String p2Hitch;
    
                
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date date;
    


}
