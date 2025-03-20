package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import lombok.Data;

/**
 * 更新实体
 *
 * @author zhy
 * @since 2023-03-16 16:41:56
 */

@Data
public class CyWeatherForecastEsu extends Esu {

    private static final long serialVersionUID = -90562334119639865L;

                        private String id;
        
            /**
     * 气象发布部门
     */            private String publishDepartment;
        
            /**
     * 发布时间
     */            private String publishTime;
        
            /**
     * 气象类型
     */            private String weatherType;
        
            /**
     * 气象等级
     */            private String weatherLevel;
        
            /**
     * 状态
     */            private String status;
        
            /**
     * 内容
     */            private String context;
        
            /**
     * 防护措施
     */            private String defence;
        
            /**
     * 消息
     */            private String msg;
        
            /**
     * 图标
     */            private String icon;
        
            /**
     * 预警类型
     */            private String nowWaring;
        


}
