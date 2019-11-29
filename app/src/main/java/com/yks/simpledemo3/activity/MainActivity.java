package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.PermissionListener;
import com.yhao.floatwindow.Screen;
import com.yhao.floatwindow.ViewStateListener;
import com.yks.simpledemo3.R;
import com.yks.simpledemo3.tools.GlideImageLoader;
import com.yks.simpledemo3.tools.Info;
import com.yks.simpledemo3.view.MenuItemView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import net.lemonsoft.lemonhello.LemonHello;
import net.lemonsoft.lemonhello.LemonHelloAction;
import net.lemonsoft.lemonhello.LemonHelloInfo;
import net.lemonsoft.lemonhello.LemonHelloView;
import net.lemonsoft.lemonhello.interfaces.LemonHelloActionDelegate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private final Context mContext = MainActivity.this;
    private final Activity mActivity = MainActivity.this;

    private DrawerLayout drawer;
    private TextView txt_current_time;
    private Button btn_loginout;
    private RelativeLayout cache_layout;

    private List<Integer> images = new ArrayList<>();
    private long exitTime = 0;//退出时间
    //跳转集合
    private final int [] ids = new int[]{R.id.channel_sort_layout,R.id.drawable_layout,R.id.appBarLayout_layout,R.id.safeKeyBoard_layout,
                                        R.id.message_verification_layout,R.id.xiaomi_stepclock_layout,R.id.dialog_fragment_layout,R.id.windwill_layout,
                                        R.id.super_button_layout,R.id.water_picture_layout,R.id.timing_tasks_layout,R.id.export_csv_layout,
                                        R.id.temperature_layout,R.id.circle_menu_layout,R.id.radar_layout,R.id.marquee_layout,
                                        R.id.custom_clock_layout,R.id.scroll_unlock_layout,R.id.xpop_layout,R.id.scroll_check_layout,
                                        R.id.search_intime_layout,R.id.number_rain_layout,R.id.run_number_layout,R.id.bottom_sheet_layout,
                                        R.id.compass_clock_layout,R.id.custom_ruler_layout,R.id.city_location_layout,R.id.bluetooth_layout,
                                        R.id.music_lrc_layout,R.id.movie_seat_layout,R.id.time_picker_layout,R.id.auto_marquee_layout,
                                        R.id.sudo_layout,R.id.sound_record_layout,R.id.chat_layout,R.id.create_form_layout,
                                        R.id.record_footline_layout,R.id.wether_report_layout};
    private final Class [] classes = new Class[]{ChannelSortActivity.class,DrawableActivity.class,AppBayLayoutActivity.class,SafeKeyBoardActivity.class,
                                                MessageVerificationActivity.class,XiaomiStepClockActivity.class,DialogFragmentActivity.class,WindWillActivity.class,
                                                SuperButtonActivity.class,WaterPictureActivity.class,TimingTaskActivity.class,ExportCSVActivity.class,
                                                TemperatureActivity.class,CircleMenuActivity.class, RadarViewActivity.class,MarqueeActivity.class,
                                                CustomClockActivity.class,ScrollUnLockActivity.class,XPopActivity.class,ScrollCheckActivity.class,
                                                SearchInTimeActivity.class,NumberRainActivity.class,RunNumberActivity.class,BottomSheetActivity.class,
                                                CompassClockActivity.class,CustomRulerActivity.class,CityLocationActivity.class,BluetoothActivity.class,
                                                MusicLrcActivity.class,MovieSeatActivity.class,TimePickerActivity.class,AutoMarqueeActivity.class,
                                                SudoActivity.class,SoundRecordActivity.class,ChatActivity.class,CreateFormActivity.class,
                                                RecordFootLineActivity.class,WeatherReportActivity.class};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();
    }
    //初始化广告栏的图片资源
    private void initData(){
        images.add(R.mipmap.banner1);
        images.add(R.mipmap.banner2);
        images.add(R.mipmap.banner3);
        images.add(R.mipmap.banner4);
        images.add(R.mipmap.banner5);
        images.add(R.mipmap.banner6);
        images.add(R.mipmap.banner7);
        images.add(R.mipmap.banner8);
        images.add(R.mipmap.banner9);
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);

        drawer = findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);

        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.me));
        toolbar.setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        drawer.setDrawerListener(toggle);
        //todo 实现广告栏
        Banner banner_main = findViewById(R.id.banner_main);
        banner_main.setImageLoader(new GlideImageLoader())
                .setImages(images)
                .isAutoPlay(true)
                .setBannerAnimation(Transformer.DepthPage)
                .setDelayTime(3000)
                .setIndicatorGravity(BannerConfig.CENTER)
                .start();

        banner_main.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Info.showToast(mContext,"大爷您来啦！我是"+(position+1)+"号",true);
                playClickTip(position+1);
            }
        });
        //todo 处理每个功能的点击事件
        for (int i=0;i<ids.length;i++){
            final MenuItemView layout = findViewById(ids[i]);
            final int finalPosition = i;
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Info.PERSONNAME.equals("")){
                        LemonHello.getErrorHello("提示","登录过期，请重新登录").addAction(new LemonHelloAction("确定", new LemonHelloActionDelegate() {
                            @Override
                            public void onClick(LemonHelloView lemonHelloView, LemonHelloInfo lemonHelloInfo, LemonHelloAction lemonHelloAction) {
                                startActivity(new Intent(mActivity,LoginActivity.class));
                                mActivity.finish();
                                lemonHelloView.hide();
                            }
                        })).show(mContext);
                        return;
                    }
                    startActivity(new Intent(mActivity, classes[finalPosition]));
                }
            });
        }
        //当前时间
        txt_current_time = findViewById(R.id.txt_current_time);
        txt_current_time.setText(getCurrentTime());
        //用户名（中文名）
        TextView txt_workname = findViewById(R.id.txt_workname);
        txt_workname.setText(Info.PERSONNAME);
        //缓存
        TextView txt_cache = findViewById(R.id.txt_cache);
        txt_cache.setText("2M");
        //清除缓存
        cache_layout = findViewById(R.id.cache_layout);
        cache_layout.setOnClickListener(this);
        //版本号
        TextView txt_appversion = findViewById(R.id.txt_appversion);
        txt_appversion.setText(Info.getVersionName(mContext));
        btn_loginout = findViewById(R.id.btn_loginout);
        btn_loginout.setOnClickListener(this);

        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(R.mipmap.ai_top);
        FloatWindow.with(getApplicationContext())
                .setView(imageView)
                .setWidth(70)
                .setHeight(70)
                .setX(Screen.width)
                .setY(0)
                .setDesktopShow(false)//桌面显示
                .setViewStateListener(new ViewStateListener() {
                    @Override
                    public void onPositionUpdate(int i, int i1) {

                    }

                    @Override
                    public void onShow() {

                    }

                    @Override
                    public void onHide() {

                    }

                    @Override
                    public void onDismiss() {

                    }

                    @Override
                    public void onMoveAnimStart() {

                    }

                    @Override
                    public void onMoveAnimEnd() {

                    }

                    @Override
                    public void onBackToDesktop() {

                    }
                }).setPermissionListener(new PermissionListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFail() {

            }
        }).build();
    }

    @Override
    public void onClick(View view) {
        if (view == btn_loginout){
            startActivity(new Intent(mActivity,LoginActivity.class));
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        txt_current_time.setText(getCurrentTime());
    }

    /**
     * 描述：获取当前的日期时间等
     * 作者：zzh
     * @return 拼接好的日期时间
     */
    private String getCurrentTime(){
        Calendar c = Calendar.getInstance();
        int week = c.get(Calendar.DAY_OF_WEEK);
        //方法一
        String[] weeks = {"","星期天","星期一","星期二","星期三","星期四","星期五","星期六"};
        String weekResult = weeks[week];
        //方法二
        String weekStr = "";
        switch (week){
            case 1:
                weekStr = "星期日";
                break;
            case 2:
                weekStr = "星期一";
                break;
            case 3:
                weekStr = "星期二";
                break;
            case 4:
                weekStr = "星期三";
                break;
            case 5:
                weekStr = "星期四";
                break;
            case 6:
                weekStr = "星期五";
                break;
            case 7:
                weekStr = "星期六";
                break;
        }
        return c.get(Calendar.YEAR)+"年"+(c.get(Calendar.MONTH)+1)+"月"+c.get(Calendar.DAY_OF_MONTH)+"日  "+weekStr+"  "+c.get(Calendar.HOUR_OF_DAY)+"时"+c.get(Calendar.MINUTE)+"分";
    }

    private int px2dip( float pxValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Info.showToast(mContext,"开发中...",true);
            Info.playRingtone(mContext,true);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 描述：播放提示声音
     * 作者：zzh
     * @param position 位置
     */
    private void playClickTip(int position) {
        try {
            AssetManager assetManager = mContext.getAssets();
            AssetFileDescriptor afd;
            afd = assetManager.openFd(position+".mp3");
            MediaPlayer player = new MediaPlayer();
            if (player.isPlaying()){
                player.reset();
            }
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
//            player.setDataSource(afd.getFileDescriptor());
            player.setLooping(false);//循环播放
            player.prepare();
            player.start();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.reset();//播放完成后及时释放资源
                    mediaPlayer.release();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FloatWindow.destroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if ((System.currentTimeMillis() - exitTime) > 2000){
                Toast.makeText(mContext,"再按一次退出程序",Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }else {
                exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 描述：全部退出，使用广播
     * 作者：zzh
     */
    private void exit() {
        Intent intent = new Intent("com.yks.simpledemo3.baseActivity");
        intent.putExtra("closeAll",1);
        sendBroadcast(intent);
        MainActivity.this.finish();
    }
}
