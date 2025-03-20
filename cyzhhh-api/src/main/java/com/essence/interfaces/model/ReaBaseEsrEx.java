package com.essence.interfaces.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 河道信息表返回实体
 *
 * @author zhy
 * @since 2022-10-18 17:22:23
 */

@Data
public class ReaBaseEsrEx extends ReaBaseEsr {

    private static final long serialVersionUID = -2857238220727312648L;

    private List<ReaBaseEsrEx> children = new ArrayList<>();
}
