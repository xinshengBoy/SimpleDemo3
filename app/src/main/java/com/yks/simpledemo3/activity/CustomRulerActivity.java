package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.view.MyActionBar;

/**
 * 描述：自定义直尺
 * 作者：zzh
 * time:2019/10/14
 * 参考：https://github.com/kodeflow/ruler
 */
public class CustomRulerActivity extends Activity {

    private Activity mActivity = CustomRulerActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_ruler);
        initView();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity,title_layout,"自定义直尺","",false);

    }

}
