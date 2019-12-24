package com.yks.simpledemo3.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.MotionEvent;
import android.view.View;

import com.yks.simpledemo3.R;

/**
 * 描述：放大镜效果
 * 作者：zzh
 * time:2019/12/24
 * https://github.com/baopengjian/Ray_SeniorUI/blob/master/app/src/main/java/com/example/baopengjian/ray_seniorui/fourth/view/ZoomImageView.java
 */
public class ZoomImageView extends View {

    private int factor = 2;//放大的倍数
    private int radius = 100;//放大镜的半径
    private Bitmap mBitmap;//原图
    private Bitmap mBigBitmap;//放大的图
    private ShapeDrawable shapeDrawable;//制作的圆形图片（局部，盖在canvas上面// ）
    private Matrix matrix;
    private int imageId = R.mipmap.splash2;

    public ZoomImageView(Context context) {
        super(context);
        init();
    }

//    public ZoomImageView(Context context,AttributeSet attrs) {
//        super(context, attrs);
//        init();
//    }
//
//    public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init();
//    }

    private void init(){
        mBitmap = BitmapFactory.decodeResource(getResources(),imageId);
        mBigBitmap = mBitmap;
        //放大后的整个图片
        mBigBitmap = Bitmap.createScaledBitmap(mBigBitmap,mBigBitmap.getWidth()*factor,mBigBitmap.getHeight()*factor,true);
        BitmapShader shader = new BitmapShader(mBigBitmap, Shader.TileMode.CLAMP,Shader.TileMode.CLAMP);
        //初始化圆形放大镜
        shapeDrawable = new ShapeDrawable(new OvalShape());
        shapeDrawable.getPaint().setShader(shader);
        //切出矩形区域，用来画圆（内切圆）
        shapeDrawable.setBounds(0,0,radius*2,radius*2);

        matrix = new Matrix();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画原图
        canvas.drawBitmap(mBitmap,0,0,null);
        //画放大镜的图
        shapeDrawable.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        //将放大的图片往相反的方向挪动
        matrix.setTranslate(radius - x * factor,radius - y * factor);
        shapeDrawable.getPaint().getShader().setLocalMatrix(matrix);
        //切出手势区域点位置的圆
        shapeDrawable.setBounds(x - radius,y - radius,x + radius, y + radius);
        invalidate();
        return true;
    }

    /**
     * 描述：设置图片
     * 作者：zzh
     * @param id 图片id
     */
    public void setImageId(int id){
        this.imageId = id;
        init();
        invalidate();
    }
}
