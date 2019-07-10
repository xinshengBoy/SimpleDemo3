package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.tools.ACache;
import com.yks.simpledemo3.tools.Info;

import net.lemonsoft.lemonbubble.LemonBubble;

/**
 * 描述：登录页面
 * 作者：zzh
 * time:2019/07/09
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    private Context mContext = LoginActivity.this;
    private Activity mActivity = LoginActivity.this;

    private EditText et_login_username,et_login_password;
    private ImageView iv_login_clear;
    private Button btn_login;

    private ACache acache;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //TODO 缓存用户名
        acache = ACache.get(mContext);
        initView();
    }

    private void initView() {
        TextView txt_login_version = findViewById(R.id.txt_login_version);
        txt_login_version.setText(Info.getVersionName(mContext));

        iv_login_clear = findViewById(R.id.iv_login_clear);
        et_login_username = findViewById(R.id.et_login_username);
        et_login_password = findViewById(R.id.et_login_password);
        btn_login = findViewById(R.id.btn_login);

        String cache = acache.getAsString(Info.LOGIN);
        if (cache != null && !cache.equals("")){
            String [] caches = cache.split(",");
            et_login_username.setText(caches[0]);
            et_login_username.setSelection(et_login_username.length());
            et_login_password.setText(caches[1]);
            et_login_password.setSelection(et_login_password.length());
        }

        iv_login_clear.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == iv_login_clear){
            et_login_username.setText("");
            et_login_username.setFocusable(true);
            et_login_username.setFocusableInTouchMode(true);
            et_login_username.requestFocus();
            et_login_username.setEnabled(true);
            et_login_password.setText("");
        }else if (view == btn_login){
            String userName = et_login_username.getText().toString().trim();
            String password = et_login_password.getText().toString().trim();
            if (userName.equals("") || password.equals("")){
                Info.showToast(mContext,"请输入用户名或密码",false);
                Info.playRingtone(mContext,false);
                return;
            }
            saveUserInfo(userName,password);
            Info.PERSONNAME = userName;
            startActivity(new Intent(mActivity,MainActivity.class));
            finish();
        }
    }

    /**
     * 描述：保存登录信息
     * 作者：zzh
     * @param userName 用户名
     * @param userPass 密码
     */
    private void saveUserInfo(String userName,String userPass){
        if (acache != null && !"".equals(acache.toString())){
            acache.remove(Info.LOGIN);
        }
        acache.put(Info.LOGIN,userName+","+userPass);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LemonBubble.forceHide();
        if (acache != null){
            acache = null;
        }
    }
}
