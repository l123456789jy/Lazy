package com.github.lazylibrary.util; /**
 * Copyright 2014 Zhenguo Jin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import java.io.File;

/**
 * 手机组件调用工具类
 *
 * @author jingle1267@163.com
 */
public final class PhoneUtil {
  private static long lastClickTime;
    /**
     * Don't let anyone instantiate this class.
     */
    private PhoneUtil() {
        throw new Error("Do not need instantiate!");
    }


    /**
     * 调用系统发短信界面
     *
     * @param activity    Activity
     * @param phoneNumber 手机号码
     * @param smsContent  短信内容
     */
    public static void sendMessage(Context activity, String phoneNumber, String smsContent) {
        if (phoneNumber == null || phoneNumber.length() < 4) {
            return;
        }
        Uri uri = Uri.parse("smsto:" + phoneNumber);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", smsContent);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(it);
    }

    /**
     * 判断是否为连击
     *
     * @return  boolean
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 获取手机型号
     *
     * @param context  上下文
     * @return   String
     */
    public static String getMobileModel(Context context) {
        try {
            String model = android.os.Build.MODEL; // 手机型号
            return model;
        } catch (Exception e) {
            return "未知";
        }
    }

    /**
     * 获取手机品牌
     *
     * @param context  上下文
     * @return  String
     */
    public static String getMobileBrand(Context context) {
        try {
            String brand = android.os.Build.BRAND; // android系统版本号
            return brand;
        } catch (Exception e) {
            return "未知";
        }
    }


    /**
     *拍照打开照相机！
     * @param requestcode   返回值
     * @param activity   上下文
     * @param fileName    生成的图片文件的路径
     */
    public static void toTakePhoto(int requestcode, Activity activity, String fileName) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("camerasensortype", 2);// 调用前置摄像头
        intent.putExtra("autofocus", true);// 自动对焦
        intent.putExtra("fullScreen", false);// 全屏
        intent.putExtra("showActionIcons", false);
        try {//创建一个当前任务id的文件然后里面存放任务的照片的和路径！这主文件的名字是用uuid到时候在用任务id去查路径！
            File file = new File(fileName);
            Uri uri = Uri.fromFile(file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            activity.startActivityForResult(intent, requestcode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *打开相册
     * @param requestcode  响应码
     * @param activity  上下文
     */
    public static void toTakePicture(int requestcode, Activity activity){
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        activity.startActivityForResult(intent, requestcode);
    }


    /**
     * 获取所有联系人的姓名和电话号码，需要READ_CONTACTS权限
     * @param context 上下文
     * @return Cursor。姓名：CommonDataKinds.Phone.DISPLAY_NAME；号码：CommonDataKinds.Phone.NUMBER
     */
    public static Cursor getContactsNameAndNumber(Context context){
        return context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[] {
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
    }

    /**
     * 获取手机号码
     * @param context 上下文
     * @return 手机号码，手机号码不一定能获取到
     */
    public static String getMobilePhoneNumber(Context context){
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();
    }

}
