package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;

import com.yks.simpledemo3.R;

/**
 * 描述：悬浮吸顶
 * 作者：zzh
 * time:2019/07/13
 * 参考文档：https://www.jianshu.com/p/bbc703a0015e
 */
public class AppBayLayoutActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_appbar_layout);
    }
}
