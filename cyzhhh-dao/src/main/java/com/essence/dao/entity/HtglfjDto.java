package com.essence.dao.entity;

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
 * 合同管理附件实体
 *
 * @author majunjie
 * @since 2024-09-10 14:02:24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "htglfj")
public class HtglfjDto extends Model<HtglfjDto> {

    private static final long serialVersionUID = 109405692460874403L;
    
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 合同id
     */
    @TableField("htid")
    private String htid;

    /**
     * 来源 0：合同电子版； 1: 合同扫描件 ； 2: 会签单扫描件； 3: 预审单扫描件； 4:授权扫描件 5:合同最终版 6:授权委托书最终版 7:律师审查附件
     */
    @TableField("lx")
    private Integer lx;

    /**
     * 文件id集合
     */
    @TableField("fileid")
    private String fileid;

    /**
     * 上传人
     */
    @TableField("scr")
    private String scr;

    /**
     * 提交时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("tjsj")
    private Date tjsj;

}
