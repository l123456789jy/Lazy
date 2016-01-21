/*
 * Copyright (C) 2014 Peng fei Pan <sky@xiaopan.me>
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import java.io.InputStream;

/**
 * 图片处理器
 */
public class ImageProcessor {
    private Bitmap bitmap;

    public ImageProcessor(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    /**
     * 缩放处理
     * @param scaling 缩放比例
     * @return 缩放后的图片
     */
    public Bitmap scale(float scaling) {
        Matrix matrix = new Matrix();
        matrix.postScale(scaling, scaling);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 缩放处理
     * @param newWidth 新的宽度
     * @return Bitmap
     */
    public Bitmap scaleByWidth(int newWidth) {
        return scale((float) newWidth / bitmap.getWidth());
    }

    /**
     * 缩放处理
     * @param newHeight 新的高度
     * @return Bitmap
     */
    public Bitmap scaleByHeight(int newHeight) {
        return scale((float) newHeight / bitmap.getHeight());
    }

    /**
     * 水平翻转处理
     * @param bitmap 原图
     * @return 水平翻转后的图片
     */
    public Bitmap reverseByHorizontal(Bitmap bitmap){
        Matrix matrix = new Matrix();
        matrix.preScale(-1, 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
    }

    /**
     * 垂直翻转处理
     * @param bitmap 原图
     * @return 垂直翻转后的图片
     */
    public Bitmap reverseByVertical(Bitmap bitmap){
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
    }

    /**
     * 将给定资源ID的Drawable转换成Bitmap
     * @param context 上下文
     * @param resId Drawable资源文件的ID
     * @return 新的Bitmap
     */
    public Bitmap drawableToBitmap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    /**
     * 圆角处理
     * @param pixels 角度，度数越大圆角越大
     * @return 转换成圆角后的图片
     */
    public Bitmap roundCorner(float pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());  //创建一个同原图一样大小的矩形，用于把原图绘制到这个矩形上
        RectF rectF = new RectF(rect);  //创建一个精度更高的矩形，用于画出圆角效果
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0); //涂上黑色全透明的底色
        paint.setColor(0xff424242);  //设置画笔的颜色为不透明的灰色
        canvas.drawRoundRect(rectF, pixels, pixels, paint); //用给给定的画笔把给定的矩形以给定的圆角的度数画到画布
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint); //用画笔paint将原图bitmap根据新的矩形重新绘制
        return output;
    }

    /**
     * 倒影处理
     * @param reflectionSpacing 原图与倒影之间的间距
     * @return 加上倒影后的图片
     */
    public Bitmap reflection(int reflectionSpacing, int reflectionHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

		/* 获取倒影图片，并创建一张宽度与原图相同，但高度等于原图的高度加上间距加上倒影的高度的图片，并创建画布。画布分为上中下三部分，上：是原图；中：是原图与倒影的间距；下：是倒影 */
        Bitmap reflectionImage = reverseByVertical(bitmap);//
        Bitmap bitmapWithReflection = Bitmap.createBitmap(width, height + reflectionSpacing + reflectionHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapWithReflection);

		/* 将原图画到画布的上半部分，将倒影画到画布的下半部分，倒影与画布顶部的间距是原图的高度加上原图与倒影之间的间距 */
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.drawBitmap(reflectionImage, 0, height + reflectionSpacing, null);
        reflectionImage.recycle();

		/* 将倒影改成半透明，创建画笔，并设置画笔的渐变从半透明的白色到全透明的白色，然后再倒影上面画半透明效果 */
        Paint paint = new Paint();
        paint.setShader(new LinearGradient(0, bitmap.getHeight(), 0, bitmapWithReflection.getHeight() + reflectionSpacing, 0x70ffffff, 0x00ffffff, Shader.TileMode.CLAMP));
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawRect(0, height+reflectionSpacing, width, bitmapWithReflection.getHeight() + reflectionSpacing, paint);

        return bitmapWithReflection;
    }

    /**
     * 倒影处理
     * @return 加上倒影后的图片
     */
    public Bitmap reflection() {
        return reflection(4, bitmap.getHeight() / 2);
    }

    /**
     * 旋转处理
     * @param angle 旋转角度
     * @param px 旋转中心点在X轴的坐标
     * @param py 旋转中心点在Y轴的坐标
     * @return 旋转后的图片
     */
    public Bitmap rotate(float angle, float px, float py){
        Matrix matrix = new Matrix();
        matrix.postRotate(angle, px, py);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
    }

    /**
     * 旋转后处理
     * @param angle 旋转角度
     * @return 旋转后的图片
     */
    public Bitmap rotate(float angle){
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
    }

    /**
     * 饱和度处理
     * @param saturationValue 新的饱和度值
     * @return 改变了饱和度值之后的图片
     */
    public Bitmap saturation(int saturationValue){
        //计算出符合要求的饱和度值
        float newSaturationValue = saturationValue * 1.0F / 127;
        //创建一个颜色矩阵
        ColorMatrix saturationColorMatrix = new ColorMatrix();
        //设置饱和度值
        saturationColorMatrix.setSaturation(newSaturationValue);
        //创建一个画笔并设置其颜色过滤器
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(saturationColorMatrix));
        //创建一个新的图片并创建画布
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        //将原图使用给定的画笔画到画布上
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return newBitmap;
    }

    /**
     * 亮度处理
     * @param lumValue 新的亮度值
     * @return 改变了亮度值之后的图片
     */
    public Bitmap lum(int lumValue){
        //计算出符合要求的亮度值
        float newlumValue = lumValue * 1.0F / 127;
        //创建一个颜色矩阵
        ColorMatrix lumColorMatrix = new ColorMatrix();
        //设置亮度值
        lumColorMatrix.setScale(newlumValue, newlumValue, newlumValue, 1);
        //创建一个画笔并设置其颜色过滤器
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(lumColorMatrix));
        //创建一个新的图片并创建画布
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        //将原图使用给定的画笔画到画布上
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return newBitmap;
    }

    /**
     * 色相处理
     * @param hueValue 新的色相值
     * @return 改变了色相值之后的图片
     */
    public Bitmap hue(int hueValue){
        //计算出符合要求的色相值
        float newHueValue = (hueValue - 127) * 1.0F / 127 * 180;
        //创建一个颜色矩阵
        ColorMatrix hueColorMatrix = new ColorMatrix();
        // 控制让红色区在色轮上旋转的角度
        hueColorMatrix.setRotate(0, newHueValue);
        // 控制让绿红色区在色轮上旋转的角度
        hueColorMatrix.setRotate(1, newHueValue);
        // 控制让蓝色区在色轮上旋转的角度
        hueColorMatrix.setRotate(2, newHueValue);
        //创建一个画笔并设置其颜色过滤器
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(hueColorMatrix));
        //创建一个新的图片并创建画布
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        //将原图使用给定的画笔画到画布上
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return newBitmap;
    }

    /**
     * 亮度、色相、饱和度处理
     * @param lumValue 亮度值
     * @param hueValue 色相值
     * @param saturationValue 饱和度值
     * @return 亮度、色相、饱和度处理后的图片
     */
    public Bitmap lumAndHueAndSaturation(int lumValue, int hueValue, int saturationValue){
        //计算出符合要求的饱和度值
        float newSaturationValue = saturationValue * 1.0F / 127;
        //计算出符合要求的亮度值
        float newlumValue = lumValue * 1.0F / 127;
        //计算出符合要求的色相值
        float newHueValue = (hueValue - 127) * 1.0F / 127 * 180;

        //创建一个颜色矩阵并设置其饱和度
        ColorMatrix colorMatrix = new ColorMatrix();

        //设置饱和度值
        colorMatrix.setSaturation(newSaturationValue);
        //设置亮度值
        colorMatrix.setScale(newlumValue, newlumValue, newlumValue, 1);
        // 控制让红色区在色轮上旋转的角度
        colorMatrix.setRotate(0, newHueValue);
        // 控制让绿红色区在色轮上旋转的角度
        colorMatrix.setRotate(1, newHueValue);
        // 控制让蓝色区在色轮上旋转的角度
        colorMatrix.setRotate(2, newHueValue);

        //创建一个画笔并设置其颜色过滤器
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        //创建一个新的图片并创建画布
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        //将原图使用给定的画笔画到画布上
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return newBitmap;
    }

    /**
     * 怀旧效果处理
     * @param bitmap 原图
     * @return 怀旧效果处理后的图片
     */
    public Bitmap nostalgic(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        int pixColor = 0;
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < height; i++) {
            for (int k = 0; k < width; k++) {
                pixColor = pixels[width * i + k];
                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);
                newR = (int) (0.393 * pixR + 0.769 * pixG + 0.189 * pixB);
                newG = (int) (0.349 * pixR + 0.686 * pixG + 0.168 * pixB);
                newB = (int) (0.272 * pixR + 0.534 * pixG + 0.131 * pixB);
                int newColor = Color.argb(255, newR > 255 ? 255 : newR, newG > 255 ? 255 : newG, newB > 255 ? 255
                                                                                                            : newB);
                pixels[width * i + k] = newColor;
            }
        }
        newBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return newBitmap;
    }

    /**
     * 模糊效果处理
     * @return 模糊效果处理后的图片
     */
    public Bitmap blur() {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        int pixColor = 0;

        int newR = 0;
        int newG = 0;
        int newB = 0;

        int newColor = 0;

        int[][] colors = new int[9][3];
        for (int i = 1, length = width - 1; i < length; i++) {
            for (int k = 1, len = height - 1; k < len; k++) {
                for (int m = 0; m < 9; m++) {
                    int s = 0;
                    int p = 0;
                    switch (m) {
                        case 0:
                            s = i - 1;
                            p = k - 1;
                            break;
                        case 1:
                            s = i;
                            p = k - 1;
                            break;
                        case 2:
                            s = i + 1;
                            p = k - 1;
                            break;
                        case 3:
                            s = i + 1;
                            p = k;
                            break;
                        case 4:
                            s = i + 1;
                            p = k + 1;
                            break;
                        case 5:
                            s = i;
                            p = k + 1;
                            break;
                        case 6:
                            s = i - 1;
                            p = k + 1;
                            break;
                        case 7:
                            s = i - 1;
                            p = k;
                            break;
                        case 8:
                            s = i;
                            p = k;
                    }
                    pixColor = bitmap.getPixel(s, p);
                    colors[m][0] = Color.red(pixColor);
                    colors[m][1] = Color.green(pixColor);
                    colors[m][2] = Color.blue(pixColor);
                }

                for (int m = 0; m < 9; m++) {
                    newR += colors[m][0];
                    newG += colors[m][1];
                    newB += colors[m][2];
                }

                newR = (int) (newR / 9F);
                newG = (int) (newG / 9F);
                newB = (int) (newB / 9F);

                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));

                newColor = Color.argb(255, newR, newG, newB);
                newBitmap.setPixel(i, k, newColor);

                newR = 0;
                newG = 0;
                newB = 0;
            }
        }
        return newBitmap;
    }

    /**
     * 柔化效果处理
     * @return 柔化效果处理后的图片
     */
    public Bitmap soften() {
        // 高斯矩阵
        int[] gauss = new int[] { 1, 2, 1, 2, 4, 2, 1, 2, 1 };

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        int pixR = 0;
        int pixG = 0;
        int pixB = 0;

        int pixColor = 0;

        int newR = 0;
        int newG = 0;
        int newB = 0;

        int delta = 16; // 值越小图片会越亮，越大则越暗

        int idx = 0;
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                idx = 0;
                for (int m = -1; m <= 1; m++) {
                    for (int n = -1; n <= 1; n++) {
                        pixColor = pixels[(i + m) * width + k + n];
                        pixR = Color.red(pixColor);
                        pixG = Color.green(pixColor);
                        pixB = Color.blue(pixColor);

                        newR = newR + (int) (pixR * gauss[idx]);
                        newG = newG + (int) (pixG * gauss[idx]);
                        newB = newB + (int) (pixB * gauss[idx]);
                        idx++;
                    }
                }

                newR /= delta;
                newG /= delta;
                newB /= delta;

                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));

                pixels[i * width + k] = Color.argb(255, newR, newG, newB);

                newR = 0;
                newG = 0;
                newB = 0;
            }
        }

        newBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return newBitmap;
    }

    /**
     * 光照效果处理
     * @param centerX 光源在X轴的位置
     * @param centerY 光源在Y轴的位置
     * @return 光照效果处理后的图片
     */
    public Bitmap sunshine(int centerX, int centerY) {
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        int pixR = 0;
        int pixG = 0;
        int pixB = 0;

        int pixColor = 0;

        int newR = 0;
        int newG = 0;
        int newB = 0;
        int radius = Math.min(centerX, centerY);

        final float strength = 150F; // 光照强度 100~150
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int pos = 0;
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                pos = i * width + k;
                pixColor = pixels[pos];

                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);

                newR = pixR;
                newG = pixG;
                newB = pixB;

                // 计算当前点到光照中心的距离，平面座标系中求两点之间的距离
                int distance = (int) (
                        Math.pow((centerY - i), 2) + Math.pow(centerX - k, 2));
                if (distance < radius * radius) {
                    // 按照距离大小计算增加的光照值
                    int result = (int) (strength * (1.0 - Math.sqrt(distance) / radius));
                    newR = pixR + result;
                    newG = pixG + result;
                    newB = pixB + result;
                }

                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));

                pixels[pos] = Color.argb(255, newR, newG, newB);
            }
        }

        newBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return newBitmap;
    }

    /**
     * 底片效果处理
     * @return 底片效果处理后的图片
     */
    public Bitmap film() {
        // RGBA的最大值
        final int MAX_VALUE = 255;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        int pixR = 0;
        int pixG = 0;
        int pixB = 0;

        int pixColor = 0;

        int newR = 0;
        int newG = 0;
        int newB = 0;

        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int pos = 0;
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                pos = i * width + k;
                pixColor = pixels[pos];

                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);

                newR = MAX_VALUE - pixR;
                newG = MAX_VALUE - pixG;
                newB = MAX_VALUE - pixB;

                newR = Math.min(MAX_VALUE, Math.max(0, newR));
                newG = Math.min(MAX_VALUE, Math.max(0, newG));
                newB = Math.min(MAX_VALUE, Math.max(0, newB));

                pixels[pos] = Color.argb(MAX_VALUE, newR, newG, newB);
            }
        }

        newBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return newBitmap;
    }

    /**
     * 锐化效果处理
     * @return 锐化效果处理后的图片
     */
    public Bitmap sharpen() {
        // 拉普拉斯矩阵
        int[] laplacian = new int[] { -1, -1, -1, -1, 9, -1, -1, -1, -1 };

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        int pixR = 0;
        int pixG = 0;
        int pixB = 0;

        int pixColor = 0;

        int newR = 0;
        int newG = 0;
        int newB = 0;

        int idx = 0;
        float alpha = 0.3F;
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                idx = 0;
                for (int m = -1; m <= 1; m++) {
                    for (int n = -1; n <= 1; n++) {
                        pixColor = pixels[(i + n) * width + k + m];
                        pixR = Color.red(pixColor);
                        pixG = Color.green(pixColor);
                        pixB = Color.blue(pixColor);

                        newR = newR + (int) (pixR * laplacian[idx] * alpha);
                        newG = newG + (int) (pixG * laplacian[idx] * alpha);
                        newB = newB + (int) (pixB * laplacian[idx] * alpha);
                        idx++;
                    }
                }

                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));

                pixels[i * width + k] = Color.argb(255, newR, newG, newB);
                newR = 0;
                newG = 0;
                newB = 0;
            }
        }

        newBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return newBitmap;
    }

    /**
     * 浮雕效果处理
     * @return 浮雕效果处理后的图片
     */
    public Bitmap emboss() {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        int pixR = 0;
        int pixG = 0;
        int pixB = 0;

        int pixColor = 0;

        int newR = 0;
        int newG = 0;
        int newB = 0;

        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int pos = 0;
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                pos = i * width + k;
                pixColor = pixels[pos];

                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);

                pixColor = pixels[pos + 1];
                newR = Color.red(pixColor) - pixR + 127;
                newG = Color.green(pixColor) - pixG + 127;
                newB = Color.blue(pixColor) - pixB + 127;

                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));

                pixels[pos] = Color.argb(255, newR, newG, newB);
            }
        }

        newBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return newBitmap;
    }
}
