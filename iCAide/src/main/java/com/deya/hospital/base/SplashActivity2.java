package com.deya.hospital.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.deya.acaide.R;
import com.deya.hospital.account.UserInfoActivity;
import com.deya.hospital.login.LoginActivity;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.Tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity2 extends BaseActivity {

    private static final String TAG = "test";
    private ViewPager viewpager = null;
    private List<View> list = null;
    private ImageView[] img = null;
    Tools tools;
    String isFirstLogin = "0";
    ImageView bgImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.spanlish_activity);
        tools = new Tools(mcontext, Constants.AC);
        bgImg = (ImageView) this.findViewById(R.id.bg);
        isFirstLogin = "1";


        viewpager = (ViewPager) findViewById(R.id.viewpager);
        viewpager.setVisibility(View.VISIBLE);//每次都显示
        if (AbStrUtil.isEmpty(isFirstLogin) || isFirstLogin.equals("0")) {

            tools.putValue(Constants.IS_FIRST_LOGIN, "1");
        } else {
            viewpager.setVisibility(View.GONE);

            startTimer();
        }
        list = new ArrayList<View>();
        View v1 = getLayoutInflater().inflate(R.layout.spanlish_table1, null);
        v1.findViewById(R.id.lay1).setTag(0);
        v1.findViewById(R.id.img).setTag(0);
        v1.setTag(0);


        list.add(v1);
        View v2 = getLayoutInflater().inflate(R.layout.spanlish_table2, null);
        v2.findViewById(R.id.lay1).setTag(1);
        v2.findViewById(R.id.img).setTag(1);
        v2.setTag(1);


        list.add(v2);
        View convertView = getLayoutInflater().inflate(
                R.layout.spanlish_table3, null);
        convertView.setTag(2);
        convertView.findViewById(R.id.lay1).setTag(2);
        convertView.findViewById(R.id.img).setTag(2);
        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent it = new Intent(SplashActivity2.this,
                        LoginActivity.class);
                startActivity(it);
                finish();

            }
        });
        list.add(convertView);

        img = new ImageView[list.size()];
        LinearLayout layout = (LinearLayout) findViewById(R.id.viewGroup);
        // for (int i = 0; i < list.size(); i++) {
        // img[i] = new ImageView(SplashActivity2.this);
        // if (0 == i) {
        // img[i].setBackgroundResource(R.drawable.);
        // } else {
        // img[i].setBackgroundResource(R.drawable.page_indicator);
        // }
        // img[i].setPadding(0, 0, 20, 0);
        // layout.addView(img[i]);
        // }
        viewpager.setAdapter(new ViewPagerAdapter(list));
        viewpager.setOnPageChangeListener(new ViewPagerPageChangeListener());
    }

    private Timer timerReciprocal;
    private int secounds = 0;

    private void startTimer() {

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                secounds++;
                if (secounds == 1) {
                    if (!AbStrUtil.isEmpty(tools.getValue(Constants.AUTHENT))
                            ) {
                        if(AbStrUtil.isEmpty(tools.getValue(Constants.STATE))||tools.getValue(Constants.STATE).equals("0")){
                            Intent intent = new Intent(mcontext,
                                    UserInfoActivity.class);
                            startActivity(intent);
                        }else{
                        Intent intent2 = new Intent(mcontext, MainActivity.class);
                        startActivity(intent2);
                       }
                        finish();
                    } else {
                        Intent it = new Intent(SplashActivity2.this,
                                LoginActivity.class);
                        startActivity(it);
                        finish();
                    }
                }
            }
        };
        timerReciprocal = new Timer(true);
        timerReciprocal.schedule(task, 800, 800);
    }

    @Override
    protected void onDestroy() {
        if (null != timerReciprocal) {
            timerReciprocal.cancel();
        }
        super.onDestroy();
    }

    class ViewPagerAdapter extends PagerAdapter implements OnClickListener {

        private List<View> list = null;

        public ViewPagerAdapter(List<View> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            list.get(position).findViewById(R.id.lay1).setOnClickListener(this);
            list.get(position).findViewById(R.id.img).setOnClickListener(this);

            list.get(position).setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    //	ToastUtils.showToast(mcontext, v.getTag()+"");
//					Object obj=v.getTag();
//					try {
//						int page=Integer.parseInt(obj.toString());
//						page=page==2?1:page+1;
//						viewpager.setCurrentItem(page);
//					} catch (Exception e) {
//						// TODO: handle exception
//					}

                }
            });
            return list.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position));
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        Intent it;

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            try {
                int page = Integer.parseInt(v.getTag().toString());
                if (null == it)
                    it = new Intent(SplashActivity2.this,
                            LoginActivity.class);

                switch (v.getId()) {
                    case R.id.img:
                        if (page < 2) {
                            viewpager.setCurrentItem(++page);
                        } else {
                            startActivity(it);
                            finish();
                        }
                        break;
                    case R.id.lay1:
                        if (page > 0) {
                            viewpager.setCurrentItem(--page);
                        }
                        break;
                    default:
                        break;
                }

            } catch (Exception e) {
                // TODO: handle exception
            }
        }

    }

    class ViewPagerPageChangeListener implements OnPageChangeListener {

        /*
         * state：网上通常说法：1的时候表示正在滑动，2的时候表示滑动完毕了，0的时候表示什么都没做，就是停在那；
         * 我的认为：1是按下时，0是松开，2则是新的标签页的是否滑动了
         * (例如：当前页是第一页，如果你向右滑不会打印出2，如果向左滑直到看到了第二页，那么就会打印出2了)；
         * 个人认为一般情况下是不会重写这个方法的
         */
        @Override
        public void onPageScrollStateChanged(int state) {
        }

        /*
         * page：看名称就看得出，当前页； positionOffset：位置偏移量，范围[0,1]；
         * positionoffsetPixels：位置像素，范围[0,屏幕宽度)； 个人认为一般情况下是不会重写这个方法的
         */
        @Override
        public void onPageScrolled(int page, float positionOffset,
                                   int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int page) {
            if (page == 2) {
                viewpager.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent it = new Intent(SplashActivity2.this,
                                LoginActivity.class);
                        startActivity(it);
                        finish();

                    }
                });
            }
            // //更新图标
            // for (int i = 0; i < list.size(); i++) {
            // if (page == i) {
            // img[i].setBackgroundResource(R.drawable.page_indicator_focused);
            // } else {
            // img[i].setBackgroundResource(R.drawable.page_indicator);
            // }
            // }
        }
    }

}
