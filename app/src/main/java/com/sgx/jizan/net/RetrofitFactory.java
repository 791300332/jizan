package com.sgx.jizan.net;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;

/**
 * Created by sgx on 2017/11/22.
 */

public class RetrofitFactory {

    private static final String apiUrl = "http://18.216.168.184:8080/";

    private static Retrofit apiRetrofit = null;

    public static Retrofit getApiInstance(){
        if(apiRetrofit == null) {
            synchronized (RetrofitFactory.class) {
                apiRetrofit = new Retrofit.Builder().baseUrl(apiUrl)
                        .client(new OkHttpClient.Builder().addInterceptor(new Interceptor(){

                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request original = chain.request();
                                Request request = original.newBuilder()
                                        .header("Os-Type","Android")
                                        .header("Client-Version","1.0.0")
                                        .header("Accept-Charset","utf-8")
                                        .header("App-Identifier","com.sgx.easy")
                                        .build();
                                return chain.proceed(request);
                            }
                        }).build())
                        .build();

            }
        }
        return apiRetrofit;
    }
}
