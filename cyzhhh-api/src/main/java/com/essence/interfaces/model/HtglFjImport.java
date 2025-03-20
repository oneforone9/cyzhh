package com.essence.interfaces.model;

import com.essence.interfaces.entity.Esr;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 合同管理返回实体
 *
 * @author majunjie
 * @since 2024-09-09 17:48:33
 */

@Data
public class HtglFjImport  {


    /**
     * 文件
     */
    private String id;


    /**
     * 文件名称
     */
    private String wjmc;
}
