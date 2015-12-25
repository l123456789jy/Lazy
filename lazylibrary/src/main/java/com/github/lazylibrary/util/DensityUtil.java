package com.github.lazylibrary.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * liujingyuan
 */
public class DensityUtil {

	private static int[] deviceWidthHeight = new int[2];
	public static int[] getDeviceInfo(Context context) {
		if ((deviceWidthHeight[0] == 0) && (deviceWidthHeight[1] == 0)) {
			DisplayMetrics metrics = new DisplayMetrics();
			((Activity) context).getWindowManager().getDefaultDisplay()
					.getMetrics(metrics);

			deviceWidthHeight[0] = metrics.widthPixels;
			deviceWidthHeight[1] = metrics.heightPixels;
		}
		return deviceWidthHeight;
	}
	/**
	 *
	 * @param context 上下文
	 * @param dpValue dp数值
	 * @return dp to  px
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);

	}

	/**
	 *
	 * @param context    上下文
	 * @param pxValue  px的数值
	 * @return  px to dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);

	}
}
