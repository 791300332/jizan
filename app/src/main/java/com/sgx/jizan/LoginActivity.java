package com.sgx.jizan;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sgx.jizan.activity.GiftDetailActivity;
import com.sgx.jizan.adapter.viewpage.ViewPagerAdapter;
import com.sgx.jizan.fragment.BaseFragment;
import com.sgx.jizan.net.RetrofitFactory;
import com.sgx.jizan.service.UserService;
import com.sgx.jizan.util.ResultErrorUtil;
import com.sgx.jizan.util.SharedPrefenencesUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{

    private AutoCompleteTextView phone;

    private EditText password;

    private Button button;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Toast.makeText(LoginActivity.this,msg.getData().getString("msg"),Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        phone = this.findViewById(R.id.phone);
        password = this.findViewById(R.id.password);
        button = this.findViewById(R.id.phone_login_button);
        button.setClickable(true);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String username = phone.getText().toString();
                        String psw = password.getText().toString();

                        int v = validate(username,psw);
                        if(v != 0) {
                            ResultErrorUtil.toast(handler,v == 1?"手机格式错误":"密码由8-16位，数字和大小字母组成",1);
                            return;
                        }

                        String deviceId = (String) SharedPrefenencesUtils.getInstance().getData("deviceId","");
                        UserService us = RetrofitFactory.getApiInstance().create(UserService.class);
                        try{
                            Response<ResponseBody> res = us.login(deviceId,username,psw).execute();
                            String str = res.body().string();
                            JSONObject obj = new JSONObject(str);
                            Integer errorCode = obj.optInt("errorCode");
                            if(obj.optBoolean("success")) {
                                SharedPrefenencesUtils.getInstance().saveData("token",obj.optString("token"));
                                finish();
                            } else {
                                ResultErrorUtil.toast(handler,obj.optString("errorMessage"),1);
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

    }

    private Integer validate(String username, String psw) {
        if(username.length() <11) {
            return 1;
        }
        String reg = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";
        if(psw == null || !psw.matches(reg)) {
            return 2;
        }
        return 0;
    }

}

