package com.essence.interfaces.dot;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
public class CrowDataDto implements Serializable {
    /**
     * 重点区域
     */
    private List<String> area;

    private List<String> date;
    /**
     * 日期下的数据
     */
    private List<List<Integer>> dateList;
}
