package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.luozm.captcha.Captcha;
import com.yks.simpledemo3.R;
import com.yks.simpledemo3.view.MyActionBar;

/**
 * 描述：图片滑动验证
 * 作者：zzh
 * time:2019/08/08
 * 参考：https://github.com/luozhanming/Captcha
 */
public class ScrollCheckActivity extends Activity {

    private Activity mActivity = ScrollCheckActivity.this;
    private Context mContext = ScrollCheckActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_check);

        initView();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity,title_layout,"滑动验证","",false);

        final Captcha view_captcha1 = findViewById(R.id.view_captcha1);
        view_captcha1.setCaptchaListener(new Captcha.CaptchaListener() {
            @Override
            public String onAccess(long time) {
                return "验证成功，花费："+(time/1000d)+"秒";
            }

            @Override
            public String onFailed(int failCount) {
                return "验证失败，还剩："+(view_captcha1.getMaxFailedCount()-failCount)+"次！";
            }

            @Override
            public String onMaxFailed() {
                return "验证失败，账号已封锁，一分钟之后再次验证！";
            }
        });
    }
}
