package com.essence.common.utils;

import cn.hutool.core.date.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtil {
    /**
     * 统计两个时间的时间差
     * two-one
     * 相差几秒几毫秒
     */
    public static String getDistanceDateTime(Date one, Date two) {
        long day = 0;//天数差
        long hour = 0;//小时数差
        long min = 0;//分钟数差
        long second=0;//秒数差
        long diff=0 ;//毫秒差
        String result = "";
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        c.setTime(one);
        long time1 = one.getTime();
        long time2 = two.getTime();
        diff = time2 - time1;
        day = diff / (24 * 60 * 60 * 1000);
        hour = (diff / (60 * 60 * 1000) - day * 24);
        min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        second = diff/1000;
        System.out.println("day="+day+" hour="+hour+" min="+min+" ss="+second%60+" SSS="+diff%1000);
        String daystr = day%30+"天";
        String hourStr = hour%24+"小时";
        String minStr = min%60+"分";
        String secondStr = second%60+"秒";
        if (day!=0){
            result = result + daystr;
        }
        if (hour!=0){
            result = result + hourStr;
        }
//        if (min!=0){
//            result = result + minStr;
//        }
//        if (second!=0){
//            result = result + secondStr;
//        }
        return result;
    }

    /**
     * 获取当前时间是周几
     * @param date
     * @return
     */
    public static String  week(Date date) {
        String[] weeks = {"7","1","2","3","4","5","6"};

        Calendar cal = Calendar.getInstance();

        cal.setTime(date);

        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;

        if(week_index<0){

            week_index = 0;

        }

        return weeks[week_index];
    }

    /**
     * 获得当前时间的年月日 yyyy-MM
     *
     * @param date
     * @return
     */
    public static String getYearAndMonth(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        return sdf.format(date);
    }
    /**y
     * 获得当前时间的年月日 yyy
     *
     * @param date
     * @return
     */
    public static String getYear(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return sdf.format(date);
    }

    /**y
     * 获得当前时间的月份 MM
     *
     * @param date
     * @return
     */
    public static String getMouth(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        return sdf.format(date);
    }
    /**y
     * 获得当前时间的月日 MMdd
     *
     * @param date
     * @return
     */
    public static String getMouthDay(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMdd");
        return sdf.format(date);
    }

    /**
     * 获取时间的前n天/后n天的时间
     * @param date
     * @param num
     * @return
     */
    public static String getPreDayDate(Date date,Integer num) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, num);
        return sdf.format(c.getTime());
    }


    /**
     * 获取时间的前n小时/后n小时的时间
     * @param date
     * @param num
     * @return
     */
    public static String getPreDayDate002(Date date,Integer num) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR_OF_DAY, num);
        return sdf.format(c.getTime());
    }

    /**
     * 获取时间的前n天/后n天的时间
     * @param date
     * @return
     */
    public static String getPreDayDateMouth(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, 0);
        return sdf.format(c.getTime());
    }


    /**
     * 获取年的开始时间
     * @param date
     * @return
     */
    public static Date getYearStartDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_YEAR, 1);
        // 将小时至0
        c.set(Calendar.HOUR_OF_DAY, 0);
        //将分钟至0
        c.set(Calendar.MINUTE, 0);
        //将秒至0
        c.set(Calendar.SECOND,0);
        //将毫秒至0
        c.set(Calendar.MILLISECOND, 0);
        // 获取本月第一天的时间戳
        return c.getTime();

    }

    /**
     * 获取年的结束时间
     * @param date
     * @return
     */
    public static Date getYearEndDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_YEAR, c.getActualMaximum(Calendar.DAY_OF_YEAR));
        //将小时至23
        c.set(Calendar.HOUR_OF_DAY, 23);
        //将分钟至59
        c.set(Calendar.MINUTE, 59);
        //将秒至59
        c.set(Calendar.SECOND,59);
        //将毫秒至999
        c.set(Calendar.MILLISECOND, 0);
        // 获取本月最后一天的时间戳
        return c.getTime();
    }


    /**
     * 将日期类型转换为日期字符串,无格式，默认：yyyy-MM-dd HH:mm:ss
     *
     * @param date
     *            Date类型
     * @return String类型，日期字符串
     */
    public static String dateToStringNormal(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(date);
    }

    public static Date getYearFirst(String year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, Integer.valueOf(year));
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }




}
