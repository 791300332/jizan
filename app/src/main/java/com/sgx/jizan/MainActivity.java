package com.sgx.jizan;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.WindowManager;

import com.sgx.jizan.fragment.BaseFragment;
import com.sgx.jizan.adapter.viewpage.ViewPagerAdapter;
import com.sgx.jizan.net.RetrofitFactory;
import com.sgx.jizan.service.HomeService;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MenuItem menuItem;
    private BottomNavigationView bottomNavigationView;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
                    adapter.addFragment(BaseFragment.newInstance("0",msg.getData().getString("msg")));
                    adapter.addFragment(BaseFragment.newInstance("1",null));
                    adapter.addFragment(BaseFragment.newInstance("2",null));
                    viewPager.setAdapter(adapter);
                    break;
                default:
                    break;
            }
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.navigation_dashboard:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.navigation_notifications:
                    viewPager.setCurrentItem(2);
                    break;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        setupViewPager(viewPager);
    }

//    @Override
//    protected  void onStart(){
//        super.onStart();
//        banner.startAutoPlay();
//    }
//
//    protected void onStop(){
//        super.onStop();
//        banner.stopAutoPlay();
//    }

    private void setupViewPager(ViewPager viewPager) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HomeService hs = RetrofitFactory.getApiInstance().create(HomeService.class);
                try {
                    Response<ResponseBody> body = hs.index().execute();
                    String str = body.body().string();
                    //将数据放入message
                    Message message=new Message();
                    message.what=1;//判断是哪个handler的请求
                    Bundle bundle=new Bundle();
                    bundle.putString("msg",str);
                    message.setData(bundle);
                    handler.sendMessage(message);//handler发送消息
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
