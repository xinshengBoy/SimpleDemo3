package com.yks.simpledemo3.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * 描述：解决交付成功之后停止接单，退出程序不完全的问题（退出了会把最开始的mainActivity展示出来，导致获取权限列表失败的问题），在mainActivity和LoginActivity中的onKeyDown方法中使用
 * 使用广播来通知程序关闭所有的activity来退出程序
 * 作者：zzh
 * time:2019/07/12
 */
public class BaseActivity extends AppCompatActivity {

    private MyBaseActivityBroad broad;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        broad = new MyBaseActivityBroad();
        IntentFilter filter = new IntentFilter("com.yks.simpledemo3.baseActivity");
        registerReceiver(broad,filter);
    }

    public class MyBaseActivityBroad extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int closeAll = intent.getIntExtra("closeAll",0);
            if (closeAll == 1){
                finish();
            }
        }
    }
}
