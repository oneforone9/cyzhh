package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 水位站和流量站数据
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-10-14 14:56:10
 */
@Data
@TableName("st_water_rate")
public class StWaterRateEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId(value = "id", type = IdType.UUID)
	private String id;
	/**
	 * PLCID
	 */
	private String pid;
	/**
	 * “0” 为模拟量  “1” 为遥信号
	 */
	private String type;
	/**
	 * 采集名称
	 */
	private String addr;
	/**
	 * 采集数据值
	 */
	private String addrv;
	/**
	 * 采集时间
	 */
	private String ctime;
	/**
	 * SN码 需要转换一下
	 */
	private String did;
	/**
	 * 瞬时流量
	 */
	private String momentRate;
	/**
	 * 当前瞬时流量
	 */
	private String preMomentRate;
	/**
	 * 瞬时河道水深
	 */
	private String momentRiverPosition;
	/**
	 * 电压
	 */
	private String voltage;

	/**
	 * 纬度：测站代表点所在地理位置的东经度，单位为度，保留6位小数。
	 */
	@TableField(exist = false)
	private BigDecimal wd;
	/**
	 * 经度：测站代表点所在地理位置的北纬度，单位为度，保留6位小数。
	 */
	@TableField(exist = false)
	private BigDecimal jd;

}
