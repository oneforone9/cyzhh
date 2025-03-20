package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 绿化保洁工作日志上报表-关联表返回实体
 *
 * @author liwy
 * @since 2023-03-17 17:20:25
 */

@Data
public class StGreenReportRelationEsr extends Esr {

    private static final long serialVersionUID = 653081603438984859L;


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
     * 上报的图片文件集合
     */
    private List<FileBaseEsr> reportFileList;


}
