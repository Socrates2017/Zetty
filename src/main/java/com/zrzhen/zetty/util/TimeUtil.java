package com.zrzhen.zetty.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author chenanlian
 */
public class TimeUtil {

    private static final Logger log = LoggerFactory.getLogger(TimeUtil.class);


    /**
     * 获取距离当前时间若干天的时间
     * 当参数为负数时，为过去时间
     * 当参数为正数时，为未来时间
     *
     * @param day
     * @return
     */
    public static Date getTimeByDayDistance(Integer day) {
        return getTimeByDistance(null, null, day, null, null, null, null);
    }

    /**
     * 获取距离当前时间若干天的时间
     * 当参数为负数时，为过去时间
     * 当参数为正数时，为未来时间
     *
     * @param day
     * @return
     */
    public static Calendar getCalendarByDayDistance(String date, Integer day) {
        return getCalendarByDistance(date, null, null, day, null, null, null, null);
    }


    /**
     * 获取距离当前时间若干天的时间
     * 当参数为负数时，为过去时间
     * 当参数为正数时，为未来时间
     *
     * @param day
     * @return
     */
    public static Calendar getCalendarByDayDistance(Integer day) {
        return getCalendarByDistance(null, null, day, null, null, null, null);
    }

    /**
     * 获取距离当前时间若干小时的时间
     * 当参数为负数时，为过去时间
     * 当参数为正数时，为未来时间
     *
     * @param hour
     * @return
     */
    public static Date getTimeByHourDistance(Integer hour) {
        return getTimeByDistance(null, null, null, hour, null, null, null);
    }


    /**
     * 获取距离当前时间若干距离的时间
     * 参数为null或0时与当前一样
     *
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @param second
     * @param millisecond
     * @return
     */
    public static Date getTimeByDistance(Integer year, Integer month, Integer day, Integer hour, Integer minute, Integer second, Integer millisecond) {
        return getCalendarByDistance(year, month, day, hour, minute, second, millisecond).getTime();
    }

    /**
     * 获取距离当前日历若干距离的时间
     * 参数为null或0时与当前一样
     *
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @param second
     * @param millisecond
     * @return
     */
    public static Calendar getCalendarByDistance(Integer year, Integer month, Integer day, Integer hour, Integer minute, Integer second, Integer millisecond) {
        Calendar now = Calendar.getInstance();
        now.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        if (null != year) {
            now.add(Calendar.YEAR, year);
        }
        if (null != month) {
            now.add(Calendar.MONTH, month);
        }
        if (null != day) {
            now.add(Calendar.DATE, day);
        }
        if (null != hour) {
            now.add(Calendar.HOUR, hour);
        }
        if (null != minute) {
            now.add(Calendar.MINUTE, minute);
        }
        if (null != second) {
            now.add(Calendar.SECOND, second);
        }
        if (null != millisecond) {
            now.add(Calendar.MILLISECOND, millisecond);
        }
        return now;
    }

    public static Calendar getCalendarByDistance(String date, Integer year, Integer month, Integer day, Integer hour, Integer minute, Integer second, Integer millisecond) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        if (StringUtils.isNotBlank(date)) {
            String[] dateS = date.split("-");
            if (dateS != null && dateS.length == 3) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    cal.setTime(sdf.parse(date));
                } catch (ParseException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }

        if (null != year) {
            cal.add(Calendar.YEAR, year);
        }
        if (null != month) {
            cal.add(Calendar.MONTH, month);
        }
        if (null != day) {
            cal.add(Calendar.DATE, day);
        }
        if (null != hour) {
            cal.add(Calendar.HOUR, hour);
        }
        if (null != minute) {
            cal.add(Calendar.MINUTE, minute);
        }
        if (null != second) {
            cal.add(Calendar.SECOND, second);
        }
        if (null != millisecond) {
            cal.add(Calendar.MILLISECOND, millisecond);
        }
        return cal;
    }

    /**
     * 返回当前日历
     *
     * @return
     */
    public static Calendar calendar() {
        Calendar now = Calendar.getInstance();
        now.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        return now;
    }

    /**
     * 返回当前日期
     *
     * @return
     */
    public static Date now() {
        Calendar now = Calendar.getInstance();
        now.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        return now.getTime();
    }

    /**
     * 获取当前年份
     *
     * @return
     */
    public static Integer nowYear() {
        Calendar now = Calendar.getInstance();
        now.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        int year = now.get(Calendar.YEAR);
        return year;
    }

    /**
     * 获取当前月份
     *
     * @return
     */
    public static Integer nowMonth() {
        Calendar now = Calendar.getInstance();
        now.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        int month = now.get(Calendar.MONTH) + 1;
        return month;
    }

    /**
     * 获取当前月第几日
     *
     * @return
     */
    public static Integer nowDayOfMonth() {
        Calendar now = Calendar.getInstance();
        now.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        int dom = now.get(Calendar.DAY_OF_MONTH);
        return dom;
    }


    public static Integer nowDayOfWeek() {
        Calendar now = Calendar.getInstance();
        now.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        int dom = now.get(Calendar.DAY_OF_WEEK);
        return dom;
    }

    /**
     * 获取当前周第几日
     *
     * @return
     */
    public static Integer nowDayOfYear() {
        Calendar now = Calendar.getInstance();
        now.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        int dow = now.get(Calendar.DAY_OF_YEAR);
        return dow;
    }

    /**
     * 获取当前小时
     *
     * @return
     */
    public static Integer nowHour() {
        Calendar now = Calendar.getInstance();
        now.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        int hour = now.get(Calendar.HOUR_OF_DAY);
        return hour;
    }

    /**
     * 当前分钟
     *
     * @return
     */
    public static Integer nowMinure() {
        Calendar now = Calendar.getInstance();
        now.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        int minute = now.get(Calendar.MINUTE);
        return minute;
    }

    /**
     * 当前秒钟
     *
     * @return
     */
    public static Integer nowSecond() {
        Calendar now = Calendar.getInstance();
        now.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        int minute = now.get(Calendar.SECOND);
        return minute;
    }

    /**
     * 求两个时间戳相差几分钟
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static Long timestampDistance(Long startTime, Long endTime) {
        Long s = (endTime - startTime) / (1000 * 60);
        return s;
    }


    /**
     * 使用带缓存的方式格式化
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String date2Str(Date date, String pattern) {
        return FastDateFormat.getInstance(pattern).format(date);
    }

    /**
     * 时间字符串转换为时间格式
     *
     * @param strDate
     * @param pattern
     * @return
     */
    public static Date str2Date(String strDate, String pattern) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.parse(strDate);
    }

    /**
     * Date转时间戳
     *
     * @param date
     * @return
     */
    public static long date2Timestamp(Date date) {
        return date.getTime();
    }

    /**
     * timestamp 转Date
     *
     * @param timestamp
     * @return
     */
    public static Date timestamp2Date(long timestamp) {
        return new Date(timestamp);
    }

    /**
     * timestamp转字符串
     *
     * @param timestamp
     * @param pattern
     * @return
     */
    public static String timestamp2Str(long timestamp, String pattern) {
        Date date = timestamp2Date(timestamp);
        return date2Str(date, pattern);
    }

    /**
     * 字符串转时间戳
     *
     * @param strDate
     * @param pattern
     * @return
     */
    public static long str2Timestamp(String strDate, String pattern) throws ParseException {
        return str2Date(strDate, pattern).getTime();
    }
}
