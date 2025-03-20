package com.essence.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 实体
 *
 * @author BINX
 * @since 2023-02-21 16:34:23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "st_ecology_water")
public class StEcologyWaterDto extends Model<StEcologyWaterDto> {

    private static final long serialVersionUID = -52471576157379173L;
        
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;
        
    /**
     * 河道名称
     */
    @TableField("river_name")
    private String riverName;
    
    /**
     * 初始水量
     */
    @TableField("start_count")
    private Double startCount;
    
    /**
     * 工况1(6、7、8月)
     */
    @TableField("count_one")
    private Double countOne;
    
    /**
     * 工况2(3、4、10、11月)
     */
    @TableField("count_two")
    private Double countTwo;
    
    /**
     * 工况3((5、9月)
     */
    @TableField("count_three")
    private Double countThree;
    
    /**
     * 地区（1-北部，2-中部，3-南部）
     */
    @TableField("area")
    private String area;
    
    /**
     * 创建者
     */
    @TableField("creator")
    private String creator;
    
    /**
     * 服务类型或管护河段名称
     */
    @TableField("data_name")
    private String dataName;

}
