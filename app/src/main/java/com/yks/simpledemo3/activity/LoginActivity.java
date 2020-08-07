package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.yks.simpledemo3.R;
import com.yks.simpledemo3.tools.ACache;
import com.yks.simpledemo3.tools.EncrypDES;
import com.yks.simpledemo3.tools.Info;

import net.lemonsoft.lemonbubble.LemonBubble;

import org.json.JSONException;
import org.json.JSONObject;

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
    private EncrypDES des;
    private long exitTime = 0;//退出时间
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //TODO 缓存用户名
        acache = ACache.get(mContext);
        //todo 加密解密
        try {
            des = new EncrypDES();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Hawk.init(mContext).build();
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
        String cachess = Hawk.get(Info.LOGIN);
        if (cache != null && !cache.equals("")){
            //todo 拿到数据后进行解密，获取原始保存值
            try {
                cache = des.decrypt(cache);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String [] caches = cache.split(",");
            et_login_username.setText(caches[0]);
            et_login_username.setSelection(et_login_username.length());
            et_login_password.setText(caches[1]);
            et_login_password.setSelection(et_login_password.length());
        }

        iv_login_clear.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        JSONObject object = acache.getAsJSONObject(Info.PDA_LOG);
        if (object != null){
            try {
                String a = object.getString("operator")+"\n"+object.getString("personName")+"\n"+object.getString("error");
                Log.d("myPdaLog",a);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
        try {
            //todo 保存前先进行一个加密操作
            String info = des.encrypt(userName+","+userPass);
            Hawk.put(Info.LOGIN,info);
            acache.put(Info.LOGIN,info);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if ((System.currentTimeMillis() - exitTime) > 2000){
                Toast.makeText(mContext,"再按一次退出程序",Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }else {
                mActivity.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
