package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 绿化保洁工作日志上报表-关联表更新实体
 *
 * @author liwy
 * @since 2023-03-17 17:20:11
 */

@Data
public class StGreenReportRelationEsu extends Esu {

    private static final long serialVersionUID = -48515403471238051L;

    /**
     * 主键
     */
    private String id;

    /**
     * 绿化保洁工作日志上报表st_green_report的id
     */
    private String stGreenId;

    /**
     * 作业内容标记
     */
    private String workFlag;

    /**
     * 作业内容描述
     */
    private String workContent;

    /**
     * 解决措施
     */
    private String handleWay;

    /**
     * 新增时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;

    /**
     * 备注
     */
    private String remark;


    /**
     * 关联的文件id
     *
     * @mock [1111, 222, 333]
     */
    private List<String> fileIds;


}
