package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yks.simpledemo3.R;
import com.yks.simpledemo3.adapter.BaseRecyclerAdapter;
import com.yks.simpledemo3.bean.BottomSheetBean;
import com.yks.simpledemo3.tools.Info;
import com.yks.simpledemo3.view.MyActionBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：底部弹出滑动
 * 作者：zzh
 * time:2019/09/04
 * https://juejin.im/entry/58b78b41570c3500620142cb
 */
public class BottomSheetActivity extends Activity {

    private Context mContext = BottomSheetActivity.this;
    private Activity mActivity = BottomSheetActivity.this;

    private RecyclerView rc_bottom_sheet;
    private BottomSheetBehavior behavior;
    private List<BottomSheetBean> mList = new ArrayList<>();
    private boolean isShow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_sheet);

        initView();
        initData();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity, title_layout, "底部弹出滑动", "", false);

        View bottom_sheet = findViewById(R.id.bottom_sheet);
        behavior = BottomSheetBehavior.from(bottom_sheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {

            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
        rc_bottom_sheet = findViewById(R.id.rc_bottom_sheet);
        Button btn_click_me = findViewById(R.id.btn_click_me);
        btn_click_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShow){
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    isShow = false;
                }else {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    rc_bottom_sheet.scrollToPosition(0);//回到最前面
                    isShow = true;
                }
            }
        });
    }

    private void initData(){
        BottomSheetBean bean1 = new BottomSheetBean(R.drawable.ic_appicon,R.string.app_name);
        BottomSheetBean bean2 = new BottomSheetBean(R.drawable.ic_bottom_sheet,R.string.ic_bottom_sheet);
        BottomSheetBean bean3 = new BottomSheetBean(R.drawable.ic_circle_menu,R.string.ic_circle_menu);
        BottomSheetBean bean4 = new BottomSheetBean(R.drawable.ic_clock,R.string.ic_clock);
        BottomSheetBean bean5 = new BottomSheetBean(R.drawable.ic_csv_table,R.string.ic_csv_table);
        BottomSheetBean bean6 = new BottomSheetBean(R.drawable.ic_custom_clock,R.string.ic_custom_clock);
        BottomSheetBean bean7 = new BottomSheetBean(R.drawable.ic_marquee,R.string.ic_marquee);
        BottomSheetBean bean8 = new BottomSheetBean(R.drawable.ic_number_rain,R.string.ic_number_rain);
        BottomSheetBean bean9 = new BottomSheetBean(R.drawable.ic_radar,R.string.ic_radar);
        BottomSheetBean bean10 = new BottomSheetBean(R.drawable.ic_run_number,R.string.ic_run_number);
        BottomSheetBean bean11 = new BottomSheetBean(R.drawable.ic_save,R.string.ic_windwill);
        BottomSheetBean bean12 = new BottomSheetBean(R.drawable.ic_super_button,R.string.ic_super_button);
        BottomSheetBean bean13 = new BottomSheetBean(R.drawable.ic_temperature,R.string.ic_temperature);

        mList.add(bean1);
        mList.add(bean2);
        mList.add(bean3);
        mList.add(bean4);
        mList.add(bean5);
        mList.add(bean6);
        mList.add(bean7);
        mList.add(bean8);
        mList.add(bean9);
        mList.add(bean10);
        mList.add(bean11);
        mList.add(bean12);
        mList.add(bean13);

        BottomSheetAdapter adapter = new BottomSheetAdapter(mList);
        Info.setRecycviewAdapter(mContext,rc_bottom_sheet,adapter);
        adapter.setOnItemClickListner(new BaseRecyclerAdapter.OnItemClickListner() {
            @Override
            public void onItemClickListner(View v, int position) {
                Info.showToast(mContext,getResources().getString(mList.get(position).getTitle()),true);
                Info.playRingtone(mContext,true);
                isShow = false;
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
    }

    private class BottomSheetAdapter extends BaseRecyclerAdapter<BottomSheetBean>{

        BottomSheetAdapter(List<BottomSheetBean> mDatas) {
            super(mDatas, R.layout.item_bottom_sheet);
        }

        @Override
        protected void bindData(BaseViewHolder holder, int position, BottomSheetBean item) {
            ImageView iv_bottom_sheet = (ImageView) holder.getView(R.id.iv_bottom_sheet);
            TextView txt_bottom_sheet = (TextView) holder.getView(R.id.txt_bottom_sheet);

            Glide.with(mContext).load(item.getSrcId()).into(iv_bottom_sheet);
            txt_bottom_sheet.setText(item.getTitle());
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
