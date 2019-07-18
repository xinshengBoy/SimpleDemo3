package com.yks.simpledemo3.activity;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.telephony.SmsMessage;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.tools.Info;
import com.yks.simpledemo3.view.MyActionBar;

import java.lang.ref.WeakReference;

import kr.co.namee.permissiongen.PermissionGen;

/**
 * 描述：短信验证码的读取
 * 作者：zzh
 * time:2019/07/17
 */
public class MessageVerificationActivity extends Activity {

    private Activity mActivity = MessageVerificationActivity.this;
    private Context mContext = MessageVerificationActivity.this;

    private final int GETSMSCODE = 0;//获取到验证码
    private EditText et_message_verification;
    private MyHandler handler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_verification);

        handler = new MyHandler(mActivity);
        initView();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(MessageVerificationActivity.this,title_layout,"短信验证码","",false);

        et_message_verification = findViewById(R.id.et_message_verification);

        SMSBroadcastReceiver receiver = new SMSBroadcastReceiver();
        IntentFilter filter = new IntentFilter(SMSBroadcastReceiver.SMS_RECEIVED_ACTION);
        filter.setPriority(Integer.MAX_VALUE);
        registerReceiver(receiver,filter);
        PermissionGen.needPermission(mActivity,100,new String[]{Manifest.permission.RECEIVE_SMS,Manifest.permission.READ_SMS});

        receiver.setOnReceiveSMSListener(new SMSBroadcastReceiver.OnReceiveSMSListener() {
            @Override
            public void onReceived(String message) {
                et_message_verification.setText(message);
                Toast.makeText(mContext,"验证码："+message,Toast.LENGTH_LONG).show();
                Info.showToast(mContext,message,true);
                Info.playRingtone(mContext,true);
            }
        });
    }

    private class MyHandler extends Handler{
        final WeakReference<Activity> mWeakReference;

        MyHandler(Activity activity){
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mWeakReference.get() != null){
//                if (msg.what == GETSMSCODE){
//                    Bundle bundle = msg.getData();
//                    et_message_verification.setText(bundle.getString("code"));
//                    Toast.makeText(mContext,"验证码："+bundle.getString("code"),Toast.LENGTH_LONG).show();
//                    Info.showToast(mContext,bundle.getString("code"),true);
//                    Info.playRingtone(mContext,true);
//                }
            }
        }
    }

    public static class SMSBroadcastReceiver extends BroadcastReceiver{

        private OnReceiveSMSListener mOnReceiveSMSListener;
        public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SMS_RECEIVED_ACTION)){
                Object[] pdus = (Object[]) intent.getExtras().get("pdus");
                for (Object pdu : pdus){
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                    String sender = smsMessage.getDisplayOriginatingAddress();//号码
                    String content = smsMessage.getDisplayMessageBody();//短信内容
                    if (mOnReceiveSMSListener != null && !content.equals(""))  {
//                        Pattern pattern = Pattern.compile("(\\d{6})");//连续6位数字
//                        Matcher matcher = pattern.matcher(content);
//                        if (matcher.find()){
//                            mOnReceiveSMSListener.onReceived(matcher.group());
//                        }
                        mOnReceiveSMSListener.onReceived(content);
                        abortBroadcast();
                    }
                }
            }
        }

        public interface OnReceiveSMSListener {
            void onReceived(String message);
        }

        public void setOnReceiveSMSListener(OnReceiveSMSListener listener){
            mOnReceiveSMSListener = listener;
        }
    }
}
