package com.essence.interfaces.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 水位站和流量站数据
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-10-14 14:56:10
 */
@Data
public class StWaterRateRelation  {

	/**
	 * 采集数据值
	 */
	private String addrv;
	/**
	 * 采集时间
	 */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date ctime;

	/**
	 * 瞬时河道水深
	 */
	private String momentRiverPosition;

	/**
	 * 瞬时流量
	 */
	private String momentRate;
}
