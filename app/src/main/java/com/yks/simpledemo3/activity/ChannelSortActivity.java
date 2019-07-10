package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;

import com.cheng.channel.Channel;
import com.cheng.channel.ChannelView;
import com.yks.simpledemo3.R;
import com.yks.simpledemo3.view.MyActionBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：频道排序
 * 作者：zzh
 * time:2019/07/08
 * 参考示例：https://github.com/chengzhicao/ChannelView
 */
public class ChannelSortActivity extends Activity implements ChannelView.OnChannelListener{

    private Activity mActivity = ChannelSortActivity.this;
    private Context mContext = ChannelSortActivity.this;
    private ChannelView channel_view;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_sort);

        initView();
        initData();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity,title_layout,"频道排序","",false);
        channel_view = findViewById(R.id.channel_view);
    }

    private void initData() {
        String [] myChannel = {"要闻","军事","财经","娱乐","体育","NBA","地方站"};
        String [] recommendChannel1 = {"综艺","美食","视频","国际","科技","汽车","时尚","图片","推荐","股票","独家","动漫","文化","健康"};
        String [] recommendChannel2 = {"百态","星座","游戏","房产","教育","宠物","热榜","头条","最火","音乐","数码","电竞","社会","奇闻"};
        String [] recommendChannel3 = {"家居","生态","美容","天气","购物","情感","职场","电影","电视剧","健康","运动","足球","育儿","户外","都市","政法"};

        List<Channel> myChannelList = new ArrayList<>();
        List<Channel> recommendList1 = new ArrayList<>();
        List<Channel> recommendList2 = new ArrayList<>();
        List<Channel> recommendList3 = new ArrayList<>();

        for (int i=0;i<myChannel.length;i++){
            String aMyChannel = myChannel[i];
            Channel channel;
            if (i>2 && i<6){
                //设置频道归属板块，删除时会回到对应的板块
                channel = new Channel(aMyChannel,2,i);
            }else {
                channel = new Channel(aMyChannel,i);
            }
            myChannelList.add(channel);
        }

        for (String aMyChannel : recommendChannel1){
            Channel channel = new Channel(aMyChannel);
            recommendList1.add(channel);
        }
        for (String aMyChannel : recommendChannel2){
            Channel channel = new Channel(aMyChannel);
            recommendList2.add(channel);
        }
        for (String aMyChannel : recommendChannel3){
            Channel channel = new Channel(aMyChannel);
            recommendList3.add(channel);
        }

        channel_view.setChannelFixedCount(3);//设置固定频道
        channel_view.addPlate("我的频道",myChannelList);
        channel_view.addPlate("推荐视频",recommendList1);
        channel_view.addPlate("搞笑频道",recommendList2);
        channel_view.addPlate("正经频道",recommendList3);

        channel_view.inflateData();
        channel_view.setOnChannelItemClickListener(this);
    }

    @Override
    public void channelItemClick(int position, Channel channel) {

    }

    @Override
    public void channelEditFinish(List<Channel> channelList) {

    }

    @Override
    public void channelEditStart() {

    }
}
