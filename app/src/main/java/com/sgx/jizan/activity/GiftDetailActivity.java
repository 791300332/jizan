package com.sgx.jizan.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.sgx.jizan.LoginActivity;
import com.sgx.jizan.R;
import com.sgx.jizan.adapter.viewpage.ViewPagerAdapter;
import com.sgx.jizan.fragment.BaseFragment;
import com.sgx.jizan.net.RetrofitFactory;
import com.sgx.jizan.service.GiftService;
import com.sgx.jizan.util.ResultErrorUtil;
import com.sgx.jizan.util.SharedPrefenencesUtils;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sgx on 2017/11/22.
 */

public class GiftDetailActivity extends AppCompatActivity {


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Toast.makeText(GiftDetailActivity.this,msg.getData().getString("msg"),Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);

        loadData();

        setContentView(R.layout.activity_gift_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        // Enable the Up button     显示返回箭头的按钮
        actionBar.setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void loadData() {
        Intent i = getIntent();
        final Long id = i.getExtras().getLong("id");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String token =  (String) SharedPrefenencesUtils.getInstance().getData("token","ss");
                String deviceId = (String) SharedPrefenencesUtils.getInstance().getData("deviceId","");

                GiftService gs = RetrofitFactory.getApiInstance().create(GiftService.class);
                try {
                    Response<ResponseBody> res = gs.detail(token,deviceId,id).execute();
                    String str = res.body().string();
                    JSONObject obj = new JSONObject(str);
                    Integer errorCode = obj.optInt("errorCode");
                    if(obj.optBoolean("success")) {


                    } else if(errorCode == 400 || errorCode == 401) {
                        Intent temp = new Intent();
                        temp.putExtra("type",1);
                        temp.setClass(getBaseContext(), LoginActivity.class);
                        startActivity(temp);
                        GiftDetailActivity.this.finish();
                    } else {
                        ResultErrorUtil.toast(handler,obj.optString("errorMessage"),1);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}
