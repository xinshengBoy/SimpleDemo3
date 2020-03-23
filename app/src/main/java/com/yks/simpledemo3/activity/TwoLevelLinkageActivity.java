package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.view.MyActionBar;

/**
 * 描述：二级联动
 * 作者：zzh
 * time:2020/03/20
 */
public class TwoLevelLinkageActivity extends TabActivity {

    private Activity mActivity = TwoLevelLinkageActivity.this;
    private Class[] activitys = {InternationalSelectActivity.class,DomesticSelectActivity.class};
    private String[] lables = {"国际","国内"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twolevel_linkage);

        initTabView();
    }

    private void initTabView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity, title_layout, "二级联动", "", false);

        TabHost tabHost = getTabHost();
        tabHost.setup(this.getLocalActivityManager());
        for (int i=0;i<activitys.length;i++){
            View view = View.inflate(this,R.layout.tab_layout,null);
            view.setBackgroundColor(getResources().getColor(R.color.colorGray4));
            TextView title = view.findViewById(R.id.txt_tabname);
            title.setText(lables[i]);

            Intent intent = new Intent(mActivity,activitys[i]);
            TabHost.TabSpec spec = tabHost.newTabSpec(lables[i]).setIndicator(view).setContent(intent);
            tabHost.addTab(spec);
        }
    }
}
