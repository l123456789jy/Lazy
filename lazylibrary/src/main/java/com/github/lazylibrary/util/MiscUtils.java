package com.github.lazylibrary.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Locale;

/**
 *
 */
public class MiscUtils {
    private final static String TAG = MiscUtils.class.getSimpleName();
    public static final String APP_FOLDER_ON_SD =
            Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/TianXiang/TianXiang";


    public static boolean checkFsWritable() {
        // Create a temporary file to see whether a volume is really writeable.
        // It's important not to put it in the root directory which may have a
        // limit on the number of files.

        // Logger.d(TAG, "checkFsWritable directoryName ==   "
        // + PathCommonDefines.APP_FOLDER_ON_SD);

        File directory = new File(APP_FOLDER_ON_SD);
        if (!directory.isDirectory()) {
            if (!directory.mkdirs()) {
                return false;
            }
        }
        File f = new File(APP_FOLDER_ON_SD, ".probe");
        try {
            // Remove stale file if any
            if (f.exists()) {
                f.delete();
            }
            if (!f.createNewFile()) {
                return false;
            }
            f.delete();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }


    public static boolean hasStorage() {
        boolean hasStorage = false;
        String str = Environment.getExternalStorageState();

        if (str.equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            hasStorage = checkFsWritable();
        }

        return hasStorage;
    }


    /**
     *
     * @param dir  目标文件
     * @param fileName  文件名
     */
    public static void updateFileTime(String dir, String fileName) {

        File file = new File(dir, fileName);
        long newModifiedTime = System.currentTimeMillis();
        file.setLastModified(newModifiedTime);
    }


    /**
     *
     * @param context  上下文
     * @return  是否有网络
     */
    public static boolean checkNet(Context context) {

        ConnectivityManager manager
                = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null) {
            return true;
        }
        return false;
    }


    /**
     *
     * @param context  上下文
     * @return  apn
     */
    public static String getAPN(Context context) {

        String apn = "";
        ConnectivityManager manager
                = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();

        if (info != null) {
            if (ConnectivityManager.TYPE_WIFI == info.getType()) {
                apn = info.getTypeName();
                if (apn == null) {
                    apn = "wifi";
                }
            }
            else {
                apn = info.getExtraInfo().toLowerCase();
                if (apn == null) {
                    apn = "mobile";
                }
            }
        }
        return apn;
    }


    /**
     *
     * @param context  上下文
     * @return  model
     */
    public static String getModel(Context context) {

        return Build.MODEL;
    }

    //
    // public static String getHardware(Context context) {
    // if (getPhoneSDK(context) < 8) {
    // return "undefined";
    // } else {
    // Logger.d(TAG, "hardware:" + Build.HARDWARE);
    // }
    // return Build.HARDWARE;
    // }


    /**
     *
     * @param context  context
     * @return  MANUFACTURER
     */
    public static String getManufacturer(Context context) {

        return Build.MANUFACTURER;
    }


    /**
     *
     * @param context  context
     * @return  RELEASE
     */
    public static String getFirmware(Context context) {

        return Build.VERSION.RELEASE;
    }


    /**
     *
     * @return  sdkversion
     */
    public static String getSDKVer() {

        return Integer.valueOf(Build.VERSION.SDK_INT).toString();
    }


    /**
     *
     * @return  获取语言
     */
    public static String getLanguage() {

        Locale locale = Locale.getDefault();
        String languageCode = locale.getLanguage();
        if (TextUtils.isEmpty(languageCode)) {
            languageCode = "";
        }
        return languageCode;
    }


    /**
     *
     * @return  获取国家
     */
    public static String getCountry() {

        Locale locale = Locale.getDefault();
        String countryCode = locale.getCountry();
        if (TextUtils.isEmpty(countryCode)) {
            countryCode = "";
        }
        return countryCode;
    }


    /**
     *
     * @param context   context
     * @return  imei
     */
    public static String getIMEI(Context context) {

        TelephonyManager mTelephonyMgr
                = (TelephonyManager) context.getSystemService(
                Context.TELEPHONY_SERVICE);
        String imei = mTelephonyMgr.getDeviceId();
        if (TextUtils.isEmpty(imei) || imei.equals("000000000000000")) {
            imei = "0";
        }

        return imei;
    }


    /**
     *
     * @param context  context
     * @return  imsi
     */
    public static String getIMSI(Context context) {

        TelephonyManager mTelephonyMgr
                = (TelephonyManager) context.getSystemService(
                Context.TELEPHONY_SERVICE);
        String imsi = mTelephonyMgr.getSubscriberId();
        if (TextUtils.isEmpty(imsi)) {
            return "0";
        }
        else {
            return imsi;
        }
    }

    // public static String getLac(Context context) {
    // CellIdInfo cell = new CellIdInfo();
    // CellIDData cData = cell.getCellId(context);
    // return (cData != null ? cData.getLac() : "1");
    // }
    //
    // public static String getCellid(Context context) {
    // CellIdInfo cell = new CellIdInfo();
    // CellIDData cData = cell.getCellId(context);
    // return (cData != null ? cData.getCellid() : "1");
    // }


    /**
     *
     * @param context  context
     * @return  mcnc
     */
    public static String getMcnc(Context context) {

        TelephonyManager tm = (TelephonyManager) context.getSystemService(
                Context.TELEPHONY_SERVICE);
        String mcnc = tm.getNetworkOperator();
        if (TextUtils.isEmpty(mcnc)) {
            return "0";
        }
        else {
            return mcnc;
        }
    }


    /**
     * Get phone SDK version
     * @param mContext      mContext
     * @return  SDK version
     */
    public static int getPhoneSDK(Context mContext) {

        TelephonyManager phoneMgr
                = (TelephonyManager) mContext.getSystemService(
                Context.TELEPHONY_SERVICE);
        int sdk = 7;
        try {
            sdk = Integer.parseInt(Build.VERSION.SDK);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return sdk;
    }


    /**
     *
     * @param context  context
     * @param keyName  keyName
     * @return  data
     */
    public static Object getMetaData(Context context, String keyName) {

        try {
            ApplicationInfo info = context.getPackageManager()
                                          .getApplicationInfo(
                                                  context.getPackageName(),
                                                  PackageManager.GET_META_DATA);

            Bundle bundle = info.metaData;
            Object value = bundle.get(keyName);
            return value;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }


    /**
     *
     * @param context context
     * @return  AppVersion
     */
    public static String getAppVersion(Context context) {

        PackageManager pm = context.getPackageManager();
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(context.getPackageName(), 0);
            String versionName = pi.versionName;
            return versionName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }


    /**
     *
     * @param context  context
     * @return  SerialNumber
     */
    public static String getSerialNumber(Context context) {

        String serial = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");
            if (serial == null || serial.trim().length() <= 0) {
                TelephonyManager tManager
                        = (TelephonyManager) context.getSystemService(
                        Context.TELEPHONY_SERVICE);
                serial = tManager.getDeviceId();
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return serial;
    }


    /**
     * SDCard
     * @return    SDCard
     */
    public boolean isSDCardSizeOverflow() {

        boolean result = false;

        String sDcString = Environment.getExternalStorageState();

        if (sDcString.equals(Environment.MEDIA_MOUNTED)) {

            File pathFile = Environment.getExternalStorageDirectory();
            StatFs statfs = new StatFs(pathFile.getPath());

            long nTotalBlocks = statfs.getBlockCount();

            long nBlocSize = statfs.getBlockSize();

            long nAvailaBlock = statfs.getAvailableBlocks();

            long nFreeBlock = statfs.getFreeBlocks();

            long nSDTotalSize = nTotalBlocks * nBlocSize / 1024 / 1024;

            long nSDFreeSize = nAvailaBlock * nBlocSize / 1024 / 1024;
            if (nSDFreeSize <= 1) {
                result = true;
            }
        }// end of if
        // end of func
        return result;
    }
}
