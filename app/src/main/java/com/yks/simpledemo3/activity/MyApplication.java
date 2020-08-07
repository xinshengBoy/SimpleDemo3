package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.app.Application;

import com.kongzue.dialog.util.DialogSettings;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.yks.simpledemo3.tools.CrashHandler;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.onAdaptListener;
import me.jessyan.autosize.utils.ScreenUtils;
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
        //todo 初始化crash闪退日志监听
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);

        //todo 空竹对话框初始化设置
        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;//主题风格 STYLE_MATERIAL, STYLE_KONGZUE, STYLE_IOS
        DialogSettings.theme = DialogSettings.THEME.LIGHT;//主题风格 LIGHT, DARK
        DialogSettings.autoShowInputKeyboard = false;//不自动弹出输入法
        DialogSettings.cancelable = true;//是否可点击外围空白处关闭
        DialogSettings.cancelableTipDialog = true;
        DialogSettings.init();

        //TODO 今日头条终极适配方案初始化
        AutoSize.initCompatMultiProcess(this);
        AutoSizeConfig.getInstance()
                .setOnAdaptListener(new onAdaptListener() {
                    @Override
                    public void onAdaptBefore(Object target, Activity activity) {
                        AutoSizeConfig.getInstance().setScreenWidth(ScreenUtils.getScreenSize(activity)[0]);
                        AutoSizeConfig.getInstance().setScreenHeight(ScreenUtils.getScreenSize(activity)[1]);
                    }

                    @Override
                    public void onAdaptAfter(Object target, Activity activity) {

                    }
                });

        //todo DBFLO数据库的初始化（sqlite基础）
        FlowManager.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        cachedThreadPool.shutdown();//终止线程
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
