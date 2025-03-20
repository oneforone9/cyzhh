package com.essence.dao.entity.caiyun;

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

import java.io.Serializable;
import java.util.Date;

/**
 * 彩云预报实时数据
 *
 * @authorBINX
 * @since 2023年5月4日 下午3:47:15
 */
@Data
@Accessors(chain = true)
public class StCaiyunPrecipitationRealTableDto implements Serializable {

    private static final long serialVersionUID = 452785220583272733L;

    /**
     * id
     */
    private String id;

    /**
     * 网格编号
     */
    private String meshId;

    /**
     * 雨量值（mm）
     */
    private String drp;

    /**
     * 雨量时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date drpTime;

    /**
     * 写入时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 站点编号
     */
    private String stcd;

    /**
     * 站点名称
     */
    private String stnm;

    private String drp1;
    private String drp3;
    private String drp6;
    private String drp12;
    private String drp24;

    public static StCaiyunPrecipitationRealTableDto build(StCaiyunPrecipitationRealDto stCaiyunPrecipitationRealDto) {
        StCaiyunPrecipitationRealTableDto stCaiyunPrecipitationRealTableDto = new StCaiyunPrecipitationRealTableDto();
        stCaiyunPrecipitationRealTableDto.setId(stCaiyunPrecipitationRealDto.getId());
        stCaiyunPrecipitationRealTableDto.setMeshId(stCaiyunPrecipitationRealDto.getMeshId());
        stCaiyunPrecipitationRealTableDto.setDrp(stCaiyunPrecipitationRealDto.getDrp());
        stCaiyunPrecipitationRealTableDto.setDrpTime(stCaiyunPrecipitationRealDto.getDrpTime());
        stCaiyunPrecipitationRealTableDto.setCreateTime(stCaiyunPrecipitationRealDto.getCreateTime());
        stCaiyunPrecipitationRealTableDto.setStcd(stCaiyunPrecipitationRealDto.getStcd());
        stCaiyunPrecipitationRealTableDto.setStnm(stCaiyunPrecipitationRealDto.getStnm());
        return stCaiyunPrecipitationRealTableDto;
    }

}
