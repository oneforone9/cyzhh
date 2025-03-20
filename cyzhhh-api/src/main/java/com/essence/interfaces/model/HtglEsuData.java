package com.essence.interfaces.model;

import lombok.Data;

import java.util.List;

/**
 * 合同管理返回实体
 *
 * @author majunjie
 * @since 2024-09-09 17:48:33
 */

@Data
public class HtglEsuData  {

    /**
     * 合同内容
     */
    private HtglEsu htglEsu;
    /**
     * 合同附件
     */
    private List<HtglfjEsu> fjData;
    /**
     * 合同预审项目
     */
    private List<HtglysxmEsu> ysxmData;



}
