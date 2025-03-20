package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 设备巡检人员信息参数实体
 *
 * @author majunjie
 * @since 2025-01-08 08:14:23
 */

@Data
public class XjRyxxEsp extends Esp {

    private static final long serialVersionUID = 301716442169471964L;

        /**
     * 主键
     */        @ColumnMean("id")
    private String id;
    /**
     * 姓名
     */        @ColumnMean("name")
    private String name;
    /**
     * 联系方式
     */        @ColumnMean("lxfs")
    private String lxfs;
    /**
     * 所在班组
     */        @ColumnMean("bzmc")
    private String bzmc;
    /**
     * 所在班组id
     */        @ColumnMean("bz_id")
    private String bzId;
    /**
     * 生成时间
     */        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ColumnMean("time")
    private Date time;
    /**
     * 状态0启用1停用
     */        @ColumnMean("type")
    private Integer type;

    /**
     * 类型0成员1班组长
     */
    @ColumnMean("lx")
    private Integer lx;
    /**
     * 部门id
     */
    @ColumnMean("bmid")
    private String bmid;
}
