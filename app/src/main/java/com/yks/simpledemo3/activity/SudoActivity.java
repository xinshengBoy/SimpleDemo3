package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.view.MyActionBar;
import com.yks.simpledemo3.view.SudoView;

/**
 * 描述：数独
 * 作者：zzh
 * time:2019/11/06
 * https://github.com/whoma/Simple-sudoku/blob/master/app/src/main/java/com/example/jobs/newsudo/Sudo.java
 */
public class SudoActivity extends Activity {

    private Activity mActivity = SudoActivity.this;
    private SudoView sudoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudo);

        initView();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity, title_layout, "数独", "", false);

        LinearLayout view_sudo = findViewById(R.id.view_sudo);
        sudoView = new SudoView(SudoActivity.this);
        sudoView.setLevel(1);
        view_sudo.addView(sudoView);

        Button btn_sudo_reset = findViewById(R.id.btn_sudo_reset);
        btn_sudo_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sudoView.reset();
            }
        });
    }
}
