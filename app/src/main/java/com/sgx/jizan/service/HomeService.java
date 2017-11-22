package com.sgx.jizan.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
/**
 * Created by sgx on 2017/11/22.
 */

public interface HomeService {
    /*
    获取首页数据
     */
    @GET("home/index")
    Call<ResponseBody> index();
}
