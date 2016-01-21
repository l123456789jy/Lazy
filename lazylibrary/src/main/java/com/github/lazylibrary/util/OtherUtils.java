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


public class OtherUtils {
	/**
	 * 为给定的字符串添加HTML红色标记，当使用Html.fromHtml()方式显示到TextView 的时候其将是红色的
	 * @param string 给定的字符串
	 * @return
	 */
	public static String addHtmlRedFlag(String string){
		return "<font color=\"red\">"+string+"</font>";
	}
	
	/**
	 * 将给定的字符串中所有给定的关键字标红
	 * @param sourceString 给定的字符串
	 * @param keyword 给定的关键字
	 * @return 返回的是带Html标签的字符串，在使用时要通过Html.fromHtml()转换为Spanned对象再传递给TextView对象
	 */
	public static String keywordMadeRed(String sourceString, String keyword){
		String result = "";
		if(sourceString != null && !"".equals(sourceString.trim())){
			if(keyword != null && !"".equals(keyword.trim())){
				result = sourceString.replaceAll(keyword, "<font color=\"red\">"+keyword+"</font>"); 
			}else{
				result = sourceString;
			}
		}
		return result;
	}
}