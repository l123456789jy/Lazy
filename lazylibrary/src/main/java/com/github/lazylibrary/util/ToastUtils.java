package com.github.lazylibrary.util;

import android.content.Context;
import android.widget.Toast;

/**
 * ToastUtils
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-12-9
 */
public class ToastUtils {
    private static Toast toast = null; //Toast的对象！

    private static void showToast(CharSequence msg, int flag) {
        if (toast == null) {
            toast = Toast.makeText(getApplicationContext(), msg, flag);
        } else {
            toast.setText(msg);
            toast.setDuration(flag);
        }
        toast.show();
    }

    public static void showToast(CharSequence msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }

    public static void showToast(int resId) {
        showToast(getApplicationContext().getResources().getText(resId));
    }

    public static void showToastLong(CharSequence msg) {
        showToast(msg, Toast.LENGTH_LONG);
    }

    public static void showToastLong(int resId) {
        showToastLong(getApplicationContext().getResources().getText(resId));
    }
    
    /**
     * 使用前，需要先实现此方法。使用ApplicatioContex，避免内存泄露
     */ 
    private Context getApplicationContext() {
        throw new IllegalStateException("尚未实现Toaster#getApplicationContext方法！！！");
    }
}
