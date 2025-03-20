package com.essence.interfaces.dot;

import lombok.Data;

import java.util.List;

/**
 * @Author BINX
 * @Description TODO
 * @Date 2023/5/5 9:51
 */
@Data
public class OverFlowListDto {

    /**
     * 溢流数量
     */
    int total;

    /**
     * 溢流列表
     */
    List<OverFlowDto> list;

}
