package com.github.lazylibrary.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.view.ViewTreeObserver;
import android.widget.EditText;
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
/**
 * 视图工具箱
 */
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
     * @param includeSubClass Whether returned list will include views which
     * are
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


    /**
     * 获取一个LinearLayout
     *
     * @param context 上下文
     * @param orientation 流向
     * @param width 宽
     * @param height 高
     * @return LinearLayout
     */
    public static LinearLayout createLinearLayout(Context context, int orientation, int width, int height) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(orientation);
        linearLayout.setLayoutParams(
                new LinearLayout.LayoutParams(width, height));
        return linearLayout;
    }


    /**
     * 获取一个LinearLayout
     *
     * @param context 上下文
     * @param orientation 流向
     * @param width 宽
     * @param height 高
     * @param weight 权重
     * @return LinearLayout
     */
    public static LinearLayout createLinearLayout(Context context, int orientation, int width, int height, int weight) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(orientation);
        linearLayout.setLayoutParams(
                new LinearLayout.LayoutParams(width, height, weight));
        return linearLayout;
    }


    /**
     * 根据ListView的所有子项的高度设置其高度
     */
    public static void setListViewHeightByAllChildrenViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {
            int totalHeight = 0;
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() *
                    (listAdapter.getCount() - 1));
            ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
            listView.setLayoutParams(params);
        }
    }


    /**
     * 将给定视图的高度增加一点
     *
     * @param view 给定的视图
     * @param increasedAmount 增加多少
     */
    public static void addViewHeight(View view, int increasedAmount) {
        ViewGroup.LayoutParams headerLayoutParams
                = (ViewGroup.LayoutParams) view.getLayoutParams();
        headerLayoutParams.height += increasedAmount;
        view.setLayoutParams(headerLayoutParams);
    }


    /**
     * 设置给定视图的宽度
     *
     * @param view 给定的视图
     * @param newWidth 新的宽度
     */
    public static void setViewWidth(View view, int newWidth) {
        ViewGroup.LayoutParams headerLayoutParams
                = (ViewGroup.LayoutParams) view.getLayoutParams();
        headerLayoutParams.width = newWidth;
        view.setLayoutParams(headerLayoutParams);
    }


    /**
     * 将给定视图的宽度增加一点
     *
     * @param view 给定的视图
     * @param increasedAmount 增加多少
     */
    public static void addViewWidth(View view, int increasedAmount) {
        ViewGroup.LayoutParams headerLayoutParams
                = (ViewGroup.LayoutParams) view.getLayoutParams();
        headerLayoutParams.width += increasedAmount;
        view.setLayoutParams(headerLayoutParams);
    }


    /**
     * 获取流布局的底部外边距
     */
    public static int getLinearLayoutBottomMargin(LinearLayout linearLayout) {
        return ((LinearLayout.LayoutParams) linearLayout.getLayoutParams()).bottomMargin;
    }


    /**
     * 设置流布局的底部外边距
     */
    public static void setLinearLayoutBottomMargin(LinearLayout linearLayout, int newBottomMargin) {
        LinearLayout.LayoutParams lp
                = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        lp.bottomMargin = newBottomMargin;
        linearLayout.setLayoutParams(lp);
    }


    /**
     * 获取流布局的高度
     */
    public static int getLinearLayoutHiehgt(LinearLayout linearLayout) {
        return ((LinearLayout.LayoutParams) linearLayout.getLayoutParams()).height;
    }


    /**
     * 设置输入框的光标到末尾
     */
    public static final void setEditTextSelectionToEnd(EditText editText) {
        Editable editable = editText.getEditableText();
        Selection.setSelection((Spannable) editable,
                editable.toString().length());
    }


    /**
     * 执行测量，执行完成之后只需调用View的getMeasuredXXX()方法即可获取测量结果
     */
    public static final View measure(View view) {
        ViewGroup.LayoutParams p = view.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight,
                    View.MeasureSpec.EXACTLY);
        }
        else {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
        }
        view.measure(childWidthSpec, childHeightSpec);
        return view;
    }


    /**
     * 获取给定视图的测量高度
     */
    public static final int getMeasuredHeight(View view) {
        return measure(view).getMeasuredHeight();
    }


    /**
     * 获取给定视图的测量宽度
     */
    public static final int getMeasuredWidth(View view) {
        return measure(view).getMeasuredWidth();
    }


    /**
     * 获取视图1相对于视图2的位置，注意在屏幕上看起来视图1应该被视图2包含，但是视图1和视图并不一定是绝对的父子关系也可以是兄弟关系，只是一个大一个小而已
     */
    public static final Rect getRelativeRect(View view1, View view2) {
        Rect childViewGlobalRect = new Rect();
        Rect parentViewGlobalRect = new Rect();
        view1.getGlobalVisibleRect(childViewGlobalRect);
        view2.getGlobalVisibleRect(parentViewGlobalRect);
        return new Rect(childViewGlobalRect.left - parentViewGlobalRect.left,
                childViewGlobalRect.top - parentViewGlobalRect.top,
                childViewGlobalRect.right - parentViewGlobalRect.left,
                childViewGlobalRect.bottom - parentViewGlobalRect.top);
    }


    /**
     * 删除监听器
     */
    @SuppressWarnings("deprecation") @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static final void removeOnGlobalLayoutListener(ViewTreeObserver viewTreeObserver, ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            viewTreeObserver.removeGlobalOnLayoutListener(
                    onGlobalLayoutListener);
        }
        else {
            viewTreeObserver.removeOnGlobalLayoutListener(
                    onGlobalLayoutListener);
        }
    }


    /**
     * 缩放视图
     */
    public static void zoomView(View view, float scaleX, float scaleY, Point originalSize) {
        int width = (int) (originalSize.x * scaleX);
        int height = (int) (originalSize.y * scaleY);
        ViewGroup.LayoutParams viewGroupParams = view.getLayoutParams();
        if (viewGroupParams != null) {
            viewGroupParams.width = width;
            viewGroupParams.height = height;
        }
        else {
            viewGroupParams = new ViewGroup.LayoutParams(width, height);
        }
        view.setLayoutParams(viewGroupParams);
    }


    /**
     * 缩放视图
     */
    public static void zoomView(View view, float scaleX, float scaleY) {
        zoomView(view, scaleX, scaleY,
                new Point(view.getWidth(), view.getHeight()));
    }


    /**
     * 缩放视图
     *
     * @param scale 比例
     */
    public static void zoomView(View view, float scale, Point originalSize) {
        zoomView(view, scale, scale, originalSize);
    }


    /**
     * 缩放视图
     */
    public static void zoomView(View view, float scale) {
        zoomView(view, scale, scale,
                new Point(view.getWidth(), view.getHeight()));
    }
}
