/*
 * Copyright (C) 2013 Peng fei Pan <sky@xiaopan.me>
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

package com.github.lazylibrary.util;

import android.content.Context;
import android.widget.Toast;
import java.util.Locale;

/**
 * 双击退出识别器
 */
public class DoubleClickExitDetector {
	public static String DEFAULT_HINT_MESSAGE_CHINA = "再按一次退出程序";
	public static String DEFAULT_HINT_MESSAGE_OTHER = "Press again to exit the program";
	private int effectiveIntervalTime;	// 有效的间隔时间，单位毫秒
	private long lastClickTime;	// 上次点击时间
	private String hintMessage;	// 提示消息
	private Context context;

	/**
	 * 创建一个双击退出识别器
	 * @param context Androdi上下文
	 * @param hintMessage 提示消息
	 * @param effectiveIntervalTime 有效间隔时间
	 */
	public DoubleClickExitDetector(Context context, String hintMessage, int effectiveIntervalTime) {
		this.context = context;
		this.hintMessage = hintMessage;
		this.effectiveIntervalTime = effectiveIntervalTime;
	}

	/**
	 * 创建一个双击退出识别器，有效间隔时间默认为2000毫秒
	 * @param context Androdi上下文
	 * @param hintMessage 提示消息
	 */
	public DoubleClickExitDetector(Context context, String hintContent) {
		this(context, hintContent, 2000);
	}

	/**
	 * 创建一个双击退出识别器，中国环境下默认提示消息为“再按一次退出程序”，其它环境下默认提示消息为“Press again to exit the program”；有效间隔时间默认为2000毫秒
	 * @param context Androdi上下文
	 */
	public DoubleClickExitDetector(Context context) {
		this(context, Locale.CHINA.equals(Locale.getDefault()) ? DEFAULT_HINT_MESSAGE_CHINA : DEFAULT_HINT_MESSAGE_OTHER, 2000);
	}
	
	/**
	 * 点击，你需要重写Activity的onBackPressed()方法，先调用此方法，如果返回true就执行父类的onBackPressed()方法来关闭Activity否则不执行
	 * @return 当两次点击时间间隔小于有效间隔时间时就会返回true，否则返回false
	 */
	public boolean click(){
		long currentTime = System.currentTimeMillis();
		boolean result = (currentTime-lastClickTime) < effectiveIntervalTime;
		lastClickTime = currentTime;
		if(!result){
			Toast.makeText(context, hintMessage, Toast.LENGTH_SHORT).show();
		}
		return result;
	}

	/**
	 * 设置有效间隔时间，单位毫秒
	 * @param effectiveIntervalTime 效间隔时间，单位毫秒。当两次点击时间间隔小于有效间隔时间click()方法就会返回true
	 */
	public void setEffectiveIntervalTime(int effectiveIntervalTime) {
		this.effectiveIntervalTime = effectiveIntervalTime;
	}

	/**
	 * 设置提示消息
	 * @param hintMessage 当前点击同上次点击时间间隔大于有效间隔时间click()方法就会返回false，并且显示提示消息
	 */
	public void setHintMessage(String hintMessage) {
		this.hintMessage = hintMessage;
	}
}
