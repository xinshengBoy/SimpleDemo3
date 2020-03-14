package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.adapter.BaseRecyclerAdapter;
import com.yks.simpledemo3.tools.Info;
import com.yks.simpledemo3.view.MyActionBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：复制颜色
 * 作者：zzh
 * time:2020/03/14
 */
public class CopyColorActivity extends Activity {

    private Activity mActivity = CopyColorActivity.this;
    private Context mContext = CopyColorActivity.this;

    private List<Integer> mList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copy_color);

        initData();
        initView();
    }

    private void initData() {
        int [] colors = getResources().getIntArray(R.array.recycle_color_array);
        if (colors.length != 0){
            for (int color : colors) {
                mList.add(color);
            }
        }
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity, title_layout, "复制颜色", "", false);

        RecyclerView recyclerview_copycolor = findViewById(R.id.recyclerview_copycolor);
        CopyColorAdapter adapter = new CopyColorAdapter(mList);
        Info.setRecycviewAdapter(mContext,recyclerview_copycolor,adapter);
    }

    private class CopyColorAdapter extends BaseRecyclerAdapter<Integer>{

        CopyColorAdapter(List<Integer> mDatas) {
            super(mDatas, R.layout.item_copy_color);
        }

        @Override
        protected void bindData(BaseViewHolder holder, int position, final Integer s) {
            TextView txt_item_copy_color = (TextView) holder.getView(R.id.txt_item_copy_color);

            txt_item_copy_color.setText(s+"");
            txt_item_copy_color.setTextColor(mList.get(mList.size() - position-1));
            txt_item_copy_color.setBackgroundColor(s);

            txt_item_copy_color.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyActionBar.setViewBackgroundColor(s);
                }
            });
        }
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
