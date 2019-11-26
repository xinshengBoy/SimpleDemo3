package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.jiakaiyang.library.easyform.view.EFFormView;
import com.yks.simpledemo3.R;
import com.yks.simpledemo3.view.MyActionBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：生成表格
 * 作者：zzh
 * time:2019/11/23
 * https://github.com/kaiyangjia/EasyForm-Android
 */
public class CreateFormActivity extends Activity {
    private Context mContext = CreateFormActivity.this;
    private Activity mActivity = CreateFormActivity.this;

    private List<Map<String,Object>> mList;
    private EFFormView view_efform;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_form);

        initView();
        initData();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity, title_layout, "生成表格", "", false);

        view_efform = findViewById(R.id.view_efform);
    }

    private void initData(){
        mList = new ArrayList<>();
        for (int i=0;i<50;i++){
            Map<String, Object> map1 = new HashMap<String, Object>();
            map1.put("钟志华"+i,"很帅哟"+i);
            mList.add(map1);
        }

        view_efform.setData(mList);
        view_efform.fillForm();
        view_efform.setRowClickChange();//设置点击每条变色
//        view_efform.setRowClickable(0,false);//设置表头第一行不可点击
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mList != null && mList.size() != 0){
            mList.clear();
            mList = null;
        }
    }
}
