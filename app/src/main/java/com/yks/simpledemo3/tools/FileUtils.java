package com.yks.simpledemo3.tools;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.yks.simpledemo3.bean.NotificationBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

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
     * 描述：获取指定文件大小
     * @param file 文件体
     * @return
     * @throws Exception
     */
    public static long getFileSize(File file) throws Exception{
        long size =0;
        if(file.exists()){
            FileInputStream fis =null;
            fis = new FileInputStream(file);
            size = fis.available();
        }else{
            file.createNewFile();
            Log.e("获取文件大小","文件不存在!");
        }
        return size;
    }

    /**
     * 描述：获取指定文件大小及单位
     * @param file 文件体
     * @return
     * @throws Exception
     */
    public static String getFileSizeAndUnit(File file){
        long size =0;
        if(file.exists()){
            FileInputStream fis;
            try {
                fis = new FileInputStream(file);
                size = fis.available();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.e("获取文件大小","文件不存在!");
        }
        return FormetFileSize(size);
    }

    /**
     * 描述：转换文件大小
     * @param fileS 文件的size
     * @return 文件的大小
     */
    public static String FormetFileSize(long fileS) {
        DecimalFormat df  = new DecimalFormat("#.00");
        String fileSizeString ="";
        String wrongSize="0B";
        if(fileS == 0){
            return wrongSize;
        }
        if(fileS < 1024){
            fileSizeString = df.format((double) fileS) +"B";
        }else if(fileS < (1024*1024)){
            fileSizeString = df.format((double) fileS /1024) +"KB";
        }else if(fileS < (1024*1024*1024)){
            fileSizeString = df.format((double) fileS /1048576) +"MB";
        }else{
            fileSizeString = df.format((double) fileS /1073741824) +"GB";
        }
        return fileSizeString;
    }

    /**
     * 删除单个文件
     * @param   filePath    被删除文件的文件名
     * @return 文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 删除文件夹以及目录下的文件
     * @param   filePath 被删除目录的文件路径
     * @return  目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String filePath) {
        boolean flag = false;
        //如果filePath不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        File dirFile = new File(filePath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        //遍历删除文件夹下的所有文件(包括子目录)
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                //删除子文件
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } else {
                //删除子目录
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前空目录
        return dirFile.delete();
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

    public static File getFile(String filePath){
        File file = new File(filePath);
        if (!file.exists()){
            try {
                if (file.isDirectory() && !file.exists()){
                    file.mkdirs();//创建文件夹
                }
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 描述：保存list数据到sd卡
     * @param list 数据
     * @param fileName 文件名
     */
    public static void saveStorage2SDCard(ArrayList list,String fileName){
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            File file = getFile(Environment.getExternalStorageDirectory()+"/yks/"+fileName);
            fileOutputStream = new FileOutputStream(file.toString());
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(list);
        }catch (Exception e){
            e.printStackTrace();
        }

        if (objectOutputStream != null){
            try {
                objectOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (fileOutputStream != null){
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 描述：获取sd卡文件的list集合
     * @param fileName 文件名称
     * @return 返回的list数据
     */
    public static ArrayList<NotificationBean> getNotificationList(String fileName){
        ObjectInputStream objectInputStream = null;
        FileInputStream fileInputStream = null;
        ArrayList<NotificationBean> list = new ArrayList<>();
        try {
            File file = getFile(Environment.getExternalStorageDirectory()+"/yks/"+fileName);
            fileInputStream = new FileInputStream(file.toString());
            objectInputStream = new ObjectInputStream(fileInputStream);
            list = (ArrayList<NotificationBean>) objectInputStream.readObject();
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
}
