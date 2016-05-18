package com.github.lazylibrary.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.util.Calendar;

/**
 * 作者：liujingyuan on 2016/3/21 17:41
 * 邮箱：906514731@qq.com
 * 定时器
 */
public class AmUtiles {
    private static AlarmManager am;
    private static PendingIntent pendingIntent;


    /**
     * 简化10分钟之后启动一个intent 600代表10分钟   20代表20秒
     */
    public static AlarmManager sendUpdateBroadcastRepeat(Context ctx, Intent intent) {
        pendingIntent = PendingIntent.getBroadcast(ctx, 0, intent, 0);
        am = (AlarmManager) ctx.getSystemService(ctx.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 600 * 1000,
                pendingIntent);
        return am;
    }


    /**
     * 应用退出了就取消闹钟
     */
    public static void destoryAlarmManger() {
        if (null != am) {
            am.cancel(pendingIntent);
        }
    }


    /**
     * 设置午夜定时器, 午夜12点发送广播, MIDNIGHT_ALARM_FILTER.
     * 实际测试可能会有一分钟左右的偏差.
     *
     * @param context 上下文
     */
    public static void setMidnightAlarm(Context context, String action) {
        Context appContext = context.getApplicationContext();
        Intent intent = new Intent(action);

        PendingIntent pi = PendingIntent.getBroadcast(appContext, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) appContext.getSystemService(
                Context.ALARM_SERVICE);

        // 午夜12点的标准计时, 来源于SO, 实际测试可能会有一分钟左右的偏差.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.AM_PM, Calendar.AM);
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        // 显示剩余时间
        long now = Calendar.getInstance().getTimeInMillis();
        Log.e("剩余时间(秒): ", ((calendar.getTimeInMillis() - now) / 1000) + "");

        // 设置之前先取消前一个PendingIntent
        am.cancel(pi);
        // 设置每一天的计时器
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pi);
    }
}
