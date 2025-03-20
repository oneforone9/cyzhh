package com.essence.dao.entity;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author BINX
 * @Description TODO
 * @Date 2023/5/6 14:37
 */
@Data
public class WaterSupplyCaseImportDto {

    /**
     * 案件ID
     */
    private Long id;
    /**
     * 补水口名称
     */
    private String waterPortName;

    /**
     * 时间序列流量Excel
     */
    private MultipartFile file;
}
