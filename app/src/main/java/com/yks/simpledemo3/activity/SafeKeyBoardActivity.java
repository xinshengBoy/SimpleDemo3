package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.tools.KeyBoardDialogUtils;
import com.yks.simpledemo3.view.MyActionBar;

/**
 * 描述：自定义安全键盘
 * 作者：zzh
 * time:2019/07/15
 * 参考文章：https://github.com/peiniwan/SafeKeyBoard/
 */
public class SafeKeyBoardActivity extends Activity {

    private Activity mActivity = SafeKeyBoardActivity.this;
    private EditText et_keyboard;
    private KeyBoardDialogUtils utils;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_keyboard);

        initView();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity,title_layout,"自定义安全键盘","",false);

        et_keyboard = findViewById(R.id.et_keyboard);
        utils = new KeyBoardDialogUtils(mActivity);

        et_keyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.show(et_keyboard);
            }
        });
    }
}
