package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.tools.Info;
import com.yks.simpledemo3.view.GobangView;
import com.yks.simpledemo3.view.MyActionBar;

import net.lemonsoft.lemonbubble.LemonBubble;
import net.lemonsoft.lemonhello.LemonHello;
import net.lemonsoft.lemonhello.LemonHelloAction;
import net.lemonsoft.lemonhello.LemonHelloInfo;
import net.lemonsoft.lemonhello.LemonHelloView;
import net.lemonsoft.lemonhello.interfaces.LemonHelloActionDelegate;

/**
 * 描述：五子棋
 * 作者：zzh
 * time:2019/12/27
 * https://github.com/Brioal/GoBang/tree/master/app/src/main/java/com/brioal/gobang/view
 */
public class GobangActivity extends Activity {

    private Context mContext = GobangActivity.this;
    private Activity mActivity = GobangActivity.this;
    private GobangView view_gobang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gobang);

        initView();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity, title_layout, "五子棋", "", false);

        view_gobang = findViewById(R.id.view_gobang);
        view_gobang.setOnGameListener(new GobangView.onGameListener() {
            @Override
            public void onGameOver(int i) {
                Info.showToast(mContext,i == 0 ? "白棋胜利" : "黑棋胜利",true);
                Info.playRingtone(mContext,true);
            }
        });
        LemonHello.getInformationHello("提示","黑棋先行").addAction(new LemonHelloAction("确定", new LemonHelloActionDelegate() {
            @Override
            public void onClick(LemonHelloView lemonHelloView, LemonHelloInfo lemonHelloInfo, LemonHelloAction lemonHelloAction) {
                lemonHelloView.hide();
            }
        })).show(mContext);
    }

    /**
     * 描述：再来一局
     * @param view 按钮
     */
    public void restartGames(View view){
        view_gobang.reStartGame();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LemonBubble.forceHide();
    }
}
