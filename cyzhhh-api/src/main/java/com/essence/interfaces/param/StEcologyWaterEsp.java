package com.essence.interfaces.param;

import com.essence.interfaces.annotation.ColumnMean;
import com.essence.interfaces.entity.Esp;
import lombok.Data;

/**
 * 参数实体
 *
 * @author zhy
 * @since 2023-02-21 16:34:38
 */

@Data
public class StEcologyWaterEsp extends Esp {

    private static final long serialVersionUID = 752723172675829537L;

                    @ColumnMean("id")
    private Integer id;
        /**
     * 河道名称
     */            @ColumnMean("river_name")
    private String riverName;
        /**
     * 初始水量
     */            @ColumnMean("start_count")
    private Double startCount;
        /**
     * 工况1(6、7、8月)
     */            @ColumnMean("count_one")
    private Double countOne;
        /**
     * 工况2(3、4、10、11月)
     */            @ColumnMean("count_two")
    private Double countTwo;
        /**
     * 工况3((5、9月)
     */            @ColumnMean("count_three")
    private Double countThree;
        /**
     * 地区（1-北部，2-中部，3-南部）
     */            @ColumnMean("area")
    private String area;
        /**
     * 创建者
     */            @ColumnMean("creator")
    private String creator;
        /**
     * 服务类型或管护河段名称
     */            @ColumnMean("data_name")
    private String dataName;


}
