package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;
import com.yks.simpledemo3.R;
import com.yks.simpledemo3.view.MyActionBar;
import com.yks.simpledemo3.view.RunNumberView;

import java.util.Random;

/**
 * 描述：数字滚动
 * 作者：zzh
 * time:2019/08/31
 */
public class RunNumberActivity extends Activity {

    private Activity mActivity = RunNumberActivity.this;
    private RunNumberView view_runnumber;
    private TickerView view_ticker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_number);

        initView();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity,title_layout,"数字滚动","",false);
        //https://www.jianshu.com/p/3513cdd8f320
        view_runnumber = findViewById(R.id.view_runnumber);
        view_runnumber.setMoney(567.89f);
        //https://github.com/robinhood/ticker
        view_ticker = findViewById(R.id.view_ticker);
        view_ticker.setCharacterLists(TickerUtils.provideNumberList());
        view_ticker.setText(567.89f+"");
        view_ticker.setAnimationDuration(1500);
        Button btn_change = findViewById(R.id.btn_change);

        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                float value = random.nextFloat() * 10000;
                view_runnumber.setMoney(value);
                view_ticker.setCharacterLists(TickerUtils.provideNumberList());
                view_ticker.setText(value+"");
                view_ticker.setAnimationDuration(1500);
            }
        });
    }
}
