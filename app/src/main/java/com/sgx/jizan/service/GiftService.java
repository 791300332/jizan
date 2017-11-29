package com.sgx.jizan.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by sgx on 2017/11/23.
 */

public interface GiftService {
    /**
     * 获取点赞礼物详情
     * @return
     */
    @GET("gift/detail")
    Call<ResponseBody> detail(@Header("Auth-Token") String token,@Header("App-Device") String dvice, @Query("id") Long id);
}
