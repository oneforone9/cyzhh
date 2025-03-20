package video.entity;

import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 水位站和流量站数据
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-10-13 19:46:43
 */
@Data
@TableName("st_water_rate")
public class StWaterRateEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.UUID)
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
}
