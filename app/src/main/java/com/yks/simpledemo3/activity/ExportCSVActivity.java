package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.adapter.BaseRecyclerAdapter;
import com.yks.simpledemo3.bean.CsvBean;
import com.yks.simpledemo3.tools.Info;
import com.yks.simpledemo3.view.MyActionBar;

import net.lemonsoft.lemonbubble.LemonBubble;
import net.lemonsoft.lemonhello.LemonHello;
import net.lemonsoft.lemonhello.LemonHelloAction;
import net.lemonsoft.lemonhello.LemonHelloInfo;
import net.lemonsoft.lemonhello.LemonHelloView;
import net.lemonsoft.lemonhello.interfaces.LemonHelloActionDelegate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述：导出csv文件
 * 作者：zzh
 * time:2019/07/30
 * https://www.cnblogs.com/huangzhen22/p/5025922.html
 */
public class ExportCSVActivity extends Activity implements View.OnClickListener, View.OnLayoutChangeListener {

    private Activity mActivity = ExportCSVActivity.this;
    private Context mContext = ExportCSVActivity.this;

    private MyHandler handler;
    private final int INPUTERROR = 0;
    private final int INPUTSUCCESS = 1;
    private final int EXPORTCSV = 2;
    private final int EXPORTSUCCESS = 3;

    private RelativeLayout layouts;
    private EditText et_csv_name,et_csv_age;
    private ImageView iv_csv_save;
    private Button btn_export_csv;
    private CsvInfoAdapter adapter;
    private List<CsvBean> mList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_csv);

        handler = new MyHandler(mActivity);
        //获取屏幕高度，用于监听软键盘的弹起
        Info.getScreenHeight(mActivity);
        initView();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity, title_layout, "CSV导出", "", false);

        layouts = findViewById(R.id.layouts);
        et_csv_name = findViewById(R.id.et_csv_name);
        et_csv_age = findViewById(R.id.et_csv_age);
        iv_csv_save = findViewById(R.id.iv_csv_save);
        btn_export_csv = findViewById(R.id.btn_export_csv);

        RecyclerView cv_export_csv = findViewById(R.id.cv_export_csv);
        adapter = new CsvInfoAdapter(mList);
        Info.setRecycviewAdapter(mContext,cv_export_csv,adapter);

        iv_csv_save.setOnClickListener(this);
        btn_export_csv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == iv_csv_save){
            String name = et_csv_name.getText().toString().trim();
            String age = et_csv_age.getText().toString().trim();
            if (name.equals("")){
                sendMessage(INPUTERROR,"请输入姓名");
                return;
            }

            if (age.equals("")){
                sendMessage(INPUTERROR,"请输入年龄");
                return;
            }
            mList.add(0,new CsvBean(name,age));
            handler.sendEmptyMessage(INPUTSUCCESS);
        }else if (v == btn_export_csv){
            if (mList.size() == 0){
                sendMessage(INPUTERROR,"数据为空！");
                return;
            }
            Info.showProgress(mContext,"导出中...");
            handler.sendEmptyMessage(EXPORTCSV);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        layouts.addOnLayoutChangeListener(this);
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
                if (msg.what == INPUTERROR){
                    Bundle bundle = msg.getData();
                    Info.showToast(mContext, bundle.getString("msg"), false);
                    Info.playRingtone(mContext, false);
                }else if (msg.what == INPUTSUCCESS){
                    adapter.refresh(mList);
                    Info.playRingtone(mContext,true);
                    et_csv_name.setText("");
                    et_csv_name.setFocusable(true);
                    et_csv_name.setFocusableInTouchMode(true);
                    et_csv_name.requestFocus();
                    et_csv_name.setEnabled(true);
                    et_csv_age.setText("");
                }else if (msg.what == EXPORTCSV){
                    final List<String> list = new ArrayList<>();
                    list.add("姓名,年龄");
                    for (int i=0;i<mList.size();i++){
                        list.add(mList.get(i).getName()+","+mList.get(i).getAge());
                    }
                    String result = exportCsv(Environment.getExternalStorageDirectory().getPath()+"/yujisofile/"+System.currentTimeMillis()+".csv",list);
                    list.clear();
                    if (result.contains("导出失败")){
                        Info.showToast(mContext, result, false);
                        Info.playRingtone(mContext, false);
                    }else {
                        LemonBubble.hide();
                        Message message = new Message();
                        message.what = EXPORTSUCCESS;
                        Bundle bundle = new Bundle();
                        bundle.putString("result",result);
                        message.setData(bundle);
                        handler.sendMessageDelayed(message,300);
                    }
                }else if (msg.what == EXPORTSUCCESS){
                    Bundle bundle = msg.getData();
                    LemonHello.getSuccessHello("成功",bundle.getString("result")).addAction(new LemonHelloAction("确定", new LemonHelloActionDelegate() {
                        @Override
                        public void onClick(LemonHelloView lemonHelloView, LemonHelloInfo lemonHelloInfo, LemonHelloAction lemonHelloAction) {
                            mList.clear();
                            adapter.refresh(mList);
                            lemonHelloView.hide();
                        }
                    })).show(mContext);
                    Info.playRingtone(mContext,true);
                }
            }
        }
    }

    private class CsvInfoAdapter extends BaseRecyclerAdapter<CsvBean>{

        CsvInfoAdapter(List<CsvBean> mDatas) {
            super(mDatas, R.layout.item_csv);
        }

        @Override
        protected void bindData(BaseViewHolder holder, int position, CsvBean csvBean) {
            TextView txt_item_csvname = (TextView) holder.getView(R.id.txt_item_csvname);
            TextView txt_item_csvage = (TextView) holder.getView(R.id.txt_item_csvage);

            txt_item_csvname.setText(csvBean.getName());
            txt_item_csvage.setText(csvBean.getAge());
        }
    }

    /**
     * 描述：导出csv
     * @param path 存储路径
     * @param list zzh
     * @return 是否成功
     */
    private String exportCsv(String path,List<String> list){
        File file = new File(path);
        boolean isSuccess = false;
        FileOutputStream out = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try {
            out = new FileOutputStream(file);
            osw = new OutputStreamWriter(out);
            bw = new BufferedWriter(osw);
            if (list != null && !list.isEmpty()){
                for (String data : list){
                    bw.append(data).append("\r\n");
                }
            }
            isSuccess = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (bw != null){
                try {
                    bw.close();
                    bw = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (osw != null){
                try {
                    osw.close();
                    osw = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null){
                try {
                    out.close();
                    out = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (isSuccess){
            return "导出成功，存储路径为："+path;
        }else {
            return "导出失败";
        }
    }

    /**
     * 描述：发handler消息
     * 作者：zzh
     * @param id 需要进入到的handler
     * @param msg 传递的消息
     */
    private void sendMessage(int id, String msg) {
        Bundle bundle = new Bundle();
        bundle.putString("msg",msg);
        Message message = new Message();
        message.what = id;
        message.setData(bundle);
        handler.sendMessage(message);
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if(oldBottom != 0 && bottom != 0 &&(oldBottom - bottom > Info.KEY_HEIGHT)){
            //键盘弹起
            btn_export_csv.setVisibility(View.GONE);
        }else if(oldBottom != 0 && bottom != 0 &&(bottom - oldBottom > Info.KEY_HEIGHT)){
            //键盘隐藏
            btn_export_csv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LemonBubble.forceHide();
        handler.removeCallbacksAndMessages(null);
        if (mList != null && mList.size() != 0){
            mList.clear();
            mList = null;
        }
    }
}
