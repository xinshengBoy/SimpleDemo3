package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.yks.simpledemo3.view.puzzle.PuzzleImageView;

/**
 * 描述： 拼图游戏
 * 作者：zzh
 * time:2020/01/02
 * https://github.com/luoyesiqiu/PuzzleGame/blob/master/app/src/main/java/com/luoye/pintu/MainView.java
 */
public class PuzzleImageActivity extends Activity {

    private Context mContext = PuzzleImageActivity.this;
    private Activity mActivity = PuzzleImageActivity.this;
    private PuzzleImageView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = new PuzzleImageView(mContext,mActivity);
        setContentView(view);
//        setContentView(R.layout.activity_puzzle_image);
//        initView();
    }

//    private void initView() {
//        LinearLayout title_layout = findViewById(R.id.headerLayout);
//        MyActionBar.show(mActivity, title_layout, "拼图游戏", "", false);
//
//        PuzzleImageView view_pullze = findViewById(R.id.view_pullze);
//        view_pullze.init(mActivity);
//    }
}
