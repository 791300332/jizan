package com.sgx.jizan.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * Created by sgx on 2017/11/23.
 */

public class ResultErrorUtil {

    public static void toast(Handler handler, String errorMessage, int what) {
        //将数据放入message
        Message message=new Message();
        message.what=what;//判断是哪个handler的请求
        Bundle bundle=new Bundle();
        bundle.putString("msg",errorMessage);
        message.setData(bundle);
        handler.sendMessage(message);//handler发送消息
    }
}
