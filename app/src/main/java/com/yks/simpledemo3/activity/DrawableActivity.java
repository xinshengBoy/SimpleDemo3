package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.view.MyActionBar;

/**
 * 描述：drawable图片属性的使用
 * 作者：zzh
 * time:2019/07/12
 * 参考文档：https://blog.csdn.net/weixin_40797204/article/details/78832640
 */
public class DrawableActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawable);

        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(DrawableActivity.this,title_layout,"drawable图片属性","",false);
    }
}
