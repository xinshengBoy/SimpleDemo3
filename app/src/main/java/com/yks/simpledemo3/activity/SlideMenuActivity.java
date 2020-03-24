package com.yks.simpledemo3.activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.mzule.fantasyslide.SideBar;
import com.github.mzule.fantasyslide.Transformer;
import com.yks.simpledemo3.R;
import com.yks.simpledemo3.view.MyActionBar;

/**
 * 描述：侧边菜单
 * 作者：zzh
 * time:2020/03/24
 * https://github.com/mzule/FantasySlide
 * 体验不好
 */
public class SlideMenuActivity extends Activity {

    private Context mContext = SlideMenuActivity.this;
    private Activity mActivity = SlideMenuActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_menu);

        initView();
        setTransformer();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity, title_layout, "侧边菜单", "", false);
    }

    private void setTransformer(){
        final float spacing = getResources().getDimension(R.dimen.slide_menu_spacing);
        SideBar rightSlideBar = findViewById(R.id.rightSlideBar);
        rightSlideBar.setTransformer(new Transformer() {

            private View lastHoverView;
            @Override
            public void apply(ViewGroup sideBar, View itemView, float touchY, float slideOffset, boolean isLeft) {
                boolean hoverd = itemView.isPressed();
                if (hoverd && lastHoverView != itemView){
                    animateIn(itemView);
                    animateOut(lastHoverView);
                    lastHoverView = itemView;
                }
            }

            private void animateIn(View view){
                ObjectAnimator translationX = ObjectAnimator.ofFloat(view,"translationX",0,-spacing);
                translationX.setDuration(200);
                translationX.start();
            }

            private void animateOut(View view){
                if (view == null){
                    return;
                }
                ObjectAnimator translationX = ObjectAnimator.ofFloat(view,"translationX",-spacing,0);
                translationX.setDuration(200);
                translationX.start();
            }
        });
    }
}
