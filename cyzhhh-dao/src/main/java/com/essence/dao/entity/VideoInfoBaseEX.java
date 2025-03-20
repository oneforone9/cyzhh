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
 * 视频基础信息表(VideoInfoBase)实体类
 *
 * @author zhy
 * @since 2022-10-20 14:20:42
 */

@Data
public class VideoInfoBaseEX extends VideoInfoBase {

    private static final long serialVersionUID = -6042739213275654940L;

    private String river_name;
}
