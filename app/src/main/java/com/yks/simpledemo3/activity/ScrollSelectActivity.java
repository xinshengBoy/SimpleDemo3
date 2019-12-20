package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.view.HorizontalSelectedView;
import com.yks.simpledemo3.view.MyActionBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：滑动选择，类似于标尺一样
 * 作者：zzh
 * time:2019/12/19
 */
public class ScrollSelectActivity extends Activity implements View.OnClickListener {

    private Activity mActivity = ScrollSelectActivity.this;
    private TextView txt_select_pre,txt_select_next,txt_select_result;
    private HorizontalSelectedView view_selected;
    private Button btn_select_sure;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_select);

        initView();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity,title_layout,"滑动选择","",false);

        txt_select_pre = findViewById(R.id.txt_select_pre);
        view_selected = findViewById(R.id.view_selected);
        txt_select_next = findViewById(R.id.txt_select_next);
        btn_select_sure = findViewById(R.id.btn_select_sure);
        txt_select_result = findViewById(R.id.txt_select_result);

        txt_select_pre.setOnClickListener(this);
        txt_select_next.setOnClickListener(this);
        btn_select_sure.setOnClickListener(this);

        List<String> data = new ArrayList<>();
        data.add("10");
        data.add("20");
        data.add("30");
        data.add("40");
        data.add("50");
        data.add("60");
        data.add("70");
        data.add("80");
        data.add("90");
        data.add("100");
        data.add("110");
        data.add("120");
        data.add("130");
        data.add("140");
        data.add("150");
        data.add("160");
        data.add("170");
        data.add("180");
        data.add("190");
        data.add("200");
        data.add("210");
        data.add("220");
        data.add("230");
        data.add("240");
        data.add("250");
        data.add("260");
        data.add("270");
        data.add("280");
        data.add("290");
        data.add("300");
        data.add("310");
        data.add("320");
        data.add("330");
        view_selected.setData(data);
    }

    @Override
    public void onClick(View v) {
        if (v == txt_select_pre){
            view_selected.setAnLeftOffset();
        }else if (v == txt_select_next){
            view_selected.setAnRightOffset();
        }else if (v == btn_select_sure){
            txt_select_result.setText(view_selected.getSelectedString());
        }
    }
}
