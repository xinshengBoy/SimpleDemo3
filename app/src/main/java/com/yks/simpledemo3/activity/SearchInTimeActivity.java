package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.adapter.BaseRecyclerAdapter;
import com.yks.simpledemo3.tools.Info;
import com.yks.simpledemo3.view.MyActionBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import net.lemonsoft.lemonbubble.LemonBubble;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 描述：及时搜索（边输入边显示结果）
 * 作者：zzh
 * time:2019/08/14
 */
public class SearchInTimeActivity extends Activity {

    private Activity mActivity = SearchInTimeActivity.this;
    private Context mContext = SearchInTimeActivity.this;

    private final int GETSEARCHINFOFAIL = 0;
    private final int GETSEARCHINFOSUCCESS = 1;

    private MyHandler handler;
    private SearchInTimeAdapter adapter;
    private List<String> mList = new ArrayList<>();

    private EditText et_search_intime;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_intime);

        handler = new MyHandler(mActivity);
        initView();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity,title_layout,"及时搜索","",false);

        et_search_intime = findViewById(R.id.et_search_intime);
        RecyclerView cv_search_intime = findViewById(R.id.cv_search_intime);
        adapter = new SearchInTimeAdapter(mList);
        Info.setRecycviewAdapter(mContext,cv_search_intime,adapter);
        adapter.setOnItemClickListner(new BaseRecyclerAdapter.OnItemClickListner() {
            @Override
            public void onItemClickListner(View v, int position) {
                Info.showToast(mContext,mList.get(position),true);
                Info.playRingtone(mContext,true);
            }
        });

        et_search_intime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getSearchInfo(s.toString());
            }
        });
    }

    private class SearchInTimeAdapter extends BaseRecyclerAdapter<String>{

        SearchInTimeAdapter(List<String> mDatas) {
            super(mDatas, R.layout.item_search_intime);
        }

        @Override
        protected void bindData(BaseViewHolder holder, int position, String item) {
            TextView txt_item_search_intime = (TextView) holder.getView(R.id.txt_item_search_intime);

            txt_item_search_intime.setText(item);
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
                if (msg.what == GETSEARCHINFOSUCCESS) {//成功
                    adapter.refresh(mList);
                }
            }
        }
    }

    /**
     * 描述：请求淘宝首页搜索接口获取返回数据
     * 作者：zzh
     * @param text 输入的关键字
     */
    private void getSearchInfo(final String text){
        if (mList != null && mList.size() != 0){
            mList.clear();
        }
        MyApplication.cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post().url("https://suggest.taobao.com/sug?code=utf-8&q="+text+"&_ksTS=1565767537349_1103&callback=json&k=1&area=c2c&bucketid=17")
                        .tag(this)
                        .build()
                        .connTimeOut(5000L)
                        .readTimeOut(5000L)
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Info.sendMessage(handler,GETSEARCHINFOFAIL,"失败1："+e);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                try {
                                    response = response.replace("json(","");
                                    response = response.replace(")","");
                                    JSONObject object = new JSONObject(response);
                                    JSONArray array = object.getJSONArray("result");
                                    if (array.length() != 0){
                                        for (int i=0;i<array.length();i++){
                                            JSONArray data =  array.getJSONArray(i);
                                            for (int j=0;j<data.length();j++){
                                                if (j == 0){
                                                    mList.add(data.getString(j));
                                                    continue;
                                                }
                                            }
                                        }
                                    }
                                    handler.sendEmptyMessage(GETSEARCHINFOSUCCESS);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    handler.sendEmptyMessage(GETSEARCHINFOSUCCESS);
                                }
                            }
                        });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LemonBubble.forceHide();
        handler.removeCallbacksAndMessages(null);
        OkHttpUtils.getInstance().cancelTag(this);
        if (mList != null && mList.size() != 0){
            mList.clear();
            mList = null;
        }
    }
}
