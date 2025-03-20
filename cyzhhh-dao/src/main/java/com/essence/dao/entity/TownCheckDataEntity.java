package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 乡镇考核 断面
 */
@Data
@TableName("t_town_check_data")
public class TownCheckDataEntity implements Serializable {
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 断面id
     */
    private String selectionId;
    /**
     * 年月
     */
    private String time;
    /**
     * 0 无水 1 达标 2 不达标
     */
    private Integer status;

    //规划目标
    String target ;
    //水质类别
    private String waterType;

    //污染物
    private String pollute ;
    //平均水质类别
    private String avgType ;
    //平均水质 达标情况
    private String avgStatus ;
}
