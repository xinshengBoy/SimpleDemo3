package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.bean.SuitUnit;
import com.yks.simpledemo3.view.MyActionBar;
import com.yks.simpledemo3.view.SuitLines;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：曲线图
 * 作者：zzh
 * time:2019/12/20
 * https://github.com/whataa/SuitLines/blob/master/lib/src/main/java/tech/linjiang/suitlines/SuitLines.java
 */
public class SuitLinesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suitlines);

        initView();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(SuitLinesActivity.this, title_layout, "曲线图", "", false);

        SuitLines view_suitlines1 = findViewById(R.id.view_suitlines1);

        List<SuitUnit> lines = new ArrayList<>();
        lines.add(new SuitUnit(12,"1"));
        lines.add(new SuitUnit(22,"2"));
        lines.add(new SuitUnit(20,"3"));
        lines.add(new SuitUnit(18,"4"));
        lines.add(new SuitUnit(17,"5"));
        lines.add(new SuitUnit(19,"6"));
        lines.add(new SuitUnit(14,"7"));
        lines.add(new SuitUnit(18,"8"));
        lines.add(new SuitUnit(19,"9"));

        view_suitlines1.feedWithAnim(lines);
    }
}
