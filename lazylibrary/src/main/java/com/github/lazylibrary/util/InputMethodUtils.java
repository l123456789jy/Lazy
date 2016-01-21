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

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class InputMethodUtils {
	/**
	 * 为给定的编辑器开启软键盘
	 * @param context 
	 * @param editText 给定的编辑器
	 */
	public static void openSoftKeyboard(Context context, EditText editText){
		editText.requestFocus();
		InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(
				Context.INPUT_METHOD_SERVICE);
		inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
		ViewUtils.setEditTextSelectionToEnd(editText);
	}
	
	/**
	 * 关闭软键盘
	 * @param context
	 */
	public static void closeSoftKeyboard(Activity activity){
		InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(
				Context.INPUT_METHOD_SERVICE);
		//如果软键盘已经开启
		if(inputMethodManager.isActive()){
			inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	
	/**
	 * 切换软键盘的状态
	 * @param context
	 */
	public static void toggleSoftKeyboardState(Context context){
		((InputMethodManager) context.getSystemService(
				Context.INPUT_METHOD_SERVICE)).toggleSoftInput(
				InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
	}
}
