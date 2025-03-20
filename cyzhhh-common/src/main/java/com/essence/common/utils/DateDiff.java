package com.essence.common.utils;
 
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author tqf
 * @Description
 * @Version 1.0
 * @since 2022-04-18 11:30
 */
public class DateDiff {
 
    /**
     * 计算2个时间相差的天数、小时、分钟、秒
     * @param startTime 开始时间
     * @param endTime 截止时间
     * @param format 时间格式 yyyy-MM-dd HH:mm:ss
     * @param str 返回的数据为：day-天、hour-小时、min-分钟、second-秒
     * @return
     */
    public static Long dateDiff(String startTime, String endTime,  String format, String str) {
        // 按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat(format);
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long ns = 1000;// 一秒钟的毫秒数
        long diff;
        long day = 0;
        long hour = 0;
        long min = 0;
        long second = 0;
        // 获得两个时间的毫秒时间差异
        try {
            diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
            // 计算差多少天
            day = diff / nd;
            // 计算差多少小时
            hour = diff / nh ;
            // 计算差多少分钟
            min = diff / nm ;
            // 计算差多少秒
            second = diff / ns;
            // 输出结果
       /*     System.out.println("时间相差：" + day + "天" +
                      (hour - day * 24) + "小时"
                    + (min - day * 24 * 60) + "分钟" +
                    second + "秒。");*/
            /*System.out.println("hour=" + hour + ",min=" + min);*/
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (str.equalsIgnoreCase("day")) {
            return day;
        } else if(str.equalsIgnoreCase("hour")) {
            return hour;
        } else if(str.equalsIgnoreCase("min")) {
            return min;
        } else {
            return second;
        }
    }
 
    public static void main(String[] args) {
        System.out.println("相差多少天：" + dateDiff(
                "2022-04-18 11:31:25",
                "2022-04-19 12:31:25",
                "yyyy-MM-dd HH:mm:ss",
                "day"));
        System.out.println("相差多少小时：" + dateDiff(
                "2022-04-18 11:31:25",
                "2022-04-19 12:31:25",
                "yyyy-MM-dd HH:mm:ss",
                "hour"));
        System.out.println("相差多少分钟：" + dateDiff(
                "2022-04-18 11:31:25",
                "2022-04-18 12:29:26",
                "yyyy-MM-dd HH:mm:ss",
                "min"));
        System.out.println("相差多少秒：" + dateDiff(
                "2022-04-18 11:31:25",
                "2022-04-18 12:31:25",
                "yyyy-MM-dd HH:mm:ss",
                "sec"));
    }
    /**
     * 时间向前取整
     * @param date 时间
     * @param step 步长
     */
    public static Date roundingTime(Date date, int step) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        int minute = calendar.get(Calendar.MINUTE);

        calendar.set(Calendar.MINUTE, minute / step * step);
        return calendar.getTime();
    }
}