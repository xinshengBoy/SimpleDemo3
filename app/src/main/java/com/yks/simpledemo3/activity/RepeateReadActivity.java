package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.view.MyActionBar;

import net.lemonsoft.lemonbubble.LemonBubble;
import net.lemonsoft.lemonhello.LemonHello;
import net.lemonsoft.lemonhello.LemonHelloAction;
import net.lemonsoft.lemonhello.LemonHelloInfo;
import net.lemonsoft.lemonhello.LemonHelloView;
import net.lemonsoft.lemonhello.interfaces.LemonHelloActionDelegate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**
 * 描述：连连看
 * 作者：zzh
 * time:2020/01/03
 * https://www.cnblogs.com/qinluohan/p/3159522.html
 */
public class RepeateReadActivity extends Activity {

    private Context mContext = RepeateReadActivity.this;
    private Activity mActivity = RepeateReadActivity.this;

    private GridView gv_repeate_read;
    private ArrayList<HashMap<String,Integer>> aList;
    private int lastClicked;
    private int temp = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repeate_read);

        initView();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity, title_layout, "连连看", "", false);

        gv_repeate_read = findViewById(R.id.gv_repeate_read);
        gv_repeate_read.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int temp2 = aList.get(position).get("whichStone");
                //第一个条件是判断点击的不是空的，第二个条件判断两次点击的不是同一个
                if (temp2 != R.drawable.empty && lastClicked != position){
                    if (temp == 0){
                        temp = temp2;
                    }else {
                        //两个点击是相同的
                        if (temp2 == temp){
                            Point thispoint = arg2ToPoint(position);//当前点击的坐标点
                            Point lastpoint = arg2ToPoint(lastClicked);//上一个坐标点
                            //todo 判断这两个点是否可以消除
                            if (checkIsItCanBeDestory(thispoint,lastpoint)){
                                clearTwoImage(position);//消除当前的图片
                                clearTwoImage(lastClicked);//消除上一个图片
                                bindData();//刷新页面
                                checkIsSuccess();//检查是否消除完了
                            }
                        }
                        temp = 0;
                    }
                    lastClicked = position;
                }
            }
        });
        startGame();
    }

    /**
     * 描述：开始游戏需要做的操作
     */
    private void startGame(){
        if (aList == null) {
            aList = new ArrayList<>();
        }
        if (aList.size() != 0){
            aList.clear();
        }
        createStones();//todo 生成数据
        mixData(aList);
        bindData();
    }

    //todo 重新开始
    public void restartRepeateRead(View view){
        startGame();
    }

    /**
     * 描述：生成数据，保证每种图片出现六次
     */
    private void createStones(){
        for (int i=1;i<7;i++){
            HashMap<String,Integer> map = new HashMap<>();
            switch (i){
                case Stones.Blue:
                    map.put("whichStone",R.drawable.ic_bluetooth);
                    break;
                case Stones.Gold:
                    map.put("whichStone",R.drawable.ic_compass_clock);
                    break;
                case Stones.Green:
                    map.put("whichStone",R.drawable.ic_form);
                    break;
                case Stones.Orange:
                    map.put("whichStone",R.drawable.ic_gobang);
                    break;
                case Stones.Purple:
                    map.put("whichStone",R.drawable.ic_music_lrc);
                    break;
                case Stones.Red:
                    map.put("whichStone",R.drawable.ic_record_line);
                    break;
            }
            aList.add(map);
            aList.add(map);
            aList.add(map);
            aList.add(map);
            aList.add(map);
            aList.add(map);
        }
    }

    /**
     * 描述：随机打乱顺序
     * @param list 列表
     */
    private void mixData(ArrayList<HashMap<String,Integer>> list){
        for (int i=0;i<200;i++){
            int rd = (int) (Math.random()*list.size());
            HashMap<String,Integer> map = list.get(rd);
            list.remove(rd);
            list.add(map);
        }
    }

    /**
     * 描述：绑定数据
     */
    private void bindData(){
        SimpleAdapter adapter = new SimpleAdapter(mContext,aList,R.layout.item_repeate_read,new String[]{"whichStone"},new int[]{R.id.iv_item_repeate});
        gv_repeate_read.setAdapter(adapter);
    }

    /**
     * 描述：检查是否已经全部消除了
     */
    private void checkIsSuccess(){
        for (HashMap<String,Integer> map : aList){
            if (map.get("whichStone") != R.drawable.empty){
                return;
            }
        }
        LemonHello.getSuccessHello("成功","重新开始").addAction(new LemonHelloAction("取消", new LemonHelloActionDelegate() {
            @Override
            public void onClick(LemonHelloView lemonHelloView, LemonHelloInfo lemonHelloInfo, LemonHelloAction lemonHelloAction) {
                lemonHelloView.hide();
            }
        })).addAction(new LemonHelloAction("确定", new LemonHelloActionDelegate() {
            @Override
            public void onClick(LemonHelloView lemonHelloView, LemonHelloInfo lemonHelloInfo, LemonHelloAction lemonHelloAction) {
                startGame();
                lemonHelloView.hide();
            }
        })).show(mContext);
    }
    /**
     * 描述：消除某个点，用空图片替代
     * @param x
     */
    private void clearTwoImage(int x){
        HashMap<String,Integer> map = new HashMap<>();
        map.put("whichStone",R.drawable.empty);
        aList.set(x,map);
    }

    /**
     * 描述：将数字转换为坐标
     * @param a 数字
     * @return 坐标
     */
    private Point arg2ToPoint(int a){
        int px = a % 6;
        int py = a / 6;
        return new Point(px,py);
    }

    /**
     * 描述：将坐标转换为数字
     * @param point 坐标
     * @return 数字
     */
    private int pointToArgs(Point point){
        return point.y * 6 + point.x;
    }

    /**
     * 描述：判断两个点是否可以消除
     * @param p1 第一个点
     * @param p2 第二个点
     * @return 是否可以消除
     */
    private boolean checkIsItCanBeDestory(Point p1,Point p2){
        //todo 判断一条线可以连接的情况
        if (testVertical(new Point(p1),new Point(p2))){//可以连接一条竖线
            return true;
        }

        if (testHorizontal(new Point(p1),new Point(p2))){//可以连接一条横线
            return true;
        }
        //todo 判断两条线可以连接的情况
        Point newPoint = new Point(p2.x,p1.y);
        int temp1 = pointToArgs(newPoint);
        if (aList.get(temp1).get("whichStone") == R.drawable.empty){//中间为空白，可用一条竖线和一条横线连接
            if (testVertical(p2,new Point(newPoint)) && testHorizontal(p1,new Point(newPoint))){
                return true;
            }
        }

        Point newPoint2 = new Point(p1.x,p2.y);
        temp1 = pointToArgs(newPoint2);
        if (aList.get(temp1).get("whichStone") == R.drawable.empty){//中间为空白，可用一条竖线和一条横线连接
            if (testVertical(p1,new Point(newPoint2)) && testHorizontal(p2,new Point(newPoint2))){
                return true;
            }
        }
        //todo 判断三条线可以连接的情况
        Vector<Line> vector = new Vector<>();
        vector = scan(new Point(p1),new Point(p2));
        if (!vector.isEmpty()){
            for (int i=0;i<vector.size();i++){
                Line line = vector.elementAt(i);
                //横线
                if (line.dirct == 0){
                    if (testVertical(new Point(p1),new Point(line.a)) && testVertical(new Point(p2),new Point(line.b))){
                        return true;
                    }
                }else {//竖线
                    if (testHorizontal(new Point(p1),new Point(line.a)) && testHorizontal(new Point(p2),new Point(line.b))){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 描述：判断两个点是否可以用竖线连接
     * @param p1 第一个点
     * @param p2 第二个点
     * @return 是否可以连接
     */
    private boolean testVertical(Point p1,Point p2){
        boolean check = true;//先定义竖线可以连接到两个点
        if (p1.x == p2.x){
            //差值，循环时用到
            int abs = Math.abs(p1.y - p2.y);
            if (abs == 0){
                return false;
            }
            int temp = (p1.y - p2.y) / abs;
            while (p1.y != p2.y){
                p2.y += temp;
                int arg2 = pointToArgs(p2);
                //如果对于的坐标点不为空
                if (aList.get(arg2).get("whichStone") != R.drawable.empty && p1.y != p2.y){
                    check = false;
                    break;
                }
            }
        }else {
            check = false;
        }
        return check;
    }
    /**
     * 描述：判断两个点是否可以用横线连接
     * @param p1 第一个点
     * @param p2 第二个点
     * @return 是否可以连接
     */
    private boolean testHorizontal(Point p1,Point p2){
        boolean check = true;//先定义横线可以连接到两个点
        if (p1.y == p2.y){
            //差值，循环时用到
            int abs = Math.abs(p1.x - p2.x);
            if (abs == 0){
                return false;
            }
            int temp = (p1.x - p2.x) / abs;
            while (p1.x != p2.x){
                p2.x += temp;
                int arg2 = pointToArgs(p2);
                //如果对于的坐标点不为空
                if (aList.get(arg2).get("whichStone") != R.drawable.empty && p1.x != p2.x){
                    check = false;
                    break;
                }
            }
        }else {
            check = false;
        }
        return check;
    }

    /**
     * 描述：连线
     * @param p1 第一个点
     * @param p2 第二个点
     * @return 线段
     */
    private Vector<Line> scan(Point p1,Point p2){
        Vector<Line> vector = new Vector<>();
        //todo 查找A左边的线
        for (int y=p1.y;y>=0;y--){
            int arg1 = pointToArgs(new Point(p1.x,y));
            int arg2 = pointToArgs(new Point(p2.x,y));
            if (aList.get(arg1).get("whichStone") == R.drawable.empty && aList.get(arg2).get("whichStone") == R.drawable.empty && testHorizontal(new Point(p1.x,y),new Point(p2.x,y))){
                vector.add(new Line(0,new Point(p1.x,y),new Point(p2.x,y)));
            }
        }
        //todo 查找A右边的线
        for (int y=p1.y;y<6;y++){
            int arg1 = pointToArgs(new Point(p1.x,y));
            int arg2 = pointToArgs(new Point(p2.x,y));
            if (aList.get(arg1).get("whichStone") == R.drawable.empty && aList.get(arg2).get("whichStone") == R.drawable.empty && testHorizontal(new Point(p1.x,y),new Point(p2.x,y))){
                vector.add(new Line(0,new Point(p1.x,y),new Point(p2.x,y)));
            }
        }
        //todo 查找A上边的线
        for (int x=p1.x;x>=0;x--){
            int arg1 = pointToArgs(new Point(x,p1.y));
            int arg2 = pointToArgs(new Point(x,p2.y));
            if (aList.get(arg1).get("whichStone") == R.drawable.empty && aList.get(arg2).get("whichStone") == R.drawable.empty && testVertical(new Point(x,p1.y),new Point(x,p2.y))){
                vector.add(new Line(0,new Point(x,p1.y),new Point(x,p2.y)));
            }
        }
        //todo 查找A下边的线
        for (int x=p1.x;x<6;x++){
            int arg1 = pointToArgs(new Point(x,p1.y));
            int arg2 = pointToArgs(new Point(x,p2.y));
            if (aList.get(arg1).get("whichStone") == R.drawable.empty && aList.get(arg2).get("whichStone") == R.drawable.empty && testVertical(new Point(x,p1.y),new Point(x,p2.y))){
                vector.add(new Line(0,new Point(x,p1.y),new Point(x,p2.y)));
            }
        }
        return vector;
    }
    /**
     * 描述：内部枚举类
     */
    private class Stones{
        public static final int Ull = 0;//这个图片是空白的
        static final int Blue = 1;
        static final int Gold = 2;
        static final int Green = 3;
        static final int Orange = 4;
        static final int Purple = 5;
        static final int Red = 6;
    }
    /**
     * 描述：存储坐标点的类
     */
    private class Point{
        int x;
        int y;

        Point(int px,int py){
            x = px;
            y = py;
        }

        Point(Point p){
            x = p.x;
            y = p.y;
        }
    }

    /**
     * 描述：判断三条直线连接的时候
     */
    private class Line{
        Point a,b;
        int dirct;//方向，0代表横线，1代表竖线
        Line(int dirce, Point a, Point b){
            this.a = a;
            this.b = b;
            this.dirct = dirce;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LemonBubble.forceHide();
        if (aList != null && aList.size() != 0){
            aList.clear();
            aList = null;
        }
    }
}
