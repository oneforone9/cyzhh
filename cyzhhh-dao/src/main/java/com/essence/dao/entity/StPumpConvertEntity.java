package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-10-17 17:07:04
 */
@Data
@TableName("st_sn_convert")
public class StPumpConvertEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(value = "id", type = IdType.UUID)
	private String id;
	/**
	 * 
	 */
	private String sn;
	/**
	 * 
	 */
	private String stcd;

}
