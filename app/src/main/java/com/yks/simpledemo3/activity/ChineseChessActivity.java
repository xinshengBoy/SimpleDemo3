package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.view.MyActionBar;

/**
 * 描述：中国象棋
 * 作者：zzh
 * time:2020/01/02
 * https://github.com/xiaojianglaile/Chess
 */
public class ChineseChessActivity extends Activity {

    private Activity mActivity = ChineseChessActivity.this;
    private Context mContext = ChineseChessActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chinese_chess);

        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity, title_layout, "中国象棋", "", false);

        Toast.makeText(mContext,"红棋先行",Toast.LENGTH_SHORT).show();
    }
}
