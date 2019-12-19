package com.yks.simpledemo3.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.tools.Info;
import com.yks.simpledemo3.tools.RealPathFromUriUtils;
import com.yks.simpledemo3.view.MyActionBar;

import net.lemonsoft.lemonbubble.LemonBubble;
import net.lemonsoft.lemonhello.LemonHello;
import net.lemonsoft.lemonhello.LemonHelloAction;
import net.lemonsoft.lemonhello.LemonHelloInfo;
import net.lemonsoft.lemonhello.LemonHelloView;
import net.lemonsoft.lemonhello.interfaces.LemonHelloActionDelegate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import kr.co.namee.permissiongen.PermissionGen;

/**
 * 描述：水印图片
 * 作者：zzh
 * time:2019/07/26
 * 参考：https://blog.csdn.net/qq_34475058/article/details/78251618
 */
public class WaterPictureActivity extends Activity implements View.OnClickListener {

    private Activity mActivity = WaterPictureActivity.this;
    private Context mContext = WaterPictureActivity.this;
    private ImageView iv_waterpicture;
    private Button btn_waterpicture;
    private Bitmap newBitmap = null;
    private MyHandler handler;
    private String result = "";

    private final int SUCCESS = 0;
    private final int FAIL = 1;
    private final int SAVESUCCESS = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_picture);

        handler = new MyHandler(mActivity);
        initView();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity,title_layout,"水印图片","",false);

        iv_waterpicture = findViewById(R.id.iv_waterpicture);
        btn_waterpicture = findViewById(R.id.btn_waterpicture);
        btn_waterpicture.setOnClickListener(this);
        //todo 长按选择图片
        iv_waterpicture.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                LemonHello.getWarningHello("保存","确定保存这张水印图片吗？").addAction(new LemonHelloAction("取消", new LemonHelloActionDelegate() {
                    @Override
                    public void onClick(LemonHelloView lemonHelloView, LemonHelloInfo lemonHelloInfo, LemonHelloAction lemonHelloAction) {
                        lemonHelloView.hide();
                    }
                })).addAction(new LemonHelloAction("确定", new LemonHelloActionDelegate() {
                    @Override
                    public void onClick(LemonHelloView lemonHelloView, LemonHelloInfo lemonHelloInfo, LemonHelloAction lemonHelloAction) {
                        Info.showProgress(mContext,"保存中...");
                        MyApplication.cachedThreadPool.execute(new Runnable() {
                            @Override
                            public void run() {
                                result = saveToSdcard(newBitmap);
                                if (result.equals("")){
                                    sendMessage(FAIL,"保存失败");
                                }else {
                                    handler.sendEmptyMessage(SAVESUCCESS);
                                }
                            }
                        });
                        lemonHelloView.hide();
                    }
                })).show(mContext);
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == btn_waterpicture) {//todo 打开相册选择图片
            PermissionGen.needPermission(mActivity, 100, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 23332);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 23332 && resultCode == RESULT_OK){
            final String path = RealPathFromUriUtils.getRealPathFromUri(mContext,data.getData());
            if (!path.equals("")) {
                Info.showProgress(mContext, "生成中...");
                MyApplication.cachedThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap oldBitmap = decodeFile(path);
//                        Bitmap waterBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.water);
                        Bitmap waterBitmap = createWaterBitmap("钟志华");
                        if (oldBitmap == null || waterBitmap == null){
                            sendMessage(FAIL,"生成Bitmap失败！");
                            return;
                        }
                        newBitmap = waterMask(mActivity, oldBitmap, waterBitmap);
                        if (newBitmap != null) {
                            handler.sendEmptyMessage(SUCCESS);
                        } else {
                            sendMessage(FAIL,"生成水印图片失败！");
                        }
                    }
                });
            }else {
                sendMessage(FAIL,"获取图片路径失败！");
            }
        }
    }

    private class MyHandler extends Handler {
        final WeakReference<Activity> mWeakReference;
        MyHandler(Activity activity) {
            mWeakReference= new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mWeakReference.get() != null) {
                if (msg.what == SUCCESS){//生成水印照片成功
                    LemonBubble.hide();
                    Info.playRingtone(mContext,true);
                    iv_waterpicture.setImageBitmap(newBitmap);
                }else if (msg.what == FAIL){//失败集合
                    Bundle bundle = msg.getData();
                    Info.showToast(mContext, bundle.getString("msg"), false);
                    Info.playRingtone(mContext, false);
                }else if (msg.what == SAVESUCCESS){//保存成功
                    LemonBubble.hide();
                    Info.playRingtone(mContext,true);
                    handler.sendEmptyMessageDelayed(111,300);
                }else if (msg.what == 111){//弹框提示
                    LemonHello.getSuccessHello("成功",result).addAction(new LemonHelloAction("确定", new LemonHelloActionDelegate() {
                        @Override
                        public void onClick(LemonHelloView lemonHelloView, LemonHelloInfo lemonHelloInfo, LemonHelloAction lemonHelloAction) {
                            lemonHelloView.hide();
                        }
                    })).show(mContext);
                }
            }
        }
    }

    /**
     * 描述：根据输入的文字生成透明背景的水印图片
     * 作者：zzh
     * @param text 要生成的文字
     * @return 透明背景的水印
     */
    private Bitmap createWaterBitmap(String text){
        Bitmap bitmap = Bitmap.createBitmap(180,150, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.TRANSPARENT);//设置背景颜色为透明

        Paint paint = new Paint();
        paint.setTextAlign(Paint.Align.LEFT);//文字的起始位置
        paint.setColor(0xFFF7C917);//设置文字颜色
        paint.setAntiAlias(true);//消除锯齿
        paint.setTextSize(30);//设置文字大小

        canvas.drawText(text,20,100,paint);
        canvas.save();
        canvas.restore();
        return bitmap;
    }

    /**
     * 描述：通过图片路径获取图片资源
     * 作者：zzh
     * @param pathName 图片路径
     * @return 图片的bitmap
     */
    private Bitmap decodeFile(String pathName){
        Bitmap bitmap = null;
        InputStream stream = null;

        try {
            stream = new FileInputStream(pathName);
            bitmap = BitmapFactory.decodeStream(stream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if (stream != null){
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    /**
     * 描述：设置水印
     * 作者：zzh
     * @param src 大图
     * @param watermark 水印
     * @return 返回设置了水印的图片
     */
    private Bitmap waterMask(Activity activity,Bitmap src,Bitmap watermark){
        //获取原图宽高
        int w = src.getWidth();
        int h = src.getHeight();
        //设置原图的大小
        float newWidth = activity.getWindowManager().getDefaultDisplay().getWidth();
        float newHeight = h * (newWidth / w);
        //计算缩放比例
        float scaleWidth = newWidth / w;
        float scaleHeight = newHeight / h;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth,scaleHeight);
        src = Bitmap.createBitmap(src,0,0,w,h,matrix,true);

        //根据原图设置缩放水印
        float w1 = w / 5;
        float h1 = w1 / 5;
        //获取原始水印图片的宽高
        int w2 = watermark.getWidth();
        int h2 = watermark.getHeight();
        //计算水印缩放比例
        scaleWidth = w1 / w2;
        scaleHeight = h1 / h2;

        Matrix waterMatrix = new Matrix();
        waterMatrix.postScale((float) 0.8,(float) 0.8);
        watermark = Bitmap.createBitmap(watermark,0,0,w2,h2,waterMatrix,true);
        //获取新的水印图片的宽高
        w2 = watermark.getWidth();
        h2 = watermark.getHeight();

        Bitmap result = Bitmap.createBitmap(src.getWidth(),src.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(result);
        //在canvas上绘制原图和新的水印图
        canvas.drawBitmap(src,0,0,null);
        //水印图绘制在画布的右下角，距离右边和底部都为20
        canvas.drawBitmap(watermark,src.getWidth() - w2,src.getHeight() - h2,null);
        canvas.save();
        canvas.restore();

        return result;
    }

    /**
     * 描述：将bitmap图片保存到指定文件夹
     * 作者：zzh
     * @param bitmap 图片资源
     * @return 返回成功或者失败
     */
    private String saveToSdcard(Bitmap bitmap){
        boolean isSuccess = true;
        String path = null;
        File file = new File(Environment.getExternalStorageDirectory()+"/yujisofile",System.currentTimeMillis()+".jpg");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,90,out);
        } catch (FileNotFoundException e) {
            isSuccess = false;
            e.printStackTrace();
        }
        try {
            out.flush();
            out.close();
            path =  file.getAbsolutePath();
        } catch (IOException e) {
            isSuccess = false;
            e.printStackTrace();
        }
        if (!isSuccess) {
            return "";
        }else {
            return "保存成功，保存路径："+path;
        }
    }

    /**
     * 描述：发handler消息
     * 作者：zzh
     * @param id 需要进入到的handler
     * @param msg 传递的消息
     */
    private void sendMessage(int id, String msg) {
        Bundle bundle = new Bundle();
        bundle.putString("msg",msg);
        Message message = new Message();
        message.what = id;
        message.setData(bundle);
        handler.sendMessage(message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LemonBubble.forceHide();
        handler.removeCallbacksAndMessages(null);
        if (newBitmap != null) {
            newBitmap.recycle();
            newBitmap = null;
        }
    }
}
