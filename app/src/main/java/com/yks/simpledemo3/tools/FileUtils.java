package com.yks.simpledemo3.tools;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import java.io.File;
import java.text.DecimalFormat;

/**
 * 描述：文件相关类工具
 * 作者：zzh
 * time:2019/10/14
 */
public class FileUtils {
    /**
     * 描述：判断SD卡是否可用
     * 作者：zzh
     * @return 是否可用
     */
    public static boolean isSDCardEnable(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 描述：获取sd卡根目录的路径
     * 作者：zzh
     * @return 根目录路径
     */
    public static String getSDCardPath(){
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }

    /**
     * 描述：获取sd卡的剩余存储容量，单位：MB
     * @return 换算成MB单位的sd卡剩余容量
     */
    public static String getSDCardAllSize(){
        if (isSDCardEnable()){
            StatFs statFs = new StatFs(getSDCardPath());
            //获取空闲的数据块的数量
            long availabelBlocks = statFs.getAvailableBlocks();
            //获取每个数据块的大小
            long blockSize = statFs.getBlockSize();
            long size = (availabelBlocks * blockSize) / (1024 * 1024);
            String result = new DecimalFormat("#.00").format(size);
            return result + "MB";
        }
        return "0MB";
    }

    /**
     * 描述：获取指定的路径的文件剩余存储容量
     * 作者：zzh
     * @param path 指定的文件夹路径
     * @return 剩余存储大小
     */
    public static String getFilePathSize(String path){
        //如果这个路径是sd卡下面的路径，则获取sd卡剩余容量
        if (path.startsWith(getSDCardPath())){
            path = getSDCardPath();
        }else {
            path = Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        StatFs statFs = new StatFs(path);
        long avaibleBlocks = statFs.getAvailableBlocks() - 4;
        long size = (avaibleBlocks * statFs.getBlockSize()) / (1024*1024);
        String result = new DecimalFormat("#.00").format(size);
        return result + "MB";
    }

    /**
     * 描述：获取屏幕宽度
     * 作者：zzh
     * @param context 上下文
     * @return 屏幕宽度
     */
    public static int getScreenWidth(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        assert wm != null;
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }
    /**
     * 描述：获取屏幕高度
     * 作者：zzh
     * @param context 上下文
     * @return 屏幕高度
     */
    public static int getScreenHeight(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        assert wm != null;
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    /**
     * 描述：获取状态栏高度
     * 作者：zzh
     * @param context 上下文
     * @return 状态栏高度
     */
    public static int getStatusHeight(Context context){
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        }catch (Exception e){
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 描述：获取包括状态栏的截图
     * 作者：zzh
     * @param activity activity
     * @return 截图
     */
    private static Bitmap getShootScreenAndStatusBar(Activity activity){
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);//允许截图
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bitmap,0,0,width,height);
        view.destroyDrawingCache();
        return bp;
    }

    /**
     * 描述：获取截屏，不包含状态栏
     * 作者：zzh
     * @param activity activity
     * @return 截图bitmap
     */
    private static Bitmap getShootBitmap(Activity activity){
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top;

        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bitmap,0,statusBarHeight,width,height-statusBarHeight);
        view.destroyDrawingCache();
        return bp;
    }
}
