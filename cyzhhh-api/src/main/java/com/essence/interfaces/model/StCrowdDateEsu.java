package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import lombok.Data;

/**
 * 清水的河 - 用水人数量更新实体
 *
 * @author zhy
 * @since 2023-01-12 17:36:46
 */

@Data
public class StCrowdDateEsu extends Esu {

    private static final long serialVersionUID = 403289411338180180L;

                /**
     * 主见
     */            private String id;
        
            /**
     * 热点区域
     */            private String area;
        
            /**
     * 河流名称
     */            private String rvnm;
        
            /**
     * 水管单位
     */            private String waterUnit;
        
            /**
     * 人数
     */            private Integer num;
        
            /**
     * 时间 yyyy-mm-dd
     */            private String date;
        


}
