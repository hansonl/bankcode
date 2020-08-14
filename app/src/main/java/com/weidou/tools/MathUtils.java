package com.weidou.tools;

import android.util.Log;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static android.content.ContentValues.TAG;

public class MathUtils {

    private SimpleDateFormat simpleDateFormat4;
    private Calendar calendarString; //初始化时间的calendar
    private Calendar nowCalendar; // 当前时间的calendar

    private MathUtils() {
    }

    private static class LazyHolder {
        private static final MathUtils INSTANCE = new MathUtils();
    }

    public static final MathUtils getInstance() {
        return LazyHolder.INSTANCE;
    }


//    pfirstAmt * incomeRate / 365 * ( liveTime - （ prdNextDate - nowDate ） - 4) > |concesPrice|

    public boolean judgeItem(double pfirstAmt, double incomeRate, String endDate, String prdNextDate,String nextEndDate,String startDate ,String prdType, double concesPrice, int liveTime) throws ParseException {
        String realTime =  null;
        Date date = new Date();
        if ("2".equals(prdType) || "6".equals(prdType)) {
            realTime = endDate;
        } else {
            if (date.getTime() <= getMillTime(startDate) && getMillTime(prdNextDate) > getMillTime(startDate)) {
                realTime = prdNextDate;
            } else {
                if (date.getTime() <= getMillTime(startDate) && getMillTime(prdNextDate) <= getMillTime(startDate)) {
                    realTime = nextEndDate;
                } else {
                    if (date.getTime() > getMillTime(startDate)) {
                        realTime = prdNextDate;
                    }
                }
            }
        }
        double mul = mul(div(mul(pfirstAmt, incomeRate), 365, DEF_DIV_SCALE), liveTime - fromToday(realTime) - 3.8);
        Log.i(TAG,"realTime" + realTime+
                "fromDate:" + fromToday(realTime) +
                "result:" + mul
        );
        return aMoreThanb(mul
                , Math.abs(concesPrice));
    }


//    (vol + ((vol * incomeRate) * liveTime / 365 ) - amt) / amt / |(ipoEndDate - nowDate)| * 365


    public boolean judgeItemRate(double vol, double incomeRate,double amt,String endDate, String prdNextDate,String nextEndDate,String startDate ,String prdType, double rate, int liveTime) throws ParseException {
        double mul = div(add(vol,sub((div(mul(mul(vol, incomeRate),liveTime), 365,DEF_DIV_SCALE)),amt)),amt,DEF_DIV_SCALE);
        String realTime =  null;
        Date date = new Date();
        if ("2".equals(prdType) || "6".equals(prdType)) {
            realTime = endDate;
        } else {
            if (date.getTime() <= getMillTime(startDate) && getMillTime(prdNextDate) > getMillTime(startDate)) {
                realTime = prdNextDate;
            } else {
                if (date.getTime() <= getMillTime(startDate) && getMillTime(prdNextDate) <= getMillTime(startDate)) {
                    realTime = nextEndDate;
                } else {
                    if (date.getTime() > getMillTime(startDate)) {
                        realTime = prdNextDate;
                    }
                }
            }
        }
        double result = mul(div(mul,fromToday(realTime),DEF_DIV_SCALE),365);

        Log.i(TAG,"realTime" + realTime+
                "fromDate:" + fromToday(realTime) +
                        "result:" + result
        );
        return aMoreThanb(result
                , rate);
    }


    // 默认除法运算精度
    private final int DEF_DIV_SCALE = 8;

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 说明：
     * 提供相对精确的除法运算，当发生除不尽的情况，精确到.后10位
     *
     * @param v1
     * @param v2
     * @return
     */
    public double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(" the scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();// scale 后的四舍五入
    }


    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * double 相减
     *
     * @param d1
     * @param d2
     * @return
     */
    public double sub(double d1, double d2) {
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.subtract(bd2).doubleValue();
    }


    public boolean aMoreThanb(double a, double b) {
        BigDecimal data1 = new BigDecimal(a);
        BigDecimal data2 = new BigDecimal(b);
        if (data1.compareTo(data2) < 0) {
            System.out.println("第二位数大！");
            return false;
        }
        if (data1.compareTo(data2) == 0) {
            System.out.println("两位数一样大！");
            return false;
        }
        if (data1.compareTo(data2) > 0) {
            System.out.println("第一位数大！");
            return true;
        }
        return false;
    }

    /**
     * 距离今天多久
     *
     * @param timeString
     * @return
     */
    public int fromToday(String timeString) throws ParseException {
        if (simpleDateFormat4 == null) {
            simpleDateFormat4 = new SimpleDateFormat("yyyy-MM-dd");
            simpleDateFormat4.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        }
        Date date = null;
            date = simpleDateFormat4.parse(timeString);
        Date date1 = new Date();


        return (int) ((date.getTime() - date1.getTime()) / (1000 * 3600 * 24)) + 1;
    }

    public long getMillTime(String timeString) {
        if (simpleDateFormat4 == null) {
            simpleDateFormat4 = new SimpleDateFormat("yyyy-MM-dd");
            simpleDateFormat4.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        }
        Date date = null;
        try {
            date = simpleDateFormat4.parse(timeString);
        } catch (ParseException e) {
            return  0;
        }
        return  date.getTime();
    }
}
