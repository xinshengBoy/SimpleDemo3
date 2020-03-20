package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.adapter.BaseRecyclerAdapter;
import com.yks.simpledemo3.bean.ChatBean;
import com.yks.simpledemo3.tools.TimeUtils;
import com.yks.simpledemo3.view.MyActionBar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述：仿微信聊天页面，左边是对方发的消息，右边是自己发的消息
 * 作者：zzh
 * time:2019/11/20
 */
public class ChatActivity extends Activity {

    private Context mContext = ChatActivity.this;
    private Activity mActivity = ChatActivity.this;
    private static final int SENDMESSAGESUCCESS = 0;

    private List<ChatBean> mList = new ArrayList<>();
    private ChatAdapter adapter;
    private MyHandler handler;

    private RecyclerView cv_chat_area;
    private EditText et_chat_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        handler = new MyHandler(mActivity);

        initView();
        initData();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity, title_layout, "微信聊天", "", false);

        cv_chat_area = findViewById(R.id.cv_chat_area);
        adapter = new ChatAdapter(mList);
        //横向滚动
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        cv_chat_area.setLayoutManager(manager);
        cv_chat_area.setAdapter(adapter);

        et_chat_content = findViewById(R.id.et_chat_content);
        TextView txt_chat_send = findViewById(R.id.txt_chat_send);
        txt_chat_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = et_chat_content.getText().toString().trim();
                if (!input.equals("")){
                    ChatBean bean = new ChatBean("华爷",input, TimeUtils.getCurTimeString(),true);
                    mList.add(bean);
                    handler.sendEmptyMessage(SENDMESSAGESUCCESS);
                    getReplay();
                }
            }
        });
    }

    private void initData() {
        ChatBean bean1 = new ChatBean("华爷","嘿，在干嘛呢？","2019-11-20 17:49:51",true);
        ChatBean bean2 = new ChatBean("小璐","刚下班，没干嘛，累...","2019-11-20 17:50:04",false);
        mList.add(bean1);
        mList.add(bean2);
        handler.sendEmptyMessage(SENDMESSAGESUCCESS);
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
                if (msg.what == SENDMESSAGESUCCESS){//发送消息
                    adapter.refresh(mList);
                    cv_chat_area.scrollToPosition(adapter.getItemCount()-1);
                    palyNotification();
                    et_chat_content.setText("");
//                    Info.hideKeyboard(mContext,et_chat_content);
                }
            }
        }
    }

    private class ChatAdapter extends BaseRecyclerAdapter<ChatBean>{

        ChatAdapter(List<ChatBean> mDatas) {
            super(mDatas, R.layout.item_chat);
        }

        @Override
        protected void bindData(BaseViewHolder holder, int position, ChatBean chatBean) {
            RelativeLayout other_chat_layout = (RelativeLayout) holder.getView(R.id.other_chat_layout);
            TextView txt_you_chatcontent = (TextView) holder.getView(R.id.txt_you_chatcontent);
            RelativeLayout my_chat_layout = (RelativeLayout) holder.getView(R.id.my_chat_layout);
            TextView txt_my_chatcontent = (TextView) holder.getView(R.id.txt_my_chatcontent);

            if (chatBean.isMySend()){
                my_chat_layout.setVisibility(View.VISIBLE);
                other_chat_layout.setVisibility(View.GONE);
                txt_my_chatcontent.setText(chatBean.getChatContent());
            }else {
                other_chat_layout.setVisibility(View.VISIBLE);
                my_chat_layout.setVisibility(View.GONE);
                txt_you_chatcontent.setText(chatBean.getChatContent());
            }
        }
    }

    /**
     * 描述：增加消息回复
     * 作者：zzh
     */
    private void getReplay(){
        String[] replay = {"想你了","我饿了","你怎么不理我","哼","我不开心了","快过来，我告诉你个秘密","啥时候下班呢","你有点坏","哎，没意思","能不能不熬夜","我们去打球吧","逛街怎么样","行，都听你的"};
        int index = (int) (Math.random()*replay.length);
        ChatBean bean2 = new ChatBean("小璐", replay[index], TimeUtils.getCurTimeString(),false);
        mList.add(bean2);
        handler.sendEmptyMessageDelayed(SENDMESSAGESUCCESS,2000);
    }

    /**
     * 描述：播放系统提示音
     * 作者：zzh
     */
    private void palyNotification(){
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone rt = RingtoneManager.getRingtone(mContext,uri);
        if (rt.isPlaying()) {
            rt.stop();
        }else {
            rt.play();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        if (mList != null && mList.size() != 0){
            mList.clear();
            mList = null;
        }
    }
}
