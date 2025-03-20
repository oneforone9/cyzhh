package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * 视频管理线保护线表返回实体
 *
 * @author zhy
 * @since 2023-02-03 14:51:36
 */

@Data
public class VideoDrawLineEsr extends Esr {

    private static final long serialVersionUID = -70136047554961174L;

                        private Integer id;
    
        
    /**
     * 视频编码code
     */
                private String videoCode;
    
        
    /**
     * 管理范围保护范围画线数据
     */
                private String geom;
    
        
    /**
     * 备注
     */
                private String remark;
    
        
    /**
     * 新增时间
     */
            
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date gmtCreate;
    
        
    /**
     * 创建者
     */
                private String creator;
    


}
