package com.github.lazylibrary.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

/**
 * 项目名称：Lazy
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2017/9/22 9:35
 * 修改人：Administrator
 * 修改时间：2017/9/22 9:35
 * 修改备注：
 * 联系方式：906514731@qq.com
 * 使用nio以提高性能
 */
public class NioFileUtiles {

  public static void writeToFile(InputStream dataIns, File target) throws IOException {
    final int BUFFER = 1024;
    BufferedOutputStream bos = new BufferedOutputStream(
        new FileOutputStream(target));
    int count;
    byte data[] = new byte[BUFFER];
    while ((count = dataIns.read(data, 0, BUFFER)) != -1) {
      bos.write(data, 0, count);
    }
    bos.close();
  }

  public static void writeToFile1(InputStream dataIns, File target) throws IOException {
    FileOutputStream fo = null;
    ReadableByteChannel src = null;
    FileChannel out = null;
    try {
      int len = dataIns.available();
      src = Channels.newChannel(dataIns);
      fo = new FileOutputStream(target);
      out = fo.getChannel();
      out.transferFrom(src, 0, len);
    } finally {
      if (fo != null) {
        fo.close();
      }
      if (src != null) {
        src.close();
      }
      if (out != null) {
        out.close();
      }
    }
  }

  public static void writeToFile(byte[] data, File target) throws IOException {
    FileOutputStream fo = null;
    ReadableByteChannel src = null;
    FileChannel out = null;
    try {
      src = Channels.newChannel(new ByteArrayInputStream(data));
      fo = new FileOutputStream(target);
      out = fo.getChannel();
      out.transferFrom(src, 0, data.length);
    } finally {
      if (fo != null) {
        fo.close();
      }
      if (src != null) {
        src.close();
      }
      if (out != null) {
        out.close();
      }
    }
  }

  /**
   * 复制文件
   *
   * @param source - 源文件
   * @param target - 目标文件
   */
  public static void copyFile(File source, File target) {

    FileInputStream fi = null;
    FileOutputStream fo = null;

    FileChannel in = null;

    FileChannel out = null;

    try {
      fi = new FileInputStream(source);

      fo = new FileOutputStream(target);

      in = fi.getChannel();// 得到对应的文件通道

      out = fo.getChannel();// 得到对应的文件通道

      in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        fi.close();

        in.close();

        fo.close();

        out.close();

      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
