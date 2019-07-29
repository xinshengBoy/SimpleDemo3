package com.yks.simpledemo3.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.fragment.MyDialogFragment;
import com.yks.simpledemo3.fragment.MyDialogFragment2;
import com.yks.simpledemo3.tools.Info;
import com.yks.simpledemo3.view.MyActionBar;

/**
 * 描述：Android推荐的对话框实现形式
 * 作者：zzh
 * time:2019/07/22
 * 参考：https://www.jianshu.com/p/d1852b04a0aa
 */
public class DialogFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_fragment);
        getSupportActionBar().hide();

        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(DialogFragmentActivity.this,title_layout,"对话框","",false);
    }

    public void deleteItem(View view){
        MyDialogFragment fragment = new MyDialogFragment();
        fragment.show(this.getSupportFragmentManager(),"dialog");
    }

    public void uploadHeader(View view){
        MyDialogFragment2 fragment2 = new MyDialogFragment2();
        fragment2.show(getSupportFragmentManager(),"dialog");
        fragment2.setCallback(new MyDialogFragment2.CallBack() {
            @Override
            public void onPhoto() {
                Info.showToast(DialogFragmentActivity.this,"打开相册",true);
            }

            @Override
            public void onCamera() {
                Info.showToast(DialogFragmentActivity.this,"打开相机",true);
            }
        });
    }
}
