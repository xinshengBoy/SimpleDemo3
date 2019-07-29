package com.yks.simpledemo3.activity;

import android.app.Application;

import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * 描述：初始化网络请求和多线程
 * 作者：zzh
 * time:2019/03/18
 */
public class MyApplication extends Application {

    public static ExecutorService cachedThreadPool;
    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .build();
        OkHttpUtils.initClient(client);
        //TODO 线程相关文章：https://www.cnblogs.com/1925yiyi/p/9040633.html
        cachedThreadPool = Executors.newCachedThreadPool();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        cachedThreadPool.shutdown();//终止线程
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
