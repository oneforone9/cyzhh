package com.essence.interfaces.model;

import com.essence.interfaces.vaild.Update;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 事件基础信息表更新实体
 *
 * @author zhy
 * @since 2022-10-30 18:06:22
 */

@Data
public class EventBasePictures  {
    /**
     * 事件id主键
     */
    @NotEmpty(groups = Update.class, message = "事件id不能为空")
    private String id;

    /**
     * 类型1问题图片2反馈图片
     */
    @NotEmpty(groups = Update.class, message = "类型不能为空")
    private String type;

    /**
     * 文件id主键多个,号分割
     */
    @NotEmpty(groups = Update.class, message = "文件id不能为空")
    private List<String> file;
}
