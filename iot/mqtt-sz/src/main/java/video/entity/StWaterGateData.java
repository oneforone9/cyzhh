package video.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 水闸实时数据
 * 
 * @author cuirx
 * @email
 * @date 2023-03-10 19:46:43
 */
@Data
@TableName("st_water_gate")
public class StWaterGateData implements Serializable {
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
