package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.bean.StockBean;
import com.yks.simpledemo3.tools.Info;
import com.yks.simpledemo3.view.MyActionBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import net.lemonsoft.lemonbubble.LemonBubble;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;

/**
 * 描述：定时任务调用接口获取数据显示
 * 作者：zzh
 * time:2019/07/29
 * 参考：https://www.oschina.net/question/263892_54187
 * http://hq.sinajs.cn/list=sh000001
 */
public class TimingTaskActivity extends Activity {

    private Activity mActivity = TimingTaskActivity.this;
    private Context mContext = TimingTaskActivity.this;

    private final int GETSTOCKINFOFAIL = 0;
    private final int GETSTOCKINFOSUCCESS = 1;

    private TextView txt_stock_name,txt_today_openprice,txt_yesterday_closeprice,txt_current_price,txt_highestprice_today,txt_lowestprice_today,txt_deal_count,txt_deal_money,txt_stock_date;
    private MyHandler handler;
    private List<StockBean> mList = new ArrayList<>();
    private double oldPrice = 0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timing_tasks);

        handler = new MyHandler(mActivity);
        initView();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity, title_layout, "定时任务", "", false);

        txt_stock_name = findViewById(R.id.txt_stock_name);
        txt_today_openprice = findViewById(R.id.txt_today_openprice);
        txt_yesterday_closeprice = findViewById(R.id.txt_yesterday_closeprice);
        txt_current_price = findViewById(R.id.txt_current_price);
        txt_highestprice_today = findViewById(R.id.txt_highestprice_today);
        txt_lowestprice_today = findViewById(R.id.txt_lowestprice_today);
        txt_deal_count = findViewById(R.id.txt_deal_count);
        txt_deal_money = findViewById(R.id.txt_deal_money);
        txt_stock_date = findViewById(R.id.txt_stock_date);

        timer.schedule(task,0,10*1000);//十秒更新一次
    }

    private class MyHandler extends Handler {
        final WeakReference<Activity> mWeakReference;

        MyHandler(Activity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mWeakReference.get() != null) {
                if (msg.what == GETSTOCKINFOFAIL) {//获取失败
                    Bundle bundle = msg.getData();
                    Info.showToast(mContext, bundle.getString("msg"), false);
                    Info.playRingtone(mContext, false);
                }else if (msg.what == GETSTOCKINFOSUCCESS){//获取成功
                    StockBean bean = mList.get(0);
                    double newPrice = Double.parseDouble(bean.getCurrentPrice());
                    if (newPrice >= oldPrice){
                        txt_current_price.setTextColor(Color.RED);
                    }else {
                        txt_current_price.setTextColor(Color.GREEN);
                    }
                    oldPrice = newPrice;
                    txt_stock_name.setText("股票名称："+bean.getStockName());
                    txt_today_openprice.setText("今日开盘价："+bean.getTodayOpenPrice());
                    txt_yesterday_closeprice.setText("昨日收盘价："+bean.getYesterdayClosePrice());
                    txt_current_price.setText("当前价格："+bean.getCurrentPrice());
                    txt_highestprice_today.setText("今日最高价："+bean.getHighestPriceToday());
                    txt_lowestprice_today.setText("今日最低价："+bean.getLowestPriceToday());

                    String countStr = "";
                    int count = Integer.parseInt(bean.getDealCount());
                    if (count >= 10000){
                        countStr = (count / 10000) + "万手";
                    }else {
                        countStr = count + "手";
                    }
                    txt_deal_count.setText("成交量："+countStr);

//                    String countMoney = "";
//                    double money = Double.parseDouble(bean.getDealMoney());
//                    if (money >= 100000000){
//                        countMoney = (int)(money / 100000000) + "亿" + (int) (money % 100000000) + "万";
//                    }else {
//                        countMoney = (int)(money / 10000) + "万";
//                    }

                    String moneys = formatVolume(bean.getDealMoney());
                    txt_deal_money.setText("成交金额："+moneys);
                    txt_stock_date.setText("日期："+bean.getStockDate()+"  "+bean.getStockTime());
                }
            }
        }
    }

    private Timer timer = new Timer(true);
    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            getStockInfo();
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        if (timer == null){
            timer = new Timer(true);
        }
        if (task == null){
            timer.schedule(task,0,20*1000);//二十秒更新一次
        }
    }

    /**
     * 描述：获取股票详情
     * 作者：zzh
     */
    private void getStockInfo(){
        if (mList != null && mList.size() != 0) {
            mList.clear();
        }
        OkHttpUtils.get().url("http://hq.sinajs.cn/list=sh000001")
                .addHeader("Content-Type","application/json")
                .tag(this)
                .build()
                .connTimeOut(10000L)
                .readTimeOut(10000L)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        sendMessage(GETSTOCKINFOFAIL,"失败1："+e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (response.equals("")){
                            sendMessage(GETSTOCKINFOFAIL,"失败2：");
                            return;
                        }

                        try {
                            String [] array1 = response.split("=");
                            String response1 = array1[1].substring(1,array1[1].length()-3);
                            String [] array2 = response1.split(",");
                            StockBean bean = new StockBean(array2[0],array2[1],array2[2],array2[3],array2[4],array2[5],array2[8],array2[9],array2[30],array2[31]);
                            mList.add(bean);
                            handler.sendEmptyMessage(GETSTOCKINFOSUCCESS);
                        }catch (Exception e){
                            e.printStackTrace();
                            sendMessage(GETSTOCKINFOFAIL,"失败3：");
                        }

                    }
                });
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

    public static String formatVolume(String s){
        //成交量为空时
        if (s==null || s.length()==0 || s.equals("0")||s.equals("0.0")||s.equals("0.00")) {
            return "0";
        }

        try {
            BigDecimal volume=new BigDecimal(s);
            float b=volume.floatValue();
            if (b<1000.0) {//一千以下
                BigDecimal dispVolume=volume.divide(new BigDecimal("1"), 0, RoundingMode.HALF_UP);
                return dispVolume.toString();
            }else if(b>=1000.0 && b<10000.0){ // 千
                BigDecimal dispVolume=volume.divide(new BigDecimal("1"), 0, RoundingMode.HALF_UP);
                return dispVolume.toString();
            }else if(b>=10000.0 && b<100000.0){//  万
                BigDecimal dispVolume=volume.divide(new BigDecimal("10000"), 2, RoundingMode.HALF_UP);
                return dispVolume.toString()+"万";
            }else if(b>=100000.0 && b<1000000.0){//     十万
                BigDecimal dispVolume=volume.divide(new BigDecimal("10000"), 2, RoundingMode.HALF_UP);
                return dispVolume.toString()+"万";
            }else if(b>=1000000.0 && b<10000000.0){//一百万
                BigDecimal dispVolume=volume.divide(new BigDecimal("10000"), 1, RoundingMode.HALF_UP);
                return dispVolume.toString()+"万";
            }else if(b>=10000000.0 && b<100000000.0){//  千万
                BigDecimal dispVolume=volume.divide(new BigDecimal("10000"), 1, RoundingMode.HALF_UP);
                return dispVolume.toString()+"万";
            }else if(b>100000000.0 && b<1000000000.0){//亿
                BigDecimal dispVolume=volume.divide(new BigDecimal("100000000"), 2, RoundingMode.HALF_UP);
                return dispVolume.toString()+"亿";
            }else if(b>=1000000000.0 && b<10000000000.0){// 十亿
                BigDecimal dispVolume=volume.divide(new BigDecimal("100000000"), 2, RoundingMode.HALF_UP);
                return dispVolume.toString()+"亿";
            }else if(b>=10000000000.0 && b<1000000000000.0){// 百亿
                BigDecimal dispVolume=volume.divide(new BigDecimal("100000000"), 1, RoundingMode.HALF_UP);
                return dispVolume.toString()+"亿";
            }else if(b>=1000000000000.0 && b<10000000000000.0){//  万亿
                BigDecimal dispVolume=volume.divide(new BigDecimal("1000000000000"), 2, RoundingMode.HALF_UP);
                return dispVolume.toString()+"万亿";
            }else if(b>=10000000000000.0 && b<100000000000000.0){//  十万亿
                BigDecimal dispVolume=volume.divide(new BigDecimal("1000000000000"), 2, RoundingMode.HALF_UP);
                return dispVolume.toString()+"万亿";
            }else if(b>=100000000000000.0){//  百万亿
                BigDecimal dispVolume=volume.divide(new BigDecimal("1000000000000"), 1, RoundingMode.HALF_UP);
                return dispVolume.toString()+"万亿";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LemonBubble.forceHide();
        OkHttpUtils.getInstance().cancelTag(mContext);
        handler.removeCallbacksAndMessages(null);
        mContext = null;
        mActivity = null;
        if (mList != null && mList.size() != 0){
            mList.clear();
            mList = null;
        }
    }
}
