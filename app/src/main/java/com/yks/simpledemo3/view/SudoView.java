package com.yks.simpledemo3.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.yks.simpledemo3.R;

import java.util.Random;

/**
 * 描述：数独自定义
 * 作者：zzh
 * time:2019/11/06
 * https://github.com/whoma/Simple-sudoku/blob/master/app/src/main/java/com/example/jobs/newsudo/Sudo.java //生成九宫格的方法连接
 * https://www.cnblogs.com/JasonBourn/articles/7279164.html // 生成随机九宫格数字的方法
 */
public class SudoView extends View {

    private Context mContext;
    private float width,height;
    private Button[] bt = new Button[9];

    private int SIZE = 9;
    private int CELL_SIZE = 3;
    private int LEVEL_MAX = 2;//难度级别
    private int[][] sudoArr = new int[SIZE][SIZE];
    private int[][] sudoArrOld = new int[SIZE][SIZE];//保存一份原始数据

    public SudoView(Context context) {
        super(context);
        mContext = context;
        getUserArray();
    }
    //todo 获取表格数组
    private void getUserArray(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getSudoArray(LEVEL_MAX);
        }
        if (sudoArrOld.length == 0) {
            sudoArrOld = sudoArr;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        @SuppressLint("DrawAllocation") Paint paint = new Paint();
        paint.setAntiAlias(true);//消除锯齿
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getResources().getColor(R.color.colorBlue2));
        //todo 先画个空心的矩形，座位外背景
        canvas.drawRect(0,0,getWidth(),getHeight(),paint);

        //todo 画9条横轴、纵轴
        for (int i=0;i<9;i++){
            paint.setColor(getResources().getColor(R.color.colorBlack));
            //画横轴
            canvas.drawLine(0,i*height,getWidth(),i*height,paint);
            //画纵轴
            canvas.drawLine(i*width,0,i*width,getHeight(),paint);
            //进行条纹加深
            paint.setColor(getResources().getColor(R.color.colorBlue));

            //画横轴
            canvas.drawLine(0,i*height+3,getWidth(),i*height+3,paint);
            //画纵轴
            canvas.drawLine(i*width+3,0,i*width+3,getHeight(),paint);
            //将目前的九宫格分为3块
            if (i%3 == 0){
                paint.setColor(getResources().getColor(R.color.colorBlue));
                //画横轴
                canvas.drawLine(0,i*height+3,getWidth(),i*height+3,paint);
                //画纵轴
                canvas.drawLine(i*width+3,0,i*width+3,getHeight(),paint);
            }
        }

        //todo 将数字写入九宫格
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize((float)(height*0.75));

        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float x = width / 2;
        float y = height / 2 - (fontMetrics.ascent + fontMetrics.descent) / 2;
        for (int i=0;i<9;i++){
            for (int j=0;j<9;j++){
                int point = sudoArr[j][i];
                if (point != 0){
                    canvas.drawText(String.valueOf(point),j*width+x,i*height+y,paint);
                }
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w / 9f;
        height = h / 9f;//todo 得到九宫格宽高
        super.onSizeChanged(w, h, oldw, oldh);
    }

    // 重写 触摸 事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            final int x = (int) (event.getX() / width);
            final int y = (int) (event.getY() / height);
            if (sudoArrOld[x][y] == 0) {
                // 出现对话框，提供用户可添选数据
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                final AlertDialog dialog = builder.create();
                View item = LayoutInflater.from(mContext).inflate(R.layout.item_choose_layout, null);
                findAllBt(item);
                // 设置点击事件
                for (int i = 0; i < 9; i++) {
                    final int finalI = i+1;
                    bt[i].setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sudoArr[x][y] = finalI;
                            invalidate();
                            dialog.dismiss();
                        }
                    });
                }

                dialog.setView(item);
                dialog.setTitle("请选择数字");
                dialog.setCancelable(true);
                dialog.show();
            }
        }

        return super.onTouchEvent(event);
    }

    //todo 查找所有的 Button
    public void findAllBt(View view) {
        bt[0] = view.findViewById(R.id.bt1);
        bt[1] = view.findViewById(R.id.bt2);
        bt[2] = view.findViewById(R.id.bt3);
        bt[3] = view.findViewById(R.id.bt4);
        bt[4] = view.findViewById(R.id.bt5);
        bt[5] = view.findViewById(R.id.bt6);
        bt[6] = view.findViewById(R.id.bt7);
        bt[7] = view.findViewById(R.id.bt8);
        bt[8] = view.findViewById(R.id.bt9);
    }

    /**
     * 描述：设置难度等级
     * @param level 难度等级
     */
    public void setLevel(int level){
        LEVEL_MAX = level;
//        sudoArr = null;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            getSudoArray(level);
//        }
//        invalidate();
    }

    /**
     * 描述：生成数独
     * 作者：zzh
     * @param level 难度级别
     * https://www.cnblogs.com/JasonBourn/articles/7279164.html
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getSudoArray(int level){
        Random random = new Random();
        int n = random.nextInt(SIZE)+1;
        for (int i=0;i<SIZE;i++){
            for (int j=0;j<SIZE;j++){
                int p = Math.floorDiv(i,CELL_SIZE);
                int q = Math.floorDiv(j,CELL_SIZE);
                for (int k=0;k<SIZE;k++){
                    if (checkPoint(n,j,false) && checkPoint(n,i,true) && checkNineCells(n,p,q)){
                        sudoArr[i][j] = n;
                        break;
                    }else {
                        n = n % SIZE + 1;
                    }
                }
            }
            n = n % SIZE + 1;
        }
        upset();
        maskCells(level);
    }

    /**
     * 描述：检查数字是否存在某行或某列
     * 作者：zzh
     * @param n 数字
     * @param row 行
     * @param isRow 是否是行
     * @return 是否存在
     */
    private boolean checkPoint(int n,int row,boolean isRow){
        boolean check = true;
        for (int i=0;i<SIZE;i++){
            int point = isRow ? sudoArr[row][i] : sudoArr[i][row];
            if (point == n){
                check = false;
                break;
            }
        }
        return check;
    }

    /**
     * 描述：检查九宫格
     * @param n 数字
     * @param x 某行
     * @param y 某列
     * @return 是否存在
     */
    private boolean checkNineCells(int n,int x,int y){
        boolean check = true;
        int sx = x * 3;
        int sy = y * 3;
        for (int i=sx;i<sx+3;i++){
            for (int j=sy;j<sy+3;j++){
                if (sudoArr[i][j] == n){
                    check = false;
                    break;
                }
            }
        }
        return check;
    }

    /**
     * 描述：随机打乱顺序
     * 作者：zzh
     */
    private void upset(){
        Random random = new Random();
        //按行交换
        for (int i=0;i<SIZE;i++){
            int n = random.nextInt(CELL_SIZE);
            int p = random.nextInt(CELL_SIZE) * CELL_SIZE + n;
            for (int j=0;j<SIZE;j++){
                int tmp = sudoArr[i][j];
                sudoArr[i][j] = sudoArr[p][j];
                sudoArr[p][j] = tmp;
            }
        }
        //按列交换
        for (int i=0;i<SIZE;i++){
            int n = random.nextInt(CELL_SIZE);
            int p = random.nextInt(CELL_SIZE) * CELL_SIZE + n;
            for (int j=0;j<SIZE;j++){
                int tmp = sudoArr[j][i];
                sudoArr[j][i] = sudoArr[j][p];
                sudoArr[j][p] = tmp;
            }
        }
    }

    /**
     * 描述：根据难度登记来确认要显示的数字多少
     * 作者：zzh
     * @param level 难度等级
     */
    private void maskCells(int level){
        int min,max;
        level = level % LEVEL_MAX;
        if (level == 0){
            level = LEVEL_MAX;
        }

        if (level < 4){
            min = 20;
            max = 15;
        }else if (level < 7){
            min = 40;
            max = 10;
        }else if (level < 10){
            min = 50;
            max = 10;
        }else {
            min = 60;
            max = 5;
        }

        Random random = new Random();
        int count = random.nextInt(max) + min;
        for (int i=0;i<count;i++){
            do {
                int n = random.nextInt(SIZE);
                int m = random.nextInt(SIZE);
                if (sudoArr[n][m] > 0){//不显示
                    sudoArr[n][m] = 0;
                    break;
                }
            }while (true);
        }
    }

    /**
     * 描述：重置
     * 作者：zzh
     */
    public void reset(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getSudoArray(LEVEL_MAX);
        }
        invalidate();
        sudoArrOld = sudoArr;
    }
}
