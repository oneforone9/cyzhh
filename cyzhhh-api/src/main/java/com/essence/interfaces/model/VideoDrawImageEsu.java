package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 视频管理线保护线图表更新实体
 *
 * @author zhy
 * @since 2023-02-03 17:10:55
 */

@Data
public class VideoDrawImageEsu extends Esu {

    private static final long serialVersionUID = 799227361793544523L;

                        private Integer id;
        
            /**
     * 视频编码code
     */            private String videoCode;
        
            /**
     * 影像地址
     */            private String imageLineUrl;
        
            /**
     * 备注
     */            private String remark;
        
            /**
     * 新增时间
     */            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date gmtCreate;
        
            /**
     * 创建者
     */            private String creator;
        


}
