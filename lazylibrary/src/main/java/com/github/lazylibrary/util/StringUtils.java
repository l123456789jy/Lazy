package com.github.lazylibrary.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <h2>字符串工具类，提供一些字符串相关的便捷方法</h2>
 */
public class StringUtils {

    private StringUtils() {
        throw new AssertionError();
    }


    /**
     * is null or its length is 0 or it is made by space
     *
     * <pre>
     * isBlank(null) = true;
     * isBlank(&quot;&quot;) = true;
     * isBlank(&quot;  &quot;) = true;
     * isBlank(&quot;a&quot;) = false;
     * isBlank(&quot;a &quot;) = false;
     * isBlank(&quot; a&quot;) = false;
     * isBlank(&quot;a b&quot;) = false;
     * </pre>
     *
     * @param str str
     * @return if string is null or its size is 0 or it is made by space, return
     * true, else return false.
     */
    public static boolean isBlank(String str) {

        return (str == null || str.trim().length() == 0);
    }


    /**
     * is null or its length is 0
     *
     * <pre>
     * isEmpty(null) = true;
     * isEmpty(&quot;&quot;) = true;
     * isEmpty(&quot;  &quot;) = false;
     * </pre>
     *
     * @param str str
     * @return if string is null or its size is 0, return true, else return
     * false.
     */
    public static boolean isEmpty(CharSequence str) {

        return (str == null || str.length() == 0);
    }


    /**
     * get length of CharSequence
     *
     * <pre>
     * length(null) = 0;
     * length(\"\") = 0;
     * length(\"abc\") = 3;
     * </pre>
     *
     * @param str str
     * @return if str is null or empty, return 0, else return {@link
     * CharSequence#length()}.
     */
    public static int length(CharSequence str) {

        return str == null ? 0 : str.length();
    }


    /**
     * null Object to empty string
     *
     * <pre>
     * nullStrToEmpty(null) = &quot;&quot;;
     * nullStrToEmpty(&quot;&quot;) = &quot;&quot;;
     * nullStrToEmpty(&quot;aa&quot;) = &quot;aa&quot;;
     * </pre>
     *
     * @param str str
     * @return String
     */
    public static String nullStrToEmpty(Object str) {

        return (str == null
                ? ""
                : (str instanceof String ? (String) str : str.toString()));
    }


    /**
     * @param str str
     * @return String
     */
    public static String capitalizeFirstLetter(String str) {

        if (isEmpty(str)) {
            return str;
        }

        char c = str.charAt(0);
        return (!Character.isLetter(c) || Character.isUpperCase(c))
               ? str
               : new StringBuilder(str.length()).append(
                       Character.toUpperCase(c))
                                                .append(str.substring(1))
                                                .toString();
    }


    /**
     * encoded in utf-8
     *
     * @param str 字符串
     * @return 返回一个utf8的字符串
     */
    public static String utf8Encode(String str) {

        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(
                        "UnsupportedEncodingException occurred. ", e);
            }
        }
        return str;
    }


    /**
     * @param href 字符串
     * @return 返回一个html
     */
    public static String getHrefInnerHtml(String href) {

        if (isEmpty(href)) {
            return "";
        }

        String hrefReg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*";
        Pattern hrefPattern = Pattern.compile(hrefReg,
                Pattern.CASE_INSENSITIVE);
        Matcher hrefMatcher = hrefPattern.matcher(href);
        if (hrefMatcher.matches()) {
            return hrefMatcher.group(1);
        }
        return href;
    }


    /**
     * @param source 字符串
     * @return 返回htmL到字符串
     */
    public static String htmlEscapeCharsToString(String source) {

        return StringUtils.isEmpty(source)
               ? source
               : source.replaceAll("&lt;", "<")
                       .replaceAll("&gt;", ">")
                       .replaceAll("&amp;", "&")
                       .replaceAll("&quot;", "\"");
    }


    /**
     * @param s str
     * @return String
     */
    public static String fullWidthToHalfWidth(String s) {

        if (isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == 12288) {
                source[i] = ' ';
                // } else if (source[i] == 12290) {
                // source[i] = '.';
            }
            else if (source[i] >= 65281 && source[i] <= 65374) {
                source[i] = (char) (source[i] - 65248);
            }
            else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }


    /**
     * @param s 字符串
     * @return 返回的数值
     */
    public static String halfWidthToFullWidth(String s) {

        if (isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == ' ') {
                source[i] = (char) 12288;
                // } else if (source[i] == '.') {
                // source[i] = (char)12290;
            }
            else if (source[i] >= 33 && source[i] <= 126) {
                source[i] = (char) (source[i] + 65248);
            }
            else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }


    /**
     * @param str 资源
     * @return 特殊字符串切换
     */

    public static String replaceBlanktihuan(String str) {

        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }


    /**
     * 判断给定的字符串是否为null或者是空的
     *
     * @param string 给定的字符串
     */
    public static boolean isEmpty(String string) {
        return string == null || "".equals(string.trim());
    }


    /**
     * 判断给定的字符串是否不为null且不为空
     *
     * @param string 给定的字符串
     */
    public static boolean isNotEmpty(String string) {
        return !isEmpty(string);
    }


    /**
     * 判断给定的字符串数组中的所有字符串是否都为null或者是空的
     *
     * @param strings 给定的字符串
     */
    public static boolean isEmpty(String... strings) {
        boolean result = true;
        for (String string : strings) {
            if (isNotEmpty(string)) {
                result = false;
                break;
            }
        }
        return result;
    }


    /**
     * 判断给定的字符串数组中是否全部都不为null且不为空
     *
     * @param strings 给定的字符串数组
     * @return 是否全部都不为null且不为空
     */
    public static boolean isNotEmpty(String... strings) {
        boolean result = true;
        for (String string : strings) {
            if (isEmpty(string)) {
                result = false;
                break;
            }
        }
        return result;
    }


    /**
     * 如果字符串是null或者空就返回""
     */
    public static String filterEmpty(String string) {
        return StringUtils.isNotEmpty(string) ? string : "";
    }


    /**
     * 在给定的字符串中，用新的字符替换所有旧的字符
     *
     * @param string 给定的字符串
     * @param oldchar 旧的字符
     * @param newchar 新的字符
     * @return 替换后的字符串
     */
    public static String replace(String string, char oldchar, char newchar) {
        char chars[] = string.toCharArray();
        for (int w = 0; w < chars.length; w++) {
            if (chars[w] == oldchar) {
                chars[w] = newchar;
                break;
            }
        }
        return new String(chars);
    }


    /**
     * 把给定的字符串用给定的字符分割
     *
     * @param string 给定的字符串
     * @param ch 给定的字符
     * @return 分割后的字符串数组
     */
    public static String[] split(String string, char ch) {
        ArrayList<String> stringList = new ArrayList<String>();
        char chars[] = string.toCharArray();
        int nextStart = 0;
        for (int w = 0; w < chars.length; w++) {
            if (ch == chars[w]) {
                stringList.add(new String(chars, nextStart, w - nextStart));
                nextStart = w + 1;
                if (nextStart ==
                        chars.length) {    //当最后一位是分割符的话，就再添加一个空的字符串到分割数组中去
                    stringList.add("");
                }
            }
        }
        if (nextStart <
                chars.length) {    //如果最后一位不是分隔符的话，就将最后一个分割符到最后一个字符中间的左右字符串作为一个字符串添加到分割数组中去
            stringList.add(new String(chars, nextStart,
                    chars.length - 1 - nextStart + 1));
        }
        return stringList.toArray(new String[stringList.size()]);
    }


    /**
     * 计算给定的字符串的长度，计算规则是：一个汉字的长度为2，一个字符的长度为1
     *
     * @param string 给定的字符串
     * @return 长度
     */
    public static int countLength(String string) {
        int length = 0;
        char[] chars = string.toCharArray();
        for (int w = 0; w < string.length(); w++) {
            char ch = chars[w];
            if (ch >= '\u0391' && ch <= '\uFFE5') {
                length++;
                length++;
            }
            else {
                length++;
            }
        }
        return length;
    }


    private static char[] getChars(char[] chars, int startIndex) {
        int endIndex = startIndex + 1;
        //如果第一个是数字
        if (Character.isDigit(chars[startIndex])) {
            //如果下一个是数字
            while (endIndex < chars.length &&
                    Character.isDigit(chars[endIndex])) {
                endIndex++;
            }
        }
        char[] resultChars = new char[endIndex - startIndex];
        System.arraycopy(chars, startIndex, resultChars, 0, resultChars.length);
        return resultChars;
    }


    /**
     * 是否全是数字
     */
    public static boolean isAllDigital(char[] chars) {
        boolean result = true;
        for (int w = 0; w < chars.length; w++) {
            if (!Character.isDigit(chars[w])) {
                result = false;
                break;
            }
        }
        return result;
    }




    /**
     * 删除给定字符串中所有的旧的字符
     *
     * @param string 源字符串
     * @param ch 要删除的字符
     * @return 删除后的字符串
     */
    public static String removeChar(String string, char ch) {
        StringBuffer sb = new StringBuffer();
        for (char cha : string.toCharArray()) {
            if (cha != '-') {
                sb.append(cha);
            }
        }
        return sb.toString();
    }


    /**
     * 删除给定字符串中给定位置处的字符
     *
     * @param string 给定字符串
     * @param index 给定位置
     */
    public static String removeChar(String string, int index) {
        String result = null;
        char[] chars = string.toCharArray();
        if (index == 0) {
            result = new String(chars, 1, chars.length - 1);
        }
        else if (index == chars.length - 1) {
            result = new String(chars, 0, chars.length - 1);
        }
        else {
            result = new String(chars, 0, index) +
                    new String(chars, index + 1, chars.length - index);
            ;
        }
        return result;
    }


    /**
     * 删除给定字符串中给定位置处的字符
     *
     * @param string 给定字符串
     * @param index 给定位置
     * @param ch 如果同给定位置处的字符相同，则将给定位置处的字符删除
     */
    public static String removeChar(String string, int index, char ch) {
        String result = null;
        char[] chars = string.toCharArray();
        if (chars.length > 0 && chars[index] == ch) {
            if (index == 0) {
                result = new String(chars, 1, chars.length - 1);
            }
            else if (index == chars.length - 1) {
                result = new String(chars, 0, chars.length - 1);
            }
            else {
                result = new String(chars, 0, index) +
                        new String(chars, index + 1, chars.length - index);
                ;
            }
        }
        else {
            result = string;
        }
        return result;
    }


    /**
     * 对给定的字符串进行空白过滤
     *
     * @param string 给定的字符串
     * @return 如果给定的字符串是一个空白字符串，那么返回null；否则返回本身。
     */
    public static String filterBlank(String string) {
        if ("".equals(string)) {
            return null;
        }
        else {
            return string;
        }
    }


    /**
     * 将给定字符串中给定的区域的字符转换成小写
     *
     * @param str 给定字符串中
     * @param beginIndex 开始索引（包括）
     * @param endIndex 结束索引（不包括）
     * @return 新的字符串
     */
    public static String toLowerCase(String str, int beginIndex, int endIndex) {
        return str.replaceFirst(str.substring(beginIndex, endIndex),
                str.substring(beginIndex, endIndex)
                   .toLowerCase(Locale.getDefault()));
    }


    /**
     * 将给定字符串中给定的区域的字符转换成大写
     *
     * @param str 给定字符串中
     * @param beginIndex 开始索引（包括）
     * @param endIndex 结束索引（不包括）
     * @return 新的字符串
     */
    public static String toUpperCase(String str, int beginIndex, int endIndex) {
        return str.replaceFirst(str.substring(beginIndex, endIndex),
                str.substring(beginIndex, endIndex)
                   .toUpperCase(Locale.getDefault()));
    }


    /**
     * 将给定字符串的首字母转为小写
     *
     * @param str 给定字符串
     * @return 新的字符串
     */
    public static String firstLetterToLowerCase(String str) {
        return toLowerCase(str, 0, 1);
    }


    /**
     * 将给定字符串的首字母转为大写
     *
     * @param str 给定字符串
     * @return 新的字符串
     */
    public static String firstLetterToUpperCase(String str) {
        return toUpperCase(str, 0, 1);
    }


    /**
     * 将给定的字符串MD5加密
     *
     * @param string 给定的字符串
     * @return MD5加密后生成的字符串
     */
    public static String MD5(String string) {
        String result = null;
        try {
            char[] charArray = string.toCharArray();
            byte[] byteArray = new byte[charArray.length];
            for (int i = 0; i < charArray.length; i++) {
                byteArray[i] = (byte) charArray[i];
            }

            StringBuffer hexValue = new StringBuffer();
            byte[] md5Bytes = MessageDigest.getInstance("MD5")
                                           .digest(byteArray);
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }

            result = hexValue.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 判断给定的字符串是否以一个特定的字符串开头，忽略大小写
     *
     * @param sourceString 给定的字符串
     * @param newString 一个特定的字符串
     */
    public static boolean startsWithIgnoreCase(String sourceString, String newString) {
        int newLength = newString.length();
        int sourceLength = sourceString.length();
        if (newLength == sourceLength) {
            return newString.equalsIgnoreCase(sourceString);
        }
        else if (newLength < sourceLength) {
            char[] newChars = new char[newLength];
            sourceString.getChars(0, newLength, newChars, 0);
            return newString.equalsIgnoreCase(String.valueOf(newChars));
        }
        else {
            return false;
        }
    }


    /**
     * 判断给定的字符串是否以一个特定的字符串结尾，忽略大小写
     *
     * @param sourceString 给定的字符串
     * @param newString 一个特定的字符串
     */
    public static boolean endsWithIgnoreCase(String sourceString, String newString) {
        int newLength = newString.length();
        int sourceLength = sourceString.length();
        if (newLength == sourceLength) {
            return newString.equalsIgnoreCase(sourceString);
        }
        else if (newLength < sourceLength) {
            char[] newChars = new char[newLength];
            sourceString.getChars(sourceLength - newLength, sourceLength,
                    newChars, 0);
            return newString.equalsIgnoreCase(String.valueOf(newChars));
        }
        else {
            return false;
        }
    }


    /**
     * 检查字符串长度，如果字符串的长度超过maxLength，就截取前maxLength个字符串并在末尾拼上appendString
     */
    public static String checkLength(String string, int maxLength, String appendString) {
        if (string.length() > maxLength) {
            string = string.substring(0, maxLength);
            if (appendString != null) {
                string += appendString;
            }
        }
        return string;
    }


    /**
     * 检查字符串长度，如果字符串的长度超过maxLength，就截取前maxLength个字符串并在末尾拼上…
     */
    public static String checkLength(String string, int maxLength) {
        return checkLength(string, maxLength, "…");
    }
    
    
     /**
     * 删除Html标签
     *
     * @param inputString
     * @return
     */
    public static String htmlRemoveTag(String inputString) {
        if (inputString == null)
            return null;
        String htmlStr = inputString; // 含html标签的字符串
        String textStr = "";
        java.util.regex.Pattern p_script;
        java.util.regex.Matcher m_script;
        java.util.regex.Pattern p_style;
        java.util.regex.Matcher m_style;
        java.util.regex.Pattern p_html;
        java.util.regex.Matcher m_html;
        try {
            //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
            //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
            String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签
            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签
            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签
            textStr = htmlStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return textStr;// 返回文本字符串
    }
    
    
}

