package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 每天雨量数据
 *
 * @author BINX
 * @since 2023-02-20 14:33:09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_rain_daily")
public class StRainDailyDto extends Model<StRainDailyDto> {

    private static final long serialVersionUID = 738651204268981714L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    /**
     * 时间
     */

    @TableField("date")
    private String date;

    /**
     * 雨量站id
     */
    @TableField("station_id")
    private String stationId;

    /**
     * 小时雨量
     */
    @TableField("value")
    private String value;

}
