package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.adapter.BaseRecyclerAdapter;
import com.yks.simpledemo3.bean.BluetoothReceivedFileBean;
import com.yks.simpledemo3.tools.FileUtils;
import com.yks.simpledemo3.tools.Info;
import com.yks.simpledemo3.tools.TimeUtils;
import com.yks.simpledemo3.view.MyActionBar;

import net.lemonsoft.lemonbubble.LemonBubble;
import net.lemonsoft.lemonhello.LemonHello;
import net.lemonsoft.lemonhello.LemonHelloAction;
import net.lemonsoft.lemonhello.LemonHelloInfo;
import net.lemonsoft.lemonhello.LemonHelloView;
import net.lemonsoft.lemonhello.interfaces.LemonHelloActionDelegate;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 描述：蓝牙已接收文件
 * 作者：zzh
 * time:2019/10/25
 */
public class BluetoothReceivedFileListActivity extends Activity {

    private Context mContext = BluetoothReceivedFileListActivity.this;
    private Activity mActivity = BluetoothReceivedFileListActivity.this;

    private final int GETBLUETOOTHRECEIVEDLISTSUCCESS = 0;//获取列表成功
    private final int GETBLUETOOTHRECEIVEDLISTFAIL = 1;//获取列表失败或列表内容为空
    private final int REMOVEALLPATHFILE = 2;//移除文件夹下的所有文件

    private RecyclerView rv_bluetooth_received;
    private TextView txt_bluetooth_tip3;
    private BluetoothReceivedAdapter adapter;
    private List<BluetoothReceivedFileBean> mList = new ArrayList<>();
    private MyHandler handler;
    private String path = Environment.getExternalStorageDirectory().getAbsolutePath() +"/bluetooth/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_received_list);

        handler = new MyHandler(mActivity);
        initView();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity, title_layout, "通过蓝牙接收的文件", "", false);

        rv_bluetooth_received = findViewById(R.id.rv_bluetooth_received);//已连接设备列表
        adapter = new BluetoothReceivedAdapter(mList);
        Info.setRecycviewAdapter(mContext,rv_bluetooth_received,adapter);

        txt_bluetooth_tip3 = findViewById(R.id.txt_bluetooth_tip3);
        LinearLayout layout_bluetooth_clearall = findViewById(R.id.layout_bluetooth_clearall);
        layout_bluetooth_clearall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LemonHello.getWarningHello("清空","确认清空文件夹下的所有文件？清空后不可恢复").addAction(new LemonHelloAction("取消", new LemonHelloActionDelegate() {
                    @Override
                    public void onClick(LemonHelloView lemonHelloView, LemonHelloInfo lemonHelloInfo, LemonHelloAction lemonHelloAction) {
                        lemonHelloView.hide();
                    }
                })).addAction(new LemonHelloAction("清空", new LemonHelloActionDelegate() {
                    @Override
                    public void onClick(LemonHelloView lemonHelloView, LemonHelloInfo lemonHelloInfo, LemonHelloAction lemonHelloAction) {
                        lemonHelloView.hide();
                        Info.showProgress(mContext,"删除中...");
                        boolean isSuccess = FileUtils.deleteDirectory(path);
                        if (isSuccess){
                            handler.sendEmptyMessage(REMOVEALLPATHFILE);
                        }else {
                            LemonBubble.hide();
                        }
                    }
                })).show(mContext);
            }
        });
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
                if (msg.what == GETBLUETOOTHRECEIVEDLISTSUCCESS) {//todo 获取列表成功
                    LemonBubble.hide();
                    adapter.refresh(mList);
                    rv_bluetooth_received.setVisibility(View.VISIBLE);
                    txt_bluetooth_tip3.setVisibility(View.GONE);
                    Info.playRingtone(mContext,true);
                }else if (msg.what == GETBLUETOOTHRECEIVEDLISTFAIL){//todo 获取列表失败或暂无已接收文件
                    LemonBubble.hide();
                    adapter.refresh(mList);
                    rv_bluetooth_received.setVisibility(View.GONE);
                    txt_bluetooth_tip3.setVisibility(View.VISIBLE);

                    Info.playRingtone(mContext,false);
                }else if (msg.what == REMOVEALLPATHFILE){
                    LemonBubble.hide();
                    mList.clear();
                    adapter.refresh(mList);
                    Info.playRingtone(mContext,true);
                }
            }
        }
    }

    private class BluetoothReceivedAdapter extends BaseRecyclerAdapter<BluetoothReceivedFileBean> {

        BluetoothReceivedAdapter(List<BluetoothReceivedFileBean> mDatas) {
            super(mDatas, R.layout.item_bluetooth_received);
        }

        @Override
        protected void bindData(BaseViewHolder holder, final int position, final BluetoothReceivedFileBean bean) {
            TextView filename = (TextView) holder.getView(R.id.txt_item_bluetooth_received_filename);
            TextView filesize = (TextView) holder.getView(R.id.txt_item_bluetooth_received_filesize);
            TextView filetime = (TextView) holder.getView(R.id.txt_item_bluetooth_received_filetime);

            filename.setText(bean.getFileName());
            filesize.setText(bean.getFileSize());
            filetime.setText(bean.getFileTime());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mList != null && mList.size() != 0){
            mList.clear();
        }
        listFileSortByModifyTime(path);
    }

    /**
     * 描述：获取目录下所有文件(按时间排序)
     * 作者：zzh
     * @param path 要遍历的地址
     */
    public void listFileSortByModifyTime(String path) {
        Info.showProgress(mContext,"查找中...");
        List<File> list = getFiles(path, new ArrayList<File>());
        if (list != null && list.size() > 0) {
            Collections.sort(list, new Comparator<File>() {
                public int compare(File file, File newFile) {
                    if (file.lastModified() < newFile.lastModified()) {
                        return -1;
                    } else if (file.lastModified() == newFile.lastModified()) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
            });
            handler.sendEmptyMessage(GETBLUETOOTHRECEIVEDLISTSUCCESS);
        }else {
            handler.sendEmptyMessage(GETBLUETOOTHRECEIVEDLISTFAIL);
        }
    }

    /**
     *
     * 获取目录下所有文件
     * @param realpath
     * @param files
     * @return
     */
    public  List<File> getFiles(String realpath, List<File> files) {
        File realFile = new File(realpath);
        if (realFile.isDirectory()) {
            File[] subfiles = realFile.listFiles();
            for (File file : subfiles) {
                if (file.isDirectory()) {
                    getFiles(file.getAbsolutePath(), files);
                } else {
                    BluetoothReceivedFileBean bean = new BluetoothReceivedFileBean();
                    bean.setFileName(file.getName());
                    bean.setFileTime(TimeUtils.milliseconds2String(file.lastModified()));
                    bean.setFileSize(FileUtils.getFileSizeAndUnit(file));
                    bean.setFileAddress(file.getAbsolutePath());
                    mList.add(bean);
                    files.add(file);
                }
            }
        }
        return files;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LemonBubble.hide();
        if (mList != null && mList.size() != 0){
            mList.clear();
            mList = null;
        }
    }
}
