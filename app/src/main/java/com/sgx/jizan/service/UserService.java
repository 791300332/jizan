package com.sgx.jizan.service;

import java.lang.annotation.Inherited;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by sgx on 2017/11/23.
 */

public interface UserService {

    @GET("user/login")
    Call<ResponseBody> login(@Header("App-Device") String deviceId, @Query("username") String phone, @Query("psw") String psw);
}
