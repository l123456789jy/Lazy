package scu.miomin.com.keeper.myview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import scu.miomin.com.keeper.R;
import scu.miomin.com.keeper.resource.MyLoader;


/**
 * ViewPager实现的轮播图广告自定义视图，如京东首页的广告轮播图效果；
 * 既支持自动轮播页面也支持手势滑动切换页面
 */

public class MyBanner extends FrameLayout {

    //自定义轮播图的资源ID
    private int[] imagesResIds;

    //放轮播图片的ImageView 的list
    private List<ImageView> imageViewsList;
    //放圆点的View的list
    private List<View> dotViewsList;

    private LinearLayout layout_dots;

    private ViewPager viewPager;

    private final static int MSG_PAUSE = 0;
    private final static int MSG_UPDATE = 1;
    private final static int MSG_STOP = 2;
    private final static int MSG_CONTINU = 3;

    private int current_item = 0;
    //Handler
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MSG_UPDATE:
                    current_item++;
                    viewPager.setCurrentItem(current_item);
                    handler.sendEmptyMessageDelayed(MSG_UPDATE, 5000);
                    break;
                case MSG_PAUSE:
                    break;

                case MSG_STOP:
                    return;

                case MSG_CONTINU:
                    sendEmptyMessage(MSG_UPDATE);
                    break;
            }

        }
    };

    public MyBanner(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }

    public MyBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    public MyBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        initData();
        initUI(context);
    }

    /**
     * 开始轮播图切换
     */
    public void startPlay() {
        Log.i("yjk", "开始轮播");
        handler.sendEmptyMessageDelayed(MSG_UPDATE, 5000);

    }

    /**
     * 停止轮播图切换
     */
    public void stopPlay() {
        handler.sendEmptyMessage(MSG_STOP);
    }

    /**
     * 初始化相关Data
     */
    private void initData() {
        imagesResIds = new int[]{
                R.drawable.banerimage1,
                R.drawable.banerimage1,
                R.drawable.banerimage1,

        };
        imageViewsList = new ArrayList<ImageView>();
        dotViewsList = new ArrayList<View>();

    }

    /**
     * 初始化Views等UI
     */
    private void initUI(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_banner, this, true);

        layout_dots = (LinearLayout) findViewById(R.id.layout_dots);

        for (int imageID : imagesResIds) {
            ImageView view = new ImageView(context);
            MyLoader.displayFromDrawable(imageID, view);
            view.setScaleType(ScaleType.FIT_XY);
            imageViewsList.add(view);

            View dot = new View(context);
            dot.setBackgroundResource(R.drawable.bg_banner_dots);
            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams(20, 20);
            if (imageID == 0) {
                dot.setEnabled(true);
            } else {
                layoutParams.leftMargin = 7;
                dot.setEnabled(false);
            }

            dot.setLayoutParams(layoutParams);
            layout_dots.addView(dot);
            dotViewsList.add(dot);

        }

        viewPager = (ViewPager) findViewById(R.id.vp_advertisement);
        viewPager.setFocusable(true);
        viewPager.setAdapter(new MyPagerAdapter());
        viewPager.setOnPageChangeListener(new MyPageChangeListener());
        viewPager.setCurrentItem(Integer.MAX_VALUE / 2 - ((Integer.MAX_VALUE / 2) % imagesResIds.length));
    }

    /**
     * 填充ViewPager的页面适配器
     */
    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub

            position %= imagesResIds.length;
            if (position < 0) {
                position = imagesResIds.length + position;
            }

            ImageView view = imageViewsList.get(position);
            //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
            ViewParent vp = view.getParent();
            if (vp != null) {
                ViewGroup parent = (ViewGroup) vp;
                parent.removeView(view);
            }
            container.addView(view);
            //add listeners here if necessary
            return view;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

    }

    /**
     * ViewPager的监听器
     * 当ViewPager中页面的状态发生改变时调用
     */
    private class MyPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
//            switch (arg0) {
//                case ViewPager.SCROLL_STATE_DRAGGING:
//                    Log.i("yjk", "正在滑动");
//                    handler.sendEmptyMessage(MSG_PAUSE);
//                    break;
//                case ViewPager.SCROLL_STATE_IDLE:
//                    Log.i("yjk", "停止滑动");
//                    handler.sendEmptyMessageDelayed(MSG_CONTINU, 5000);
//                    break;
//            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int position) {
            // TODO Auto-generated method stub

            current_item = position;
            position %= imagesResIds.length;

            if (position < 0) {
                position = imagesResIds.length + position;
            }

            for (int i = 0; i < dotViewsList.size(); i++) {
                if (i == position) {
                    dotViewsList.get(i).setEnabled(true);
                } else {
                    dotViewsList.get(i).setEnabled(false);
                }
            }
        }

    }
}
