package com.robin.appdemo;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends BaseActivity {
//    private static final String TAG = "SplashActivity";
    ViewPager viewPager;
    LinearLayout ll_guide_spot;
    ImageView iv_red_spot;
    Button bt_userguide_start;
//    Button bt_userguide_start1;
    //创建一个数组用来存放新手向导的图片
    int[] imageIdArray = new int[]{R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3};
    List<ImageView> imageViewList; //用来存放ImageView控件的集合
    int moveDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarFullTransparent();
        setContentView(R.layout.activity_splash);

        viewPager = findViewById(R.id.vp_user_guide);
        ll_guide_spot = findViewById(R.id.ll_guide_spot);
        iv_red_spot = findViewById(R.id.iv_red_spot);
        bt_userguide_start = findViewById(R.id.bt_userguide_start);
//        bt_userguide_start1 = findViewById(R.id.bt_userguide_start1);
//        setMargins(bt_userguide_start1, 0, getStatusHeight(this), 0, 0);
//        Log.i(TAG, "onCreate: " + getStatusHeight(this));
        bt_userguide_start.setVisibility(View.INVISIBLE);
        bt_userguide_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectAct(MainActivity.class);
            }
        });

        initData();
        viewPager.setAdapter(new MyViewPager()); //给viewpager设置适配器
        //给viewpager设置滑动监听事件
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //当页面滑动过程中回调
                //更新页面中小红点的位置
                //1.计算小红点当前的左边距
                int leftMargin = (int) (moveDistance * positionOffset) + position * moveDistance;
                //2.获取当前小红点的布局参数
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv_red_spot.getLayoutParams();
                //3.修改左边距
                layoutParams.leftMargin = leftMargin;
                //4.重新设置布局参数
                iv_red_spot.setLayoutParams(layoutParams);
            }

            @Override
            public void onPageSelected(int position) {
                //某个页面被选中
                if (position == imageIdArray.length - 1) {
                    bt_userguide_start.setVisibility(View.VISIBLE);
                } else {
                    bt_userguide_start.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        iv_red_spot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                iv_red_spot.getViewTreeObserver().removeOnGlobalLayoutListener(this); //移除监听，避免重复回调
                //计算两个小圆点之间的距离
                //小红点的移动距离=第二个小圆点的left值-第一个小圆点的left值
                moveDistance = ll_guide_spot.getChildAt(1).getLeft() - ll_guide_spot.getChildAt(0).getLeft();
            }
        });
    }

    //初始化数据
    private void initData() {
        imageViewList = new ArrayList<>();
        for (int i = 0; i < imageIdArray.length; i++) {
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setBackgroundResource(imageIdArray[i]);
            imageViewList.add(imageView);
            //初始化小圆点
            ImageView spot = new ImageView(getApplicationContext());
            spot.setImageResource(R.mipmap.user_spot_gray);
            //初始化布局参数
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i > 0) params.leftMargin = 15;
            spot.setLayoutParams(params); //设置小圆点的间距
            ll_guide_spot.addView(spot);
        }
    }

    private class MyViewPager extends PagerAdapter {
        //返回item的个数
        @Override
        public int getCount() {
            return imageViewList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        //初始化item布局
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView = imageViewList.get(position);
            container.addView(imageView);
            return imageView;
        }

        //销毁item
        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 全透状态栏
     */
    protected void setStatusBarFullTransparent() {
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

//    public static void setMargins(View v, int l, int t, int r, int b) {
//        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
//            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
//            p.setMargins(l, t, r, b);
////            v.requestLayout();
//        }
//    }
//
//    public static int getStatusHeight(Context context) {
//        int result = 0;
//        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
//        if (resId > 0) {
//            result = context.getResources().getDimensionPixelOffset(resId);
//        }
//        return result;
//    }
}
