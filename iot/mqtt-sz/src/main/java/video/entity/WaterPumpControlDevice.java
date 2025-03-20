package video.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("st_pump_control_device_plc")
public class WaterPumpControlDevice implements Serializable {
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
