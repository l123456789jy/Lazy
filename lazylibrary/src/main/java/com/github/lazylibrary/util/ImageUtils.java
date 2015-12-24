package com.github.lazylibrary.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * ImageUtils
 * <ul>
 * convert between Bitmap, byte array, Drawable
 * <li>{@link #bitmapToByte(Bitmap)}</li>
 * <li>{@link #bitmapToDrawable(Bitmap)}</li>
 * <li>{@link #byteToBitmap(byte[])}</li>
 * <li>{@link #byteToDrawable(byte[])}</li>
 * <li>{@link #drawableToBitmap(Drawable)}</li>
 * <li>{@link #drawableToByte(Drawable)}</li>
 * </ul>
 * <ul>
 * get image
 * <li>{@link #getInputStreamFromUrl(String, int, Map)}</li>
 * <li>{@link #getBitmapFromUrl(String, int)}</li>
 * <li>{@link #getDrawableFromUrl(String, int)}</li>
 * </ul>
 * <ul>
 * scale image
 * <li>{@link #scaleImageTo(Bitmap, int, int)}</li>
 * <li>{@link #scaleImage(Bitmap, float, float)}</li>
 * </ul>
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2012-6-27
 */
public class ImageUtils {

    private ImageUtils() {
        throw new AssertionError();
    }


    /**
     * convert Bitmap to byte array
     */
    public static byte[] bitmapToByte(Bitmap b) {
        if (b == null) {
            return null;
        }

        ByteArrayOutputStream o = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, o);
        return o.toByteArray();
    }


    /**
     * convert byte array to Bitmap
     */
    public static Bitmap byteToBitmap(byte[] b) {
        return (b == null || b.length == 0)
               ? null
               : BitmapFactory.decodeByteArray(b, 0, b.length);
    }


    /**
     * convert Drawable to Bitmap
     */
    public static Bitmap drawableToBitmap(Drawable d) {
        return d == null ? null : ((BitmapDrawable) d).getBitmap();
    }


    /**
     * convert Bitmap to Drawable
     */
    public static Drawable bitmapToDrawable(Bitmap b) {
        return b == null ? null : new BitmapDrawable(b);
    }


    /**
     * convert Drawable to byte array
     */
    public static byte[] drawableToByte(Drawable d) {
        return bitmapToByte(drawableToBitmap(d));
    }


    /**
     * convert byte array to Drawable
     */
    public static Drawable byteToDrawable(byte[] b) {
        return bitmapToDrawable(byteToBitmap(b));
    }


    /**
     * get input stream from network by imageurl, you need to close inputStream
     * yourself
     *
     * @see ImageUtils# getInputStreamFromUrl(String, int, boolean)
     */
    public static InputStream getInputStreamFromUrl(String imageUrl, int readTimeOutMillis, Map<String, String> requestProperties) {
        return getInputStreamFromUrl(imageUrl, readTimeOutMillis, null);
    }


    /**
     * get drawable by imageUrl
     *
     * @see ImageUtils# getDrawableFromUrl(String, int, boolean)
     */
    public static Drawable getDrawableFromUrl(String imageUrl, int readTimeOutMillis) {
        return getDrawableFromUrl(imageUrl, readTimeOutMillis, null);
    }


    /**
     * get drawable by imageUrl
     *
     * @param readTimeOutMillis read time out, if less than 0, not set, in
     * mills
     * @param requestProperties http request properties
     */
    public static Drawable getDrawableFromUrl(String imageUrl, int readTimeOutMillis, Map<String, String> requestProperties) {
        InputStream stream = getInputStreamFromUrl(imageUrl, readTimeOutMillis,
                requestProperties);
        Drawable d = Drawable.createFromStream(stream, "src");
        IOUtils.close(stream);
        return d;
    }


    /**
     * get Bitmap by imageUrl
     *
     * @see ImageUtils# getBitmapFromUrl(String, int, boolean)
     */
    public static Bitmap getBitmapFromUrl(String imageUrl, int readTimeOut) {
        return getBitmapFromUrl(imageUrl, readTimeOut, null);
    }


    /**
     * get Bitmap by imageUrl
     *
     * @param requestProperties http request properties
     */
    public static Bitmap getBitmapFromUrl(String imageUrl, int readTimeOut, Map<String, String> requestProperties) {
        InputStream stream = getInputStreamFromUrl(imageUrl, readTimeOut,
                requestProperties);
        Bitmap b = BitmapFactory.decodeStream(stream);
        IOUtils.close(stream);
        return b;
    }


    /**
     * scale image
     */
    public static Bitmap scaleImageTo(Bitmap org, int newWidth, int newHeight) {
        return scaleImage(org, (float) newWidth / org.getWidth(),
                (float) newHeight / org.getHeight());
    }


    /**
     * scale image
     *
     * @param scaleWidth sacle of width
     * @param scaleHeight scale of height
     */
    public static Bitmap scaleImage(Bitmap org, float scaleWidth, float scaleHeight) {
        if (org == null) {
            return null;
        }

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(org, 0, 0, org.getWidth(), org.getHeight(),
                matrix, true);
    }


    /**
     * 根据图片，获取旋转角度
     */
    public static int readPictureDegree(String path) {

        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }


    /**
     * 根据给定的旋转角度，返回一张图片
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {

        if (bitmap == null) return null;

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        // Setting post rotate to 90
        Matrix mtx = new Matrix();
        mtx.postRotate(rotate);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }


    /**
     * 生成图片的倒影
     *
     * @param w 最终图片的宽
     * @param h 最终图片的高
     * @param baseline 原图的放置高度，baseline < h
     * @param bitmap 原图，若大小大于(w,baseline)，则从左上角取(w,baseline)，若小于，则居中
     * @param reflectionGap 原图和倒影的分割线高度
     * @return 包含原图和倒影
     */
    public static Bitmap createReflectionImageWithOrigin(int w, int h, int baseline, Bitmap bitmap, int reflectionGap) {
        if (baseline >= h) {
            return imageScale(bitmap, w, h);
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 处理居中
        float left = (w - width) / 2;
        float top = baseline - height;
        if (left < 0) {
            left = 0;
            width = w;
        }
        if (top < 0) {
            top = 0;
            height = baseline;
        }
        // 生成倒影
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);
        int reflectionHeight = h - baseline - reflectionGap;
        if (reflectionHeight > height) {
            reflectionHeight = height;
        }
        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0,
                height - reflectionHeight, width, reflectionHeight, matrix,
                false);
        // 生成最终图
        Bitmap bitmapWithReflection = Bitmap.createBitmap(w, h,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, left, top, null);// 画原图
        Paint deafalutPaint = new Paint();
        canvas.drawRect(left, baseline, width + left, baseline + reflectionGap,
                deafalutPaint);// 画分割线
        canvas.drawBitmap(reflectionImage, left, baseline + reflectionGap,
                null);// 画倒影
        // 加透明
        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(left, baseline, left, h,
                0x70ffffff, 0x00ffffff, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        // Draw a rectangle using the paint with our linear gradient
        canvas.drawRect(left, baseline, left + width, h, paint);
        return bitmapWithReflection;
    }


    /**
     * 调整图片大小
     *
     * @param bitmap 源
     * @param dst_w 输出宽度
     * @param dst_h 输出高度
     */
    public static Bitmap imageScale(Bitmap bitmap, int dst_w, int dst_h) {
        int src_w = bitmap.getWidth();
        int src_h = bitmap.getHeight();
        float scale_w = ((float) dst_w) / src_w;
        float scale_h = ((float) dst_h) / src_h;
        Matrix matrix = new Matrix();
        matrix.postScale(scale_w, scale_h);
        Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, src_w, src_h, matrix,
                true);
        return dstbmp;
    }


    /**
     * // 压缩图片尺寸
     */
    public static Bitmap compressBySize(String pathName, int targetWidth, int targetHeight) {

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;// 不去真的解析图片，只是获取图片的头部信息，包含宽高等；
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, opts);
        // 得到图片的宽度、高度；
        float imgWidth = opts.outWidth;
        float imgHeight = opts.outHeight;
        // 分别计算图片宽度、高度与目标宽度、高度的比例；取大于等于该比例的最小整数；
        int widthRatio = (int) Math.ceil(imgWidth / (float) targetWidth);
        int heightRatio = (int) Math.ceil(imgHeight / (float) targetHeight);
        opts.inSampleSize = 1;
        if (widthRatio > 1 || widthRatio > 1) {
            if (widthRatio > heightRatio) {
                opts.inSampleSize = widthRatio;
            }
            else {
                opts.inSampleSize = heightRatio;
            }
        }
        // 设置好缩放比例后，加载图片进内容；
        opts.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(pathName, opts);
        return bitmap;
    }


    /**
     * // 存储进SD卡
     *
     * @throws Exception
     */
    public static void saveFile(Bitmap bm, String fileName, boolean IsJpg)

            throws Exception {
        File dirFile = new File(fileName);
        // 检测图片是否存在
        if (dirFile.exists()) {
            dirFile.delete(); // 删除原图片
        }
        File myCaptureFile = new File(fileName);
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(myCaptureFile));
        if (IsJpg) {
            // 100表示不进行压缩，70表示压缩率为30%
            bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        }
        else {
            // 100表示不进行压缩，70表示压缩率为30%
            bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
        }
        bos.flush();
        bos.close();
    }



}
