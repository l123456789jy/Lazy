package com.github.lazylibrary.view;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 *
 * 类描述：修改7.0以下popwindows位置显示错误bug
 * 创建人：Lazy
 * 创建时间：2017/5/12 15:43
 * 联系方式：906514731@qq.com
 */


public class Repair7PopupWindow extends PopupWindow {

  private Context mContext;
  private WindowManager mWindowManager;

  public Repair7PopupWindow(View contentView, int width, int height, boolean focusable) {
    if (contentView != null) {
      mContext = contentView.getContext();
      mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
    }

    setContentView(contentView);
    setWidth(width);
    setHeight(height);
    setFocusable(focusable);
  }


  @Override
  public void showAsDropDown(View anchor) {
    if (Build.VERSION.SDK_INT >= 24) {
      Rect rect = new Rect();
      anchor.getGlobalVisibleRect(rect);
      int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
      setHeight(h);
    }
    super.showAsDropDown(anchor);
  }
}
