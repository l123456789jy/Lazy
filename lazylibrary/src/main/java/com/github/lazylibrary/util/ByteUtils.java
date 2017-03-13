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

/**
 * <h2>字节工具类，提供一些有关字节的便捷方法</h2>
 * 
 * <br>&nbsp;&nbsp;&nbsp;&nbsp;(01)、位移加密：static void byteJiaMi(byte[] bytes)
 * <br>&nbsp;&nbsp;&nbsp;&nbsp;(02)、从bytes上截取一段：static byte[] cutOut(byte[] bytes, int off, int length)
 */
public class ByteUtils {
	/**
	 * (01)、位移加密
	 * @param bytes
	 */
	public static void byteJiaMi(byte[] bytes){
		for (int w = 0; w < bytes.length; w++){
			int a = bytes[w];
			a = ~a;
			bytes[w] = (byte)a;
		}
	}


	/**
	 * 字符串转数组
	 * @param str
	 * @return
	 */
	public static byte[] hexStrToByteArray(String str)
	{
		if (str == null) {
			return null;
		}
		if (str.length() == 0) {
			return new byte[0];
		}
		byte[] byteArray = new byte[str.length() / 2];
		for (int i = 0; i < byteArray.length; i++){
			String subStr = str.substring(2 * i, 2 * i + 2);
			byteArray[i] = ((byte)Integer.parseInt(subStr, 16));
		}
		return byteArray;
	}



	/**
	 * (02)、从bytes上截取一段
	 * @param bytes 母体
	 * @param off 起始
	 * @param length 个数
	 * @return byte[]
	 */
	public static byte[] cutOut(byte[] bytes, int off, int length){
		byte[] bytess = new byte[length];
		System.arraycopy(bytes, off, bytess, 0, length);
		return bytess;
	}
	
	/**
	 * 将字节转换为二进制字符串
	 * @param bytes 字节数组
	 * @return 二进制字符串
	 */
	public static String byteToBit(byte... bytes){
		StringBuffer sb = new StringBuffer();
		int z, len;
		String str;
		for(int w = 0; w < bytes.length ; w++){
			z = bytes[w]; 
			z |= 256;
			str = Integer.toBinaryString(z);
			len = str.length(); 
			sb.append(str.substring(len-8, len));
		}
		return sb.toString();
	}
	
	
	/**
	 * 字节数组转换成16进制字符串
	 * @param raw
	 * @return
	 */
	public static String getHex(byte [] raw ) {
		String HEXES = "0123456789ABCDEF";
	    if ( raw == null ) {
	      return null;
	    }
	    final StringBuilder hex = new StringBuilder( 2 * raw.length );
	    for ( final byte b : raw ) {
	      hex.append(HEXES.charAt((b & 0xF0) >> 4))
	         .append(HEXES.charAt((b & 0x0F)));
	    }
	    return hex.toString();
	}
	
	/**
	 * 将一个short转换成字节数组
	 * @param sh short
	 * @return 字节数组
	 */
	public static byte[] valueOf(short sh){
		byte[] shortBuf = new byte[2];
		for(int i=0;i<2;i++) {
			int offset = (shortBuf.length - 1 -i)*8;
			shortBuf[i] = (byte)((sh>>>offset)&0xff);
		}
		return shortBuf;
	}
	
	/**
	 * 将一个int转换成字节数组
	 * @param in int
	 * @return 字节数组
	 */
	public static byte[] valueOf(int in){
		byte[] b = new byte[4];
	    for (int i = 0; i < 4; i++) {
	    	int offset = (b.length - 1 - i) * 8;
	    	b[i] = (byte) ((in >>> offset) & 0xFF);
	    }
	    return b;
	}


	/**
	 * 从一个byte[]数组中截取一部分
	 * @param src
	 * @param begin
	 * @param count
	 * @return
	 */
	public static byte[] subBytes ( byte[] src, int begin, int count){
		byte[] bs = new byte[count];
		for (int i = begin; i < begin + count; i++) {
			bs[i - begin] = src[i];
		}
		return bs;
	}
}
