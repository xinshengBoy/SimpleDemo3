package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.adapter.BaseRecyclerAdapter;
import com.yks.simpledemo3.bean.CityCodeBean;
import com.yks.simpledemo3.bean.WeatherReportBean;
import com.yks.simpledemo3.tools.Info;
import com.yks.simpledemo3.view.MyActionBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import net.lemonsoft.lemonbubble.LemonBubble;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.lognteng.editspinner.lteditspinner.LTEditSpinner;
import okhttp3.Call;

/**
 * 描述：天气预报
 * 作者：zzh
 * time:2019/11/27
 * 天气预报api接口 https://blog.csdn.net/JON_QQ/article/details/101106375
 * 输入框：https://blog.csdn.net/c237821375/article/details/80575943
 */
public class WeatherReportActivity extends Activity {

    private Context mContext = WeatherReportActivity.this;
    private Activity mActivity = WeatherReportActivity.this;

    private static final int GETWEATHERREPORT = 0;//获取天气数据
    private static final int GETWEATHERREPORTFAIL = 1;//获取失败
    private static final int GETWEATHERREPORTSUCCESS = 2;//获取成功
    private static final int GETCITYCODESUCCESS = 3;//获取城市代码成功

    private LTEditSpinner et_weather_report;
    private RecyclerView rv_weather_report;
    private MyHandler handler;
    private List<CityCodeBean> cityList = new ArrayList<>();
    private List<WeatherReportBean> mList = new ArrayList<>();
    private WeatherReportAdapter adapter;
    private String cityCode = "101060101";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_report);

        handler = new MyHandler(mActivity);
        ReadCityCodeTask task = new ReadCityCodeTask();
        task.execute();
        initView();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity, title_layout, "天气预报", "", false);

        et_weather_report = findViewById(R.id.et_weather_report);
        et_weather_report.setImageBttonBackground(null);
        et_weather_report.setEditHint("城市");
        Button btn_weather_report = findViewById(R.id.btn_weather_report);
        rv_weather_report = findViewById(R.id.rv_weather_report);
        adapter = new WeatherReportAdapter(mList);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_weather_report.setLayoutManager(manager);
        rv_weather_report.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.HORIZONTAL));//默认的分割线
        rv_weather_report.setAdapter(adapter);

        btn_weather_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Info.hideKeyboard(mContext,et_weather_report.getLtes_editText());
                String et_city = et_weather_report.getValue();
                if (!et_city.equals("")){
                    handler.sendEmptyMessage(GETWEATHERREPORT);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mList == null || mList.size() == 0){
            handler.sendEmptyMessage(GETWEATHERREPORT);
        }
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
                if (msg.what == GETWEATHERREPORT) {//todo 获取天气信息
                    getWeatherReportInfo();
                }else if (msg.what == GETWEATHERREPORTFAIL){//todo 获取日天气信息失败
                    Bundle bundle = msg.getData();
                    Info.showToast(mContext, bundle.getString("msg"), false);
                    Info.playRingtone(mContext, false);
                }else if (msg.what == GETWEATHERREPORTSUCCESS){//todo 获取天气信息成功
                    LemonBubble.hide();
                    adapter.refresh(mList);
                    rv_weather_report.scrollToPosition(1);//滑到当天的天气

                    Info.playRingtone(mContext,true);
                }else if (msg.what == GETCITYCODESUCCESS){//todo 获取城市代码成功
                    et_weather_report.initData(cityList, new LTEditSpinner.OnESItemClickListener<CityCodeBean>() {
                        @Override
                        public void onItemClick(CityCodeBean item) {
                            cityCode = item.getCityCode();
                            Info.hideKeyboard(mContext,et_weather_report.getLtes_editText());
                        }
                    });
                }
            }
        }
    }

    private class WeatherReportAdapter extends BaseRecyclerAdapter<WeatherReportBean>{

        WeatherReportAdapter(List<WeatherReportBean> mDatas) {
            super(mDatas, R.layout.item_weather_report);
        }

        @Override
        protected void bindData(BaseViewHolder holder, int position, WeatherReportBean bean) {
            TextView date = (TextView) holder.getView(R.id.txt_item_weather_date);
            TextView address = (TextView) holder.getView(R.id.txt_item_weather_address);
            TextView sunrise = (TextView) holder.getView(R.id.txt_item_weather_sunrise);
            TextView sunset = (TextView) holder.getView(R.id.txt_item_weather_sunset);
            TextView temprate = (TextView) holder.getView(R.id.txt_item_weather_temprate);
            TextView type = (TextView) holder.getView(R.id.txt_item_weather_type);
            TextView fengxiang = (TextView) holder.getView(R.id.txt_item_weather_fengxiang);
            TextView fengli = (TextView) holder.getView(R.id.txt_item_weather_fengli);
            TextView aqi = (TextView) holder.getView(R.id.txt_item_weather_aqi);
            TextView notice = (TextView) holder.getView(R.id.txt_item_weather_notice);

            date.setText("日期： "+bean.getDate()+"   "+bean.getWeek());
            address.setText("城市："+bean.getAddress());
            sunrise.setText("日出："+bean.getSunrise());
            sunset.setText("日落："+bean.getSunset());
            temprate.setText("温度：  "+bean.getTemprate());
            type.setText("天气：  "+bean.getType());
            fengxiang.setText("风向：   "+bean.getFengxiang());
            fengli.setText("风力：  "+bean.getFengli());
            aqi.setText("空气质量："+bean.getAqi());
            notice.setText("提示："+bean.getNotice());
        }
    }

    /**
     * 描述：获取天气数据
     * 作者：zzh
     */
    private void getWeatherReportInfo(){
        Info.showProgress(mContext,"查询中...");
        if (mList.size() != 0){
            mList.clear();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get().url("http://t.weather.sojson.com/api/weather/city/"+cityCode)
                        .addHeader("Content-Type","application/json;Charset=UTF-8")
                        .tag(this)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Info.sendMessage(handler,GETWEATHERREPORTFAIL,"获取失败1："+e);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                try {
                                    JSONObject object = new JSONObject(response);
                                    String status = object.getString("status");
                                    String message = object.getString("message");
                                    if (!status.equals("200")){
                                        Info.sendMessage(handler,GETWEATHERREPORTFAIL,message);
                                        return;
                                    }
                                    JSONObject object1 = object.getJSONObject("cityInfo");
                                    String address = object1.getString("parent")+object1.getString("city");

                                    JSONObject object2 = object.getJSONObject("data");
                                    //todo 昨日信息
                                    JSONObject object3 = object2.getJSONObject("yesterday");
                                    WeatherReportBean bean = new WeatherReportBean();
                                    bean.setDate(object3.getString("ymd"));
                                    bean.setWeek(object3.getString("week"));
                                    bean.setSunrise(object3.getString("sunrise"));
                                    bean.setSunset(object3.getString("sunset"));
                                    String high = getNumberForEx(object3.getString("high"));
                                    String low = getNumberForEx(object3.getString("low"));
                                    bean.setTemprate(low+" ~ "+high);
                                    bean.setType(object3.getString("type"));
                                    bean.setFengxiang(object3.getString("fx"));
                                    bean.setFengli(object3.getString("fl"));
                                    bean.setAqi(object3.getString("aqi"));
                                    bean.setNotice(object3.getString("notice"));
                                    bean.setAddress(address);
                                    mList.add(bean);
                                    JSONArray array = object2.getJSONArray("forecast");
                                    for (int i=0;i<array.length();i++){
                                        JSONObject object4 = array.getJSONObject(i);
                                        WeatherReportBean bean1 = new WeatherReportBean();
                                        bean1.setDate(object4.getString("ymd"));
                                        bean1.setWeek(object4.getString("week"));
                                        bean1.setSunrise(object4.getString("sunrise"));
                                        bean1.setSunset(object4.getString("sunset"));
                                        String highs = getNumberForEx(object4.getString("high"));
                                        String lows = getNumberForEx(object4.getString("low"));
                                        bean1.setTemprate(lows+" ~ "+highs);
                                        bean1.setType(object4.getString("type"));
                                        bean1.setFengxiang(object4.getString("fx"));
                                        bean1.setFengli(object4.getString("fl"));
                                        bean1.setAqi(object4.has("aqi")  ? object4.getString("aqi") : "");
                                        bean1.setNotice(object4.getString("notice"));
                                        bean1.setAddress(address);
                                        mList.add(bean1);
                                    }
                                    handler.sendEmptyMessage(GETWEATHERREPORTSUCCESS);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Info.sendMessage(handler,GETWEATHERREPORTFAIL,"获取失败3："+e);
                                }
                            }
                        });
            }
        }).start();

    }

    /**
     * 描述：从asset文件夹中读取城市名称和code的对应表
     * 作者：zzh
     */
    private class ReadCityCodeTask extends AsyncTask<Void,Object,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            String content = null;
            try {
                InputStreamReader reader = new InputStreamReader(getResources().getAssets().open("citycode.txt"), StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String line = "";
                StringBuilder builder = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null){
                    builder.append(line);
                }
                reader.close();
                bufferedReader.close();
                content = builder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert content != null;
            String[] contents = content.split(",");
            for (String s : contents) {
                String[] array = s.split("=");
                CityCodeBean bean = new CityCodeBean(array[0], array[1]);
                cityList.add(bean);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            handler.sendEmptyMessage(GETCITYCODESUCCESS);
        }
    }

    /**
     * 描述：使用正则表达式提取数字
     * @param key 字符串
     * @return 数字
     */
    private String getNumberForEx(String key){
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(key);
        return m.replaceAll("").trim();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LemonBubble.forceHide();
        handler.removeCallbacksAndMessages(null);
        if (mList != null && mList.size() != 0){
            mList.clear();
            mList = null;
        }
    }
}
