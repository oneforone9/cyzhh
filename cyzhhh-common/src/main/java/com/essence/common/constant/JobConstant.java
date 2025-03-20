package com.essence.common.constant;

/**
 * 定时任务变量
 * @author huangyong
 * Create at 2024/7/25
 */
public class JobConstant {

    /**
     * 生产环境 D:\backend
     */
    public final static String BACK_END = "backend";

    /**
     * 生产环境 D:\backend\backjob
     */
    public final static String BACK_JOB = "backjob";

    /**
     * java -jar 启动参数 --cronjob.identify=backend
     */
    public final static String IDENTIFY = "cronjob.identify";

    /**
     * 错误提示
     */
    public final static String jobErrorTip =
            "\n***********************************************************************\n"+
            "\n***********************************************************************\n"+
            "\n**************生产环境启动jar包必须指定cronjob.identifier的值**************\n"+
            "\n***********************************************************************\n"+
            "\n***********************************************************************\n";

    /**
     * 错误提示
     */
    public final static String jobErrorTip2 =
            "\n***********************************************************************\n"+
            "\n***********************************************************************\n"+
            "\n***********cronjob.identifier的值只能为'backend'或者'backjob'************\n"+
            "\n***********************************************************************\n"+
            "\n***********************************************************************\n";
}
