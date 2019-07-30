package com.yks.simpledemo3.tools;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yks.simpledemo3.adapter.BaseRecyclerAdapter;

import net.lemonsoft.lemonbubble.LemonBubble;

/**
 * 描述：
 * 作者：
 * time:2019/07/09
 */
public class Info {

    public static String LOGIN = "login";
    public static String PERSONNAME = "";
    //获取屏幕高度，用于监听键盘弹起
    public static int KEY_HEIGHT = 0;
    /**
     * 获取当前应用的版本名称
     * @param context 上下文
     * @return 返回版本号
     */
    public static String getVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        PackageInfo info;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    /**
     * 弹框提示
     * @param msg 提示的内容
     * @param isRight 提示的类型（成功或失败）
     */
    public static void showToast(Context context,String msg , boolean isRight){
        if (context != null) {
            if (isRight) {
                LemonBubble.showRight(context, msg, 2000);
            } else {
                LemonBubble.showError(context, msg, 2500);
            }
        }
    }

    /**
     * 播放扫描结果的对比
     * @param context 上下文
     * @param success 是否成功
     */
    public static void playRingtone(Context context, boolean success) {
        if (context != null) {
            try {
                AssetManager assetManager = context.getAssets();
                AssetFileDescriptor afd;
                if (success) {
                    afd = assetManager.openFd("success.mp3");
                } else {
                    afd = assetManager.openFd("fail.mp3");
                }
                MediaPlayer player = new MediaPlayer();
                if (player.isPlaying()) {
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
    }

    /**
     * 描述：显示加载框
     * 作者：zzh
     * @param context 上下文
     * @param text 要提示的内容
     */
    public static void showProgress(Context context,String text){
        LemonBubble.showRoundProgress(context,text);
    }

    /**
     * 描述：设置recycview的适配器
     * 作者：zzh
     * @param context 上下文
     * @param view recycview
     * @param adapter 适配器
     */
    public static void setRecycviewAdapter(Context context, RecyclerView view, BaseRecyclerAdapter adapter){
        //横向滚动
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        view.setLayoutManager(manager);
        view.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL));//默认的分割线
        view.setAdapter(adapter);
    }

    /**
     * 描述：获取软键盘弹起的高度
     * @param activity activity
     */
    public static void getScreenHeight(Activity activity){
        //获取屏幕高度，用于监听软键盘的弹起
        int screenHeight = activity.getWindowManager().getDefaultDisplay().getHeight();
        KEY_HEIGHT = screenHeight / 3;
    }
}
