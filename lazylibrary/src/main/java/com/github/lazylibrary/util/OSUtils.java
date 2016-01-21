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

import android.os.Build;
import java.io.File;

/**
 * Android系统工具箱
 */
public class OSUtils {
    /**
     * 根据/system/bin/或/system/xbin目录下是否存在su文件判断是否已ROOT
     * @return true：已ROOT
     */
    public static boolean isRoot() {
        try {
            return new File("/system/bin/su").exists() || new File("/system/xbin/su").exists();
        } catch (Exception e) {
            return false;
        }
    }
	
	/**
	 * 判断当前系统是否是Android4.0
	 * @return 0：是；小于0：小于4.0；大于0：大于4.0
	 */
	public static int isAPI14(){
		return Build.VERSION.SDK_INT - 14;
	}
}