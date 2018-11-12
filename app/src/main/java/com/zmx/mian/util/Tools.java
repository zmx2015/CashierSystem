package com.zmx.mian.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zmx.mian.R;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 开发人员：曾敏祥
 * 开发时间：2018-07-29 19:19
 * 类功能：工具类
 */

public class Tools {


    public static final int ONE_SECOND = 1 * 1000;
    private static Toast toast;

    /**
     * 时间格式转换为字符串
     *
     * @param date
     * @return
     */
    public static String DateConversion(Date date) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);

        return dateString;
    }

    /**
     * 时间格式转换为字符串
     *
     * @param date
     * @return
     */
    public static String DateConversions(Date date) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = formatter.format(date);

        return dateString;
    }

    public static void showText(final Context context, final int resId,
                                final int duration) {
        Handler handler = new Handler(Looper.getMainLooper());
        final Context ctx = context;
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (null == context) {
                    return;
                }
                View view = LayoutInflater.from(ctx).inflate(
                        R.layout.dialog_toast, null);
                TextView message = (TextView) view.findViewById(R.id.tv_msg);
                message.setText(resId);

                if (toast == null)
                    toast = new Toast(ctx);
                toast.setGravity(Gravity.BOTTOM, Gravity.CENTER_HORIZONTAL, Gravity.BOTTOM);
                toast.setMargin(0, 0.1f);
                toast.setDuration(duration);
                toast.setView(view);
                toast.show();
            }
        });
        Log.d("ToastUtils", context.getResources().getString(resId));
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * php时间戳转java时间戳
     *
     * @param time
     * @param state
     * @return
     */
    public static String refFormatNowDate(String time, int state) {

        time = time + "000";
        Date nowTime = new Date(Long.parseLong(time));
        SimpleDateFormat sdFormatter = null;
        if (state == 1) {

            sdFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        } else {

            sdFormatter = new SimpleDateFormat("yyyy-MM-dd");

        }
        String retStrFormatNowDate = sdFormatter.format(nowTime);
        return retStrFormatNowDate;

    }

    // 判断输入是否为正常的数字
    public static boolean isNumeric(String str) {

        String reg = "^[0-9]+(.[0-9]+)?$";
        return str.matches(reg);

    }

    //获得昨天的日期
    public static String getYesterday() {

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(calendar.DATE, -1);
        date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(date);
        System.out.println(dateString);

        return dateString;

    }


    //获得key
    public String getKey(Context context) {

        return new Tools().getKey(MySharedPreferences.getInstance(context).getString(MySharedPreferences.name, ""));

    }


    // 获取key
    public static String getKey(String account) {

        String txt = account + "xbjrws";
        double times = (double) Long.parseLong(String
                .valueOf(System.currentTimeMillis()).toString()
                .substring(0, 10)) * 74;
        BigDecimal a = new BigDecimal(times);
        String keys = md5(txt) + a.toString();
        return keys;

    }

    /**
     * Md5加密函数
     *
     * @param txt
     * @return
     */
    public static String md5(String txt) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(txt.getBytes("GBK")); // Java的字符串是unicode编码，不受源码文件的编码影响；而PHP的编码是和源码文件的编码一致，受源码编码影响。
            StringBuffer buf = new StringBuffer();
            for (byte b : md.digest()) {
                buf.append(String.format("%02x", b & 0xff));
            }
            return buf.toString();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


    //获得这个月最后一天
    public static String getDateLastDay(String year, String month) {

        //year="2018" month="2"
        Calendar calendar = Calendar.getInstance();
        // 设置时间,当前时间不用设置
        calendar.set(Calendar.YEAR, Integer.parseInt(year));
        calendar.set(Calendar.MONTH, Integer.parseInt(month));

        // System.out.println(calendar.getTime());

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd ");
        return format.format(calendar.getTime());
    }

}
