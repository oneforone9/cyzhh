package com.essence.dao.entity.henrunan;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 定时数据表实体
 *
 * @author BINX
 * @since 2023-08-02 14:03:49
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "t_bll_element")
public class TBllElementDto extends Model<TBllElementDto> {

    private static final long serialVersionUID = 670084179735788151L;

    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 测站地址
     */
    @TableField("StationAddr")
    private String stationaddr;

    /**
     * 报文ID
     */
    @TableField("orgRecordId")
    private Integer orgrecordid;

    /**
     * 入库时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("RecvTime")
    private Date recvtime;

    /**
     * 发送时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("ReportTime")
    private Date reporttime;

    /**
     * 采集时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("DataTime")
    private Date datatime;

    /**
     * 功能码
     */
    @TableField("DataType")
    private String datatype;

    /**
     * 要素码   瞬时流量	0X27     瞬时河道水位	0X39
     */
    @TableField("ElementType")
    private String elementtype;

    /**
     * 要素数据
     */
    @TableField("DataContent")
    private String datacontent;

    @TableField("channel")
    private String channel;

}
