package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.adapter.BaseRecyclerAdapter;
import com.yks.simpledemo3.bean.VerticalScrollCardBean;
import com.yks.simpledemo3.view.MyActionBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：垂直滑动卡片
 * 作者：zzh
 * time:2019/08/01
 * https://github.com/myml666/SnapHelperDemo/blob/master/app/src/main/java/com/itfitness/snaphelperdemo/MainActivity.java
 */
public class VerticalScrollCardActivity extends Activity {

    private Activity mActivity = VerticalScrollCardActivity.this;
    private Context mContext = VerticalScrollCardActivity.this;

    private List<VerticalScrollCardBean> mList = new ArrayList<>();

    private int [] icons = {R.drawable.ic_circle_menu,R.drawable.ic_csv_table,R.drawable.ic_timing_tasks,R.drawable.ic_temperature,
                            R.drawable.ic_water_picture,R.drawable.ic_windwill,R.drawable.ic_super_button};
    private String [] texts = {"旋转菜单","表格导出","定时请求","温度计","水印图片","大风车","超级按钮"};
    private int[] bgColors = {R.color.colorBlue,R.color.colorGreen,R.color.colorDeepRed,R.color.colorOrange,R.color.colorPrimary,R.color.colorDarkRed,R.color.pink};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_scroll_card);

        initView();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity, title_layout, "垂直滑动卡片", "", false);

        RecyclerView rv_vertical_scroll_card = findViewById(R.id.rv_vertical_scroll_card);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);//垂直方向
        rv_vertical_scroll_card.setLayoutManager(manager);

        PagerSnapHelper helper = new PagerSnapHelper();
        helper.attachToRecyclerView(rv_vertical_scroll_card);

        for (int i=0;i<icons.length;i++){
            VerticalScrollCardBean bean = new VerticalScrollCardBean();
            bean.setIcon(icons[i]);
            bean.setName(texts[i]);
            bean.setBgColor(bgColors[i]);
            mList.add(bean);
        }
        VerticalScrollCardAdapter adapter = new VerticalScrollCardAdapter(mList);
        rv_vertical_scroll_card.setAdapter(adapter);
    }

    private class VerticalScrollCardAdapter extends BaseRecyclerAdapter<VerticalScrollCardBean>{

        VerticalScrollCardAdapter(List<VerticalScrollCardBean> mDatas) {
            super(mDatas, R.layout.item_vertical_scroll_card);
        }

        @Override
        protected void bindData(BaseViewHolder holder, int position, VerticalScrollCardBean item) {
            RelativeLayout rl_vertical_scroll_card = (RelativeLayout) holder.getView(R.id.rl_vertical_scroll_card);
            ImageView iv_vertical_scroll_card = (ImageView) holder.getView(R.id.iv_vertical_scroll_card);
            TextView txt_vertical_scroll_card = (TextView) holder.getView(R.id.txt_vertical_scroll_card);

            rl_vertical_scroll_card.setBackgroundColor(getResources().getColor(item.getBgColor()));
            iv_vertical_scroll_card.setImageResource(item.getIcon());
            txt_vertical_scroll_card.setText(item.getName());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mList.clear();
    }
}
