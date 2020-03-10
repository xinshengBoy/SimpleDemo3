package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.tools.ACache;
import com.yks.simpledemo3.tools.GlideImageLoader;
import com.yks.simpledemo3.tools.Info;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：欢迎页
 * 作者：zzh
 * time:2019/07/09
 */
public class SplashActivity extends Activity implements View.OnClickListener {

    private Context mContext = SplashActivity.this;
    private List<Integer> images = new ArrayList<>();
    private TextView txt_splash_finish;
    private ACache acache;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //TODO 缓存欢迎页是否已观看过
        acache = ACache.get(mContext);
        String saveVersion = acache.getAsString(Info.VERSION);
        String currentVersion = Info.getVersionName(mContext);
        if (saveVersion == null || saveVersion.equals("") || !saveVersion.equals(currentVersion)){//todo 假如历史版本与当前版本不一致时，要显示欢迎页
            acache.put(Info.VERSION,currentVersion);
            initView();
        }else {
            startActivity(new Intent(SplashActivity.this,LoginActivity.class));
            finish();
        }
    }

    private void initView() {
        Button btn_splash_skip = findViewById(R.id.btn_splash_skip);
        Banner banner_splash = findViewById(R.id.banner_splash);
        txt_splash_finish = findViewById(R.id.txt_splash_finish);

        images.add(R.mipmap.splash1);
        images.add(R.mipmap.splash2);
        images.add(R.mipmap.splash3);

        banner_splash.setImageLoader(new GlideImageLoader())
                .setImages(images)
                .isAutoPlay(false)
                .setIndicatorGravity(BannerConfig.CENTER)
                .start();
        banner_splash.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == images.size()-1){//滑到了最后一页
                    txt_splash_finish.setVisibility(View.VISIBLE);
                } else {
                    txt_splash_finish.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btn_splash_skip.setOnClickListener(this);
        txt_splash_finish.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(SplashActivity.this,LoginActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (acache != null){
            acache = null;
        }
    }
}
