package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
@Data
@TableName("st_vx_info")
public class StVxInfoEntity implements Serializable {
    @TableId(type = IdType.UUID)
    private String id;

    private String openid;

    private String phone;
}
