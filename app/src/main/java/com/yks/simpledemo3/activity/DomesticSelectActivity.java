package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yks.simpledemo3.tools.Info;

import me.jessyan.autosize.internal.CustomAdapt;

/**
 * 描述：
 * 作者：zzh
 * time:2020/03/20
 */
public class DomesticSelectActivity extends Activity implements CustomAdapt {

    private Activity mActivity = DomesticSelectActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView txt = new TextView(mActivity);
        txt.setText("我爱你，中国");
        txt.setTextSize(40);
        txt.setTextColor(Color.RED);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        txt.setLayoutParams(params);

        setContentView(txt);
    }

    @Override
    public boolean isBaseOnWidth() {
        return true;
    }

    @Override
    public float getSizeInDp() {
        Info.getScreenSize(mActivity);
        return Info.SCREEN_HEIGHT;
    }
}
