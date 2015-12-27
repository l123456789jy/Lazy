package com.github.lazylibrary.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class ViewUtils {

    private static final String CLASS_NAME_GRID_VIEW
            = "android.widget.GridView";
    private static final String FIELD_NAME_VERTICAL_SPACING
            = "mVerticalSpacing";


    private ViewUtils() {
        throw new AssertionError();
    }


    /**
     * get ListView height according to every children
     *
     * @param view view
     * @return int
     */
    public static int getListViewHeightBasedOnChildren(ListView view) {
        int height = getAbsListViewHeightBasedOnChildren(view);
        ListAdapter adapter;
        int adapterCount;
        if (view != null && (adapter = view.getAdapter()) != null &&
                (adapterCount = adapter.getCount()) > 0) {
            height += view.getDividerHeight() * (adapterCount - 1);
        }
        return height;
    }


    /**
     * get GridView vertical spacing
     *
     * @param view view
     * @return int
     */
    public static int getGridViewVerticalSpacing(GridView view) {
        // get mVerticalSpacing by android.widget.GridView
        Class<?> demo = null;
        int verticalSpacing = 0;
        try {
            demo = Class.forName(CLASS_NAME_GRID_VIEW);
            Field field = demo.getDeclaredField(FIELD_NAME_VERTICAL_SPACING);
            field.setAccessible(true);
            verticalSpacing = (Integer) field.get(view);
            return verticalSpacing;
        } catch (Exception e) {
            /**
             * accept all exception, include ClassNotFoundException, NoSuchFieldException, InstantiationException,
             * IllegalArgumentException, IllegalAccessException, NullPointException
             */
            e.printStackTrace();
        }
        return verticalSpacing;
    }


    /**
     * get AbsListView height according to every children
     *
     * @param view view
     * @return int
     */
    public static int getAbsListViewHeightBasedOnChildren(AbsListView view) {
        ListAdapter adapter;
        if (view == null || (adapter = view.getAdapter()) == null) {
            return 0;
        }

        int height = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View item = adapter.getView(i, null, view);
            if (item instanceof ViewGroup) {
                item.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT));
            }
            item.measure(0, 0);
            height += item.getMeasuredHeight();
        }
        height += view.getPaddingTop() + view.getPaddingBottom();
        return height;
    }


    /**
     * set view height
     *
     * @param view view
     * @param height height
     */
    public static void setViewHeight(View view, int height) {
        if (view == null) {
            return;
        }

        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = height;
    }


    /**
     * @param view listview
     */
    public static void setListViewHeightBasedOnChildren(ListView view) {

        setViewHeight(view, getListViewHeightBasedOnChildren(view));
    }


    /**
     * @param view AbsListView
     */
    public static void setAbsListViewHeightBasedOnChildren(AbsListView view) {
        setViewHeight(view, getAbsListViewHeightBasedOnChildren(view));
    }


    /**
     * set SearchView OnClickListener
     *
     * @param v set SearchView OnClickListener
     * @param listener set SearchView OnClickListener
     */
    public static void setSearchViewOnClickListener(View v, OnClickListener listener) {
        if (v instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) v;
            int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = group.getChildAt(i);
                if (child instanceof LinearLayout ||
                        child instanceof RelativeLayout) {
                    setSearchViewOnClickListener(child, listener);
                }

                if (child instanceof TextView) {
                    TextView text = (TextView) child;
                    text.setFocusable(false);
                }
                child.setOnClickListener(listener);
            }
        }
    }


    /**
     * get descended views from parent.
     *
     * @param <T> 泛型
     * @param parent ViewGroup
     * @param filter Type of views which will be returned.
     * @param includeSubClass Whether returned list will include views which are
     * subclass of filter or not.
     * @return View
     */
    public static <T extends View> List<T> getDescendants(ViewGroup parent, Class<T> filter, boolean includeSubClass) {

        List<T> descendedViewList = new ArrayList<T>();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            Class<? extends View> childsClass = child.getClass();
            if ((includeSubClass && filter.isAssignableFrom(childsClass)) ||
                    (!includeSubClass && childsClass == filter)) {
                descendedViewList.add(filter.cast(child));
            }
            if (child instanceof ViewGroup) {
                descendedViewList.addAll(
                        getDescendants((ViewGroup) child, filter,
                                includeSubClass));
            }
        }
        return descendedViewList;
    }


    /**
     * 手动测量布局大小
     *
     * @param view 被测量的布局
     * @param width 布局默认宽度
     * @param height 布局默认高度
     * 示例： measureView(view, ViewGroup.LayoutParams.MATCH_PARENT,
     * ViewGroup.LayoutParams.WRAP_CONTENT);
     */
    public static void measureView(View view, int width, int height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(width, height);
        }
        int mWidth = ViewGroup.getChildMeasureSpec(0, 0, params.width);

        int mHeight;
        int tempHeight = params.height;
        if (tempHeight > 0) {
            mHeight = View.MeasureSpec.makeMeasureSpec(tempHeight,
                    View.MeasureSpec.EXACTLY);
        }
        else {
            mHeight = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
        }
        view.measure(mWidth, mHeight);
    }

    //*****设置外边距相关函数*******************************************************************************


    /**
     * 设置View的左侧外边距
     *
     * @param view 要设置外边距的View
     * @param left 左侧外边距
     */
    public static void setMarginLeft(View view, int left) {
        setMargins(view, left, 0, 0, 0);
    }


    /**
     * 设置View的顶部外边距
     *
     * @param view 要设置外边距的View
     * @param top 顶部外边距
     */
    public static void setMarginTop(View view, int top) {
        setMargins(view, 0, top, 0, 0);
    }


    /**
     * 设置View的右侧外边距
     *
     * @param view 要设置外边距的View
     * @param right 右侧外边距
     */
    public static void setMarginRight(View view, int right) {
        setMargins(view, 0, 0, right, 0);
    }


    /**
     * 设置View的底部外边距
     *
     * @param view 要设置外边距的View
     * @param bottom 底部外边距
     */
    public static void setMarginBottom(View view, int bottom) {
        setMargins(view, 0, 0, 0, bottom);
    }


    /**
     * 设置View的外边距(Margins)
     *
     * @param view 要设置外边距的View
     * @param left 左侧外边距
     * @param top 顶部外边距
     * @param right 右侧外边距
     * @param bottom 底部外边距
     */
    public static void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view
                    .getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();       //请求重绘
        }
    }
}
