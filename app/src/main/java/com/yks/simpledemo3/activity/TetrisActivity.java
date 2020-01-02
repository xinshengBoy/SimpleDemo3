package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.tools.Info;
import com.yks.simpledemo3.view.MyActionBar;
import com.yks.simpledemo3.view.tetris.BlockUnit;
import com.yks.simpledemo3.view.tetris.NextBlockView;
import com.yks.simpledemo3.view.tetris.TetrisView;

import java.util.List;

/**
 * 描述：俄罗斯方块
 * 作者：zzh
 * time:2019/12/30
 * https://www.jb51.net/article/142669.htm
 */
public class TetrisActivity extends Activity {

    private Context mContext = TetrisActivity.this;
    private Activity mActivity = TetrisActivity.this;
    private TetrisView tetrisView;
    private NextBlockView nextBlockView;
    public TextView score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tetris);

        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity, title_layout, "俄罗斯方块", "", false);
        tetrisView = findViewById(R.id.tetrisView);
        nextBlockView = findViewById(R.id.nextBlockView);
        score = findViewById(R.id.score);

        tetrisView.setFather(this);
    }

    public void setNextBlockView(List<BlockUnit> blockUnits, int div_x) {
        nextBlockView.setBlockUnits(blockUnits, div_x);
    }

    /**
     * 开始游戏
     *
     * @param view
     */
    public void startGame(View view) {
        tetrisView.startGame();
//        Info.showToast(mContext,"游戏运行中",true);
    }

    /**
     * 暂停游戏
     */
    public void pauseGame(View view) {
        tetrisView.pauseGame();
        Info.showToast(mContext,"游戏已暂停",true);
    }

    /**
     * 继续游戏
     */
    public void continueGame(View view) {
        tetrisView.continueGame();
        Info.showToast(mContext,"游戏运行中",true);
    }

    /**
     * 停止游戏
     */
    public void stopGame(View view) {
        tetrisView.stopGame();
        score.setText(""+0);
        Info.showToast(mContext,"游戏已停止",true);
    }

    /**
     * 向左滑动
     */
    public void toLeft(View view) {
        tetrisView.toLeft();
    }

    /**
     * 向右滑动
     */
    public void toRight(View view) {
        tetrisView.toRight();
    }
    /**
     * 向右滑动
     */
    public void toRoute(View view) {
        tetrisView.route();
    }
}
