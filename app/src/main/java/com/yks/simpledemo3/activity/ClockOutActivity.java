package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.yks.simpledemo3.R;
import com.yks.simpledemo3.adapter.BaseRecyclerAdapter;
import com.yks.simpledemo3.database.ClockOutModel;
import com.yks.simpledemo3.tools.Info;
import com.yks.simpledemo3.view.MyActionBar;

import net.lemonsoft.lemonbubble.LemonBubble;
import net.lemonsoft.lemonhello.LemonHello;
import net.lemonsoft.lemonhello.LemonHelloAction;
import net.lemonsoft.lemonhello.LemonHelloInfo;
import net.lemonsoft.lemonhello.LemonHelloView;
import net.lemonsoft.lemonhello.interfaces.LemonHelloActionDelegate;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述：考勤打卡
 * 作者：zzh
 * time:2020/08/06
 * 数据库参考网址：https://github.com/kaka10xiaobang/DBFLOW_DEMO/blob/master/app/src/main/java/com/kaka/dbflowdemo/MainActivity.java
 */
public class ClockOutActivity extends Activity {

    private Activity mActivity = ClockOutActivity.this;
    private Context mContext = ClockOutActivity.this;

    private final int SUCCESS_RECORD = 0;

    private TextClock txt_clock_time;
    private List<ClockOutModel> list = new ArrayList<>();
    private MyHandler handler;
    private ClockOutRecordAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_out);

        handler = new MyHandler(mActivity);
        initView();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity, title_layout, "考勤打卡", "", false);

        ImageView iv_clock_time = findViewById(R.id.iv_clock_time);
        txt_clock_time = findViewById(R.id.txt_clock_time);
        RecyclerView rv_clock_out = findViewById(R.id.rv_clock_out);
        adapter = new ClockOutRecordAdapter(list);
        Info.setRecycviewAdapter(mContext,rv_clock_out,adapter);
        iv_clock_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String times = txt_clock_time.getText().toString();
                insertNote(times,"");
                Info.showToast(mContext,times+"，打卡成功！",true);
            }
        });

        //先查询之前的数据
        queryList();

        //清空
        TextView txt_clear_all = findViewById(R.id.txt_clear_all);
        txt_clear_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LemonHello.getWarningHello("清空","确认清空所有数据？").addAction(new LemonHelloAction("取消", new LemonHelloActionDelegate() {
                    @Override
                    public void onClick(LemonHelloView lemonHelloView, LemonHelloInfo lemonHelloInfo, LemonHelloAction lemonHelloAction) {
                        lemonHelloView.hide();
                    }
                })).addAction(new LemonHelloAction("确定", new LemonHelloActionDelegate() {
                    @Override
                    public void onClick(LemonHelloView lemonHelloView, LemonHelloInfo lemonHelloInfo, LemonHelloAction lemonHelloAction) {
                        SQLite.delete().from(ClockOutModel.class).execute();
                        lemonHelloView.hide();
                        queryList();
                    }
                })).show(mContext);
            }
        });
    }

    private void insertNote(String time,String remark){
        ClockOutModel model = new ClockOutModel();
        model.setClockTime(time);
        model.setClockRemark(remark);
        long result = model.insert();
        if (result > 0){
            //插入成功
            queryList();
        }else {
            insertNote(time,remark);
        }
    }

    private void queryList(){
        list = SQLite.select().from(ClockOutModel.class).orderBy(OrderBy.fromNameAlias(NameAlias.of("id"))).queryList();
        handler.sendEmptyMessage(SUCCESS_RECORD);
    }

    private class MyHandler extends Handler {
        final WeakReference<Activity> mWeakReference;

        MyHandler(Activity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mWeakReference.get() != null) {
                if (msg.what == SUCCESS_RECORD) {//记录成功
                    adapter.refresh(list);
                }
            }
        }
    }

    private class ClockOutRecordAdapter extends BaseRecyclerAdapter<ClockOutModel>{

        ClockOutRecordAdapter(List<ClockOutModel> mDatas) {
            super(mDatas, R.layout.item_clock_out);
        }

        @Override
        protected void bindData(BaseViewHolder holder, int position, final ClockOutModel item) {
            LinearLayout ll_item_clock_out = (LinearLayout) holder.getView(R.id.ll_item_clock_out);
            TextView txt_item_clock_out_time = (TextView) holder.getView(R.id.txt_item_clock_out_time);
            final EditText et_item_clock_out_remark = (EditText) holder.getView(R.id.et_item_clock_out_remark);
            ImageView iv_item_clock_out_save = (ImageView) holder.getView(R.id.iv_item_clock_out_save);

            txt_item_clock_out_time.setText(item.getClockTime());
            et_item_clock_out_remark.setText(item.getClockRemark());
            et_item_clock_out_remark.setSelection(et_item_clock_out_remark.length());

            ll_item_clock_out.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    boolean result = item.delete();
                    if (result){
                        queryList();
                    }
                    Info.hideKeyboard(mContext,et_item_clock_out_remark);
                    return false;
                }
            });

            iv_item_clock_out_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item.setClockRemark(et_item_clock_out_remark.getText().toString().trim());
                    boolean result = item.update();
                    if (result){
                        queryList();
                    }
                    Info.hideKeyboard(mContext,et_item_clock_out_remark);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LemonBubble.forceHide();
        handler.removeCallbacksAndMessages(null);
        if (list != null && list.size() != 0){
            list.clear();
            list = null;
        }
    }
}
