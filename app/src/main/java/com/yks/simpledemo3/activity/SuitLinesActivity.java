package com.yks.simpledemo3.activity;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.LinearLayout;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.adapter.OpenAppService;
import com.yks.simpledemo3.bean.SuitUnit;
import com.yks.simpledemo3.tools.Info;
import com.yks.simpledemo3.view.MyActionBar;
import com.yks.simpledemo3.view.SplashView;
import com.yks.simpledemo3.view.SuitLines;

import net.lemonsoft.lemonbubble.LemonBubble;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import kr.co.namee.permissiongen.PermissionGen;

/**
 * 描述：曲线图
 * 作者：zzh
 * time:2019/12/20
 * https://github.com/whataa/SuitLines/blob/master/lib/src/main/java/tech/linjiang/suitlines/SuitLines.java
 */
public class SuitLinesActivity extends Activity {

    private Context mContext = SuitLinesActivity.this;
    private Activity mActivity = SuitLinesActivity.this;
    private SplashView splashView;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suitlines);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("openapp");
        registerReceiver(AutoOpenAppReceiver,intentFilter);
        initView();
    }

    private void startLoadData(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                splashView.splashDisappear();
            }
        },3000);
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity, title_layout, "曲线图", "", false);

        splashView = new SplashView(mContext);

        title_layout.addView(splashView);
        startLoadData();

        SuitLines view_suitlines1 = findViewById(R.id.view_suitlines1);

        List<SuitUnit> lines = new ArrayList<>();
        lines.add(new SuitUnit(12,"1"));
        lines.add(new SuitUnit(22,"2"));
        lines.add(new SuitUnit(20,"3"));
        lines.add(new SuitUnit(18,"4"));
        lines.add(new SuitUnit(17,"5"));
        lines.add(new SuitUnit(19,"6"));
        lines.add(new SuitUnit(14,"7"));
        lines.add(new SuitUnit(18,"8"));
        lines.add(new SuitUnit(19,"9"));

        view_suitlines1.feedWithAnim(lines);
    }

    /**
     * 描述：开启广播，定时自动打开另一个app
     * 作者：zzh
     */
    private BroadcastReceiver AutoOpenAppReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("openapp")) {
                Intent intent1 = new Intent(context, OpenAppService.class);
                context.startService(intent1);
            }
        }
    };

    /**
     * 描述：定时打开第三方应用
     * 作者：zzh
     * @param view 按钮
     */
    public void timingOpenThirdApp(View view){
        Intent intent = new Intent();
        intent.setAction("openapp");
        sendBroadcast(intent);
    }
    /**
     * 描述：调起第三方支持PDF阅读的应用打开PDF文档
     * 备注：将sd卡的文件资源共享到第三方应用打开，需要在androidManifest配置文件中新增provider配置方可打开
     * @param view 按钮
     */
    public void openPDF(View view){
        PermissionGen.needPermission(mActivity,100,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE});
        String path = Environment.getExternalStorageDirectory()+ "/yks/IQ.pdf";
        File file = new File(path);
        if (file.exists()){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = FileProvider.getUriForFile(mContext,mContext.getApplicationContext().getPackageName()+".provider",new File(path));
            intent.setDataAndType(uri,"application/pdf");
            startActivity(intent);
        }else {
            Info.showToast(mContext,"文件不存在",false);
            Info.playRingtone(mContext,false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LemonBubble.forceHide();
        unregisterReceiver(AutoOpenAppReceiver);
    }
}
