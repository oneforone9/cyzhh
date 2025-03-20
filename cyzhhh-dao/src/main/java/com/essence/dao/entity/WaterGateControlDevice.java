package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
@Data
@TableName("st_gate_control_device_plc")
public class WaterGateControlDevice implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 数据回传的 id = sn 码
     */
    private String did;
    /**
     * plc 对应的硬件码
     */
    private String pid;
}
