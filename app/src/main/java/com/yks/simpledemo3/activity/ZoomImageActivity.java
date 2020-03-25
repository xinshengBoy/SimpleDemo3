package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.view.MyActionBar;
import com.yks.simpledemo3.view.ZoomImageView;

import java.util.Random;

/**
 * 描述：图片放大镜
 * 作者：zzh
 * time:2019/12/24
 * https://github.com/baopengjian/Ray_SeniorUI/blob/master/app/src/main/java/com/example/baopengjian/ray_seniorui/fourth/view/ZoomImageView.java
 */
public class ZoomImageActivity extends Activity {

    private ZoomImageView zoomImageView;
    private int[] images = {R.mipmap.splash1,R.mipmap.splash2,R.mipmap.splash3,R.mipmap.bg,R.mipmap.banner1,R.mipmap.banner2,R.mipmap.banner3,R.mipmap.banner4,R.mipmap.banner5};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image);

        initView();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(this, title_layout, "图片放大镜", "", false);

        LinearLayout view_zoom_image = findViewById(R.id.view_zoom_image);
        zoomImageView = new ZoomImageView(this);
        view_zoom_image.addView(zoomImageView);
    }

    public void changeZoomImages(View view){
        Random random = new Random();
        zoomImageView.setImageId(images[random.nextInt(images.length)]);
    }
}
