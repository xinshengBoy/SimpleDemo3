package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.view.MyActionBar;

/**
 * 描述：超级按钮
 * 作者：zzh
 * time:2019/07/25
 * 参考：https://github.com/ansnail/SuperButton
 */
public class SuperButtonActivity extends Activity {

    private Activity mActivity = SuperButtonActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_button);

        initView();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity, title_layout, "超级按钮", "", false);
    }
}
