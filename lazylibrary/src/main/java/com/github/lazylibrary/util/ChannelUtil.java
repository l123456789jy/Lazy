package com.github.lazylibrary.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ChannelUtil {
    private static final String CHANNEL_KEY = "channel_key";
    private static final String CHANNEL_VERSION_KEY = "channel_version_key";
    private static String mChannel;

    /**
     *
     *
     * @param context 上下文
     * @return  返回市场号
     */
    public static String getChannel(Context context) {
        return getChannel(context, "Channel_Default");
    }

    /**
     *
     *
     * @param context  上下文
     * @param defaultChannel  数值
     * @return 返回市场。  如果获取失败返回defaultChannel
     */
    public static String getChannel(Context context, String defaultChannel) {
        //内存中获取
        if (!TextUtils.isEmpty(mChannel)) {
            return mChannel;
        }
        //sp中获取
        mChannel = getChannelBySharedPreferences(context);
        if (!TextUtils.isEmpty(mChannel)) {
            return mChannel;
        }
        //从apk中获取
        mChannel = getChannelFromApk(context, CHANNEL_KEY);
        if (!TextUtils.isEmpty(mChannel)) {
            //保存sp中备用
            saveChannelBySharedPreferences(context, mChannel);
            return mChannel;
        }
        //全部获取失败
        return defaultChannel;
    }

    /**
     * 从apk中获取版本信息
     *
     * @param context
     * @param channelKey
     * @return
     */
    private static String getChannelFromApk(Context context, String channelKey) {
        //从apk包中获取
        ApplicationInfo appinfo = context.getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        //默认放在meta-inf/里， 所以需要再拼接一下
        String key = "META-INF/" + channelKey;
        String ret = "";
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(sourceDir);
            Enumeration<?> entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String entryName = entry.getName();
                if (entryName.startsWith(key)) {
                    ret = entryName;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String[] split = ret.split("\\*");
        String channel = "";
        if (split != null && split.length >= 2) {
            channel = ret.substring(split[0].length() + 1);
        }
        return channel;
    }

    /**
     * 本地保存channel & 对应版本号
     *
     * @param context
     * @param channel
     */
    private static void saveChannelBySharedPreferences(Context context, String channel) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString(CHANNEL_KEY, channel);
        editor.putInt(CHANNEL_VERSION_KEY, getVersionCode(context));
        editor.commit();
    }

    /**
     * 从sp中获取channel
     *
     * @param context
     * @return 为空表示获取异常、sp中的值已经失效、sp中没有此值
     */
    private static String getChannelBySharedPreferences(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        int currentVersionCode = getVersionCode(context);
        if (currentVersionCode == -1) {
            //获取错误
            return "";
        }
        int versionCodeSaved = sp.getInt(CHANNEL_VERSION_KEY, -1);
        if (versionCodeSaved == -1) {
            //本地没有存储的channel对应的版本号
            //第一次使用  或者 原先存储版本号异常
            return "";
        }
        if (currentVersionCode != versionCodeSaved) {
            return "";
        }
        return sp.getString(CHANNEL_KEY, "");
    }

    /**
     * 从包信息中获取版本号
     *
     * @param context
     * @return
     */
    private static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }
}