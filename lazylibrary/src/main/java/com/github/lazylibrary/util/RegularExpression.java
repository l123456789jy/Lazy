/**
 * Copyright (c) 2010 北京数字政通科技股份有限公司
 * 版权所有
 * 
 * 修改标识：赵冲20110506
 * 修改描述：创建
 */
package com.github.lazylibrary.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则验证相关操作的类.
 * 
 * @version 0.1 20110506
 * @author 赵冲
 */
public class RegularExpression {

	public final static String DESC_NORMALTEXT = "不能包含特殊字符，且不能为空.";

	/**
	 * 正则验证
	 * 
	 * @param toCheckStr 待验证的字符串
	 * @param patternStr 验证格式字符串
	 * @return 是否通过验证
	 */
	public static boolean canMatch(String toCheckStr, String patternStr) {
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(toCheckStr);
		if(!matcher.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 验证是否为整数.
	 * 
	 * @param toCheckStr 待验证的字符串
	 * @return 是否通过验证
	 */
	public static boolean isNumeric(String toCheckStr) {
		return canMatch(toCheckStr, "[0-9][0-9]*");
	}

	/**
	 * 验证是否为整数或字母.
	 * 
	 * @param toCheckStr 待验证的字符串
	 * @return 是否通过验证
	 */
	public static boolean isNumOrChar(String toCheckStr) {
		return canMatch(toCheckStr, "[a-zA-Z0-9][a-zA-Z0-9]*");
	}

	/**
	 * 验证是否为身份证号
	 * 
	 * @param toCheckStr 待验证的字符串
	 * @return 是否通过验证
	 */
	public static boolean isIDCard(String toCheckStr) {
		// String patternStr =
		// "/^((1[1-5])|(2[1-3])|(3[0-7])|(4[1-6])|(5[0-4])|(6[0-9])|(7[12])|(8[0-9])|(9[0-9])|(10[0-9])|(11[0-1])|(12[0-9])|(13[0-3])|(14[0-9]))"
		// + "\\d{4}("
		// + "(19\\d{2}(0[13-9]|1[012])(0[1-9]|[12]\\d|30))"
		// + "|(19\\d{2}(0[13578]|1[02])31)"
		// + "|(19\\d{2}02(0[1-9]|1\\d|2[0-8]))"
		// + "|(19([13579][26]|[2468][048]|0[48])0229)"
		// + ")\\d{3}(\\d|X|x)?$/";
		// String patternStr1 =
		// "/^((1[1-5])|(2[1-3])|(3[0-7])|(4[1-6])|(5[0-4])|(6[0-9])|(7[0-9])|(8[0-9])|(9[0-1])|(10[0-9])|(11[0-3])|(12[0-9]))"
		// + "\\d{4}("
		// + "(16\\d{2}(0[13-9]|1[012])(0[1-9]|[12]\\d|30))"
		// + "|(16\\d{2}(0[13578]|1[02])31)"
		// + "|(16\\d{2}02(0[1-9]|1\\d|2[0-8]))"
		// + "|(16([13579][26]|[2468][048]|0[48])0229)"
		// + ")\\d{3}(\\d|X|x)?$/";
		String isIDCard1 = "^(([0-9]{14}[x0-9]{1})|([0-9]{17}[x0-9]{1}))$";
		// String
		// isIDCard2="/^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$/";
		return canMatch(toCheckStr, isIDCard1);// || canMatch(toCheckStr,
												// isIDCard2);
	}

	/**
	 * 验证是否为电话号码
	 * 
	 * @param toCheckStr 待验证的字符串
	 * @return 是否通过验证
	 */
	public static boolean isTeleNo(String toCheckStr) {
		String patternStr = "(^[0-9]{3,4}\\-[0-9]{3,8}$)|(^[0-9]{3,8}$)|(^\\([0-9]{3,4}\\)[0-9]{3,8}$)|(^0{0,1}13[0-9]{9}$)";
		return canMatch(toCheckStr, patternStr);
	}

	/**
	 * 验证是否为合法的用户名. 用户名只能由汉字、数字、字母、下划线组成，且不能为空.
	 * 
	 * @param toCheckStr 待验证的字符串
	 * @return 是否通过验证
	 */
	public static boolean isUserName(String toCheckStr) {
		String patternStr = "^[a-zA-Z0-9_\u4e00-\u9fa5]+$";
		return canMatch(toCheckStr, patternStr);
	}

	/**
	 * 验证是否为汉字.
	 * 
	 * @param toCheckStr 待验证的字符串
	 * @return 是否通过验证
	 */
	public static boolean isCH(String toCheckStr) {
		String patternStr = "^[\u4e00-\u9fa5]+$";
		return canMatch(toCheckStr, patternStr);
	}

	/**
	 * 验证是否为正常的文本内容. 内容只能为：汉字、数字、字母、下划线、 中文标点符号
	 * 英文标点符号，且不能为空.
	 * 
	 * @param toCheckStr 待验证的字符串
	 * @return 是否通过验证
	 */
	public static boolean isNormalText(String toCheckStr) {
		String patternStr = "^[a-zA-Z0-9_\u4e00-\u9fa5" // 汉字、数字、字母、下划线
				// 中文标点符号（。 ； ， ： “ ”（ ） 、 ！ ？ 《 》）
				+ "\u3002\uff1b\uff0c\uff1a\u201c\u201d\uff08\uff09\u3001\uff01\uff1f\u300a\u300b"
				// 英文标点符号（. ; , : ' ( ) / ! ? < >）
				+ "\u002e\u003b\u002c\u003a\u0027\u0028\u0029\u002f\u0021\u003f\u003c\u003e\r\n"
				+ "]+$";
		return canMatch(toCheckStr, patternStr);
	}

	/**
	 * 验证是否为Url的文本内容. 内容只能为：数字、字母、英文标点符号（. : / ），且不能为空.
	 * 
	 * @param toCheckStr 待验证的字符串
	 * @return 是否通过验证
	 */
	public static boolean isUrlText(String toCheckStr) {
		String patternStr = "^[a-zA-Z0-9" // 数字、字母
				// 英文标点符号（. : /）
				+ "\u002e\u003a\u002f"
				+ "]+$";
		return canMatch(toCheckStr, patternStr);
	}

	/**
	 * 判断房间号是否符合规范：例如102,1202... 先判断3位或者4位的数字
	 * 
	 * @param roomNumber  roomNumber
	 * @return   boolean
	 */
	public static boolean checkRoomNumber(String roomNumber) {
		String regex = "^\\d{3,4}$";
		return Pattern.matches(regex, roomNumber);
	}

	/**
	 * 将身份证后六位隐藏,不显示
	 * 
	 * @param identityID  identityID
	 * @return  String
	 */
	public static String hideIdentityID(String identityID) {
		if(identityID != null && identityID.length() > 6) {
			identityID = identityID.substring(0, identityID.length() - 6)
					+ "******";
		}
		return identityID;
	}

	/**
	 * 是否规范的邮编
	 * 
	 * @param toCheckStr  toCheckStr
	 * @return  是否规范的邮编
	 */
	public static boolean isPostalCode(String toCheckStr) {
		return isNumeric(toCheckStr) && toCheckStr.length() == 6;
	}

	/**
	 * 邮箱验证
	 * 
	 * @param toCheckStr  toCheckStr
	 * @return  邮箱验证
	 */
	public static boolean isEmail(String toCheckStr) {
		String patternStr = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
		return canMatch(toCheckStr, patternStr);
	}

	/**
	 * 办公电话验证 格式：区号(可选)-主机号-分机号(可选)
	 * 
	 * @param toCheckStr  toCheckStr
	 * @return   办公电话验证 格式：区号(可选)-主机号-分机号(可选)
	 */
	public static boolean isWorkPhone(String toCheckStr) {
		String patternStr = "(^[0-9]{3,4}-[0-9]{7,8}-[0-9]{3,4}$)|(^[0-9]{3,4}-[0-9]{7,8}$)|(^[0-9]{7,8}-[0-9]{3,4}$)|(^[0-9]{7,8}$)";
		return canMatch(toCheckStr, patternStr);
	}

	/**
	 * 常用固定电话验证 格式：区号(可选)-主机号
	 * 
	 * @param toCheckStr  toCheckStr
	 * @return   常用固定电话验证 格式：区号(可选)-主机号
	 */
	public static boolean isPhoneNumber(String toCheckStr) {
		String patternStr = "(^[0-9]{3,4}-[0-9]{7,8}$)|(^[0-9]{7,8}$)";
		return canMatch(toCheckStr, patternStr);
	}

	/**
	 * 是否为规范的手机电话号码 ，以13/15/18开头
	 * 
	 * @param toCheckStr  toCheckStr
	 * @return  是否为规范的手机电话号码 ，以13/15/18开头
	 */
	public static boolean isTelephone(String toCheckStr) {
		String patternStr = "(^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$)";
		return canMatch(toCheckStr, patternStr);
	}

	public static boolean isDateyyMMddHHmmss(String toCheckStr) {
		return canMatch(toCheckStr, "([1-2])([0-9]{3})([0-1])([0-9])([0-3])([0-9])([0-2])([0-9])([0-5])([0-9])([0-5])([0-9])");
	}
}
