package com.essence.common.utils;

/**
 * @author zhy
 * @since 2022/1/4 16:46
 */
public class FileUtils {

    /**
     * @param size 单位 b
     * @return
     */
    public static String fileSize(Long size) {
        Long kb = size / 1024;
        if (0 == kb) {
            return size + "B";
        }
        Long mb = kb / 1024;
        if (0 == mb) {
            return kb + "KB";
        }
        return mb + "MB";
    }

}
