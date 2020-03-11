package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.promeg.pinyinhelper.Pinyin;
import com.github.promeg.tinypinyin.lexicons.android.cncity.CnCityDict;
import com.yks.simpledemo3.R;
import com.yks.simpledemo3.adapter.BaseRecyclerAdapter;
import com.yks.simpledemo3.bean.ItemOpenBean;
import com.yks.simpledemo3.view.MyActionBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：列表展开
 * 作者：zzh
 * time:2020/03/10
 * 汉字转拼音功能：https://github.com/promeG/TinyPinyin
 */
public class RecyclerItemOpenActivity extends Activity {

    private Activity mActivity = RecyclerItemOpenActivity.this;
    private Context mContext = RecyclerItemOpenActivity.this;
    private List<ItemOpenBean> mList = new ArrayList<>();
    private ItemOpenAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_item_open);

        Pinyin.init(Pinyin.newConfig().with(CnCityDict.getInstance(mContext)));
        initData();
        initView();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity, title_layout, "列表展开", "", false);

        RecyclerView recycler_open = findViewById(R.id.recycler_open);
        adapter = new ItemOpenAdapter(mList);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_open.setLayoutManager(manager);
        recycler_open.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.HORIZONTAL));//默认的分割线
        recycler_open.setAdapter(adapter);
    }

    private class ItemOpenAdapter extends BaseRecyclerAdapter<ItemOpenBean>{

        ItemOpenAdapter(List<ItemOpenBean> mDatas) {
            super(mDatas, R.layout.item_recycler_open);
        }

        @Override
        protected void bindData(BaseViewHolder holder, final int position, ItemOpenBean itemOpenBean) {
            LinearLayout item_open_lyout = (LinearLayout) holder.getView(R.id.item_open_lyout);
            TextView txt_item_open_title = (TextView) holder.getView(R.id.txt_item_open_title);
            final TextView txt_item_open_content = (TextView) holder.getView(R.id.txt_item_open_content);

            txt_item_open_title.setText(itemOpenBean.getTitle());
            //todo 添加将汉字转拼音的功能
            String pinyinText = Pinyin.toPinyin(itemOpenBean.getContent()," ").toLowerCase();
            txt_item_open_content.setText(itemOpenBean.getContent()+"\n"+ pinyinText);

            item_open_lyout.setBackgroundColor(position % 2 == 0 ? getResources().getColor(R.color.colorWhite) : getResources().getColor(R.color.colorGray3));

            item_open_lyout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mList.get(position).isShow()){
                        txt_item_open_content.setVisibility(View.GONE);
                        mList.get(position).setShow(false);
                    }else {
                        txt_item_open_content.setVisibility(View.VISIBLE);
                        mList.get(position).setShow(true);
                    }
                }
            });
        }
    }

    private void initData(){
        ItemOpenBean bean1 = new ItemOpenBean();
        bean1.setTitle("疫情肆虐！官方：意甲联赛停摆");
        bean1.setContent("直播吧3月10日讯 意大利总理孔特在电视讲话中宣布，全意大利的体育活动暂停，直至4月3日。");
        bean1.setShow(false);
        mList.add(bean1);
        ItemOpenBean bean2 = new ItemOpenBean();
        bean2.setTitle("热刺官方：贝尔温脚踝严重扭伤，将长期缺阵");
        bean2.setContent("直播吧3月10日讯 热刺官方消息，球队边锋贝尔温左脚踝严重扭伤，将长期缺阵。穆里尼奥在对莱比锡新闻发布会上表示：“不指望贝尔温本赛季能重返赛场。”");
        bean2.setShow(false);
        mList.add(bean2);
        ItemOpenBean bean3 = new ItemOpenBean();
        bean3.setTitle("官方：意球队欧战主场可空场进行");
        bean3.setContent("直播吧3月10日讯 意大利总理孔特在电视讲话中宣布，全意大利的教育和体育活动暂停，直至4月3日。但是，意大利政府在颁布的法令中表示，意大利国内赛事暂停并不影响欧战，欧战比赛可以空场进行。");
        bean3.setShow(false);
        mList.add(bean3);
        ItemOpenBean bean4 = new ItemOpenBean();
        bean4.setTitle("电讯：弗格森参加贝林汉姆谈判");
        bean4.setContent("直播吧3月10日讯 根据英国媒体《每日电讯报》的报道，周一曼联与贝林汉姆的父母进行了会面，红魔传奇教头弗格森也在场陪同，目的就是为了说服这名中场小将加盟老特拉福德。");
        bean4.setShow(false);
        mList.add(bean4);
        ItemOpenBean bean5 = new ItemOpenBean();
        bean5.setTitle("若英超空场比赛有可能免费转播");
        bean5.setContent("直播吧3月10日讯 据《邮报》消息，英超俱乐部已经和转播方展开讨论，商讨英超在空场的情况下进行免费直播的可能性。周一英超官方与政府体育文化部门、转播方召开会议，约有60人参加会议。目前的情况下，英超被告知比赛无需延期或者空场进行。");
        bean5.setShow(false);
        mList.add(bean5);
        ItemOpenBean bean6 = new ItemOpenBean();
        bean6.setTitle("瓦尔迪：罚点前雷纳跟我心理战");
        bean6.setContent("今天凌晨的英超比赛，莱斯特城主场4-0大胜阿斯顿维拉，巩固了球队在积分榜第三的位置。头号射手瓦尔迪本场复出，并打进两球");
        bean6.setShow(false);
        mList.add(bean6);
        ItemOpenBean bean7 = new ItemOpenBean();
        bean7.setTitle("蒙托利沃：所有人都应居家隔离");
        bean7.setContent("这是今天的新冠疫情确诊人数，就在过去短短的24小时内发生了什么？现在你们是否明白我们已经要崩溃了？现在你们是否懂得居家隔离的重要性？");
        bean7.setShow(false);
        mList.add(bean7);
        ItemOpenBean bean8 = new ItemOpenBean();
        bean8.setTitle("因斯：曼联看上去已不需要博格巴 ");
        bean8.setContent("直播吧3月10日讯 曼联名宿保罗-因斯在接受Paddy Power News采访时表示，曼联有了布鲁诺-费尔南德斯之后，感觉他们已经不再需要博格巴，即使博格巴复出，他也需要为自己的位置而战。");
        bean8.setShow(false);
        mList.add(bean8);
        ItemOpenBean bean9 = new ItemOpenBean();
        bean9.setTitle("哈里-威尔逊：穿红军外套因天冷 ");
        bean9.setContent("在周末伯恩茅斯与利物浦的比赛中，租借在伯恩茅斯效力的红军球员哈里-威尔逊被拍到穿着利物浦外套坐在场下，这引起了球迷的不满，对此他做出了解释");
        bean9.setShow(false);
        mList.add(bean9);
        ItemOpenBean bean10 = new ItemOpenBean();
        bean10.setTitle("世体：巴萨已禁那不勒斯球迷购票  ");
        bean10.setContent("巴萨将于今日会晤加泰卫生署，商讨欧冠次回合主场对阵那不勒斯的比赛是否空场进行。此外，目前巴萨已禁止客队球迷购票");
        bean10.setShow(false);
        mList.add(bean10);
        ItemOpenBean bean11 = new ItemOpenBean();
        bean11.setTitle("监狱看守员：小罗在狱中笑盈盈");
        bean11.setContent("小罗因护照造假被巴拉圭拘留。不幸中的万幸是，小罗很快就适应了监狱的生活，并且保持着很好的精神状态");
        bean11.setShow(false);
        mList.add(bean11);
        ItemOpenBean bean12 = new ItemOpenBean();
        bean12.setTitle("恒大队医：必须暂停所有体育赛事");
        bean12.setContent("卡斯特拉齐表示中国对新冠疫情的反应很快并迅速让中超延期，而且没有造成舆论压力，现在意大利必须迅速停止一切体育赛事");
        bean12.setShow(false);
        mList.add(bean12);
        ItemOpenBean bean13 = new ItemOpenBean();
        bean13.setTitle("于大宝赠送球衣鼓励援鄂女球迷 ");
        bean13.setContent("今晚，国安球员于大宝在社交平台发文，向支援武汉一线的国安女球迷医护人员送上了鼓励");
        bean13.setShow(false);
        mList.add(bean13);
        ItemOpenBean bean14 = new ItemOpenBean();
        bean14.setTitle("天海球员：做好没钱踢完中超打算");
        bean14.setContent("我们都做好了在几乎没钱的情况下，过穷日子、苦日子自己踢完中超这个赛季的打算，如果你们愿意来投资");
        bean14.setShow(false);
        mList.add(bean14);
        ItemOpenBean bean15 = new ItemOpenBean();
        bean15.setTitle("J联赛主席：继续延期J联赛");
        bean15.setContent("然而疫情影响未见消退，日前J联赛主席村井满接受采访时表示，J联赛俱乐部已一致同意继续推迟比赛。");
        bean15.setShow(false);
        mList.add(bean15);
        ItemOpenBean bean16 = new ItemOpenBean();
        bean16.setTitle("萨拉赫对圣徒进球当选红军月最佳");
        bean16.setContent("利物浦官方公布了队内二月最佳进球的评选结果，萨拉赫对阵南安普顿时的进球顺利当选。");
        bean16.setShow(false);
        mList.add(bean16);
        ItemOpenBean bean17 = new ItemOpenBean();
        bean17.setTitle("官方：中韩女足比赛延期至6月");
        bean17.setContent("今天早些时候亚足联官方确认中韩女足奥预赛将在首回合比赛在4月9日，次回合比赛在4月14日，具体的比赛地点还未确认。");
        bean17.setShow(false);
        mList.add(bean17);
        ItemOpenBean bean18 = new ItemOpenBean();
        bean18.setTitle("记者：山东省体改造因疫情推迟");
        bean18.setContent("由于济南奥体中心将为了2021年世俱杯进行大规模维修改造，因此鲁能新赛季的主场将迁回省体育中心体育场，这也得到了山东省体官方公众号的确认。");
        bean18.setShow(false);
        mList.add(bean18);
        ItemOpenBean bean19 = new ItemOpenBean();
        bean19.setTitle("不服老！75岁埃及球员点射破门");
        bean19.setContent("75岁的埃及球员伊兹-埃尔丁-巴哈德（Ezz El-Din Bahader）是世界足坛年纪最大的职业球员，他在去年年末的处子秀中攻入一粒点球，帮助球队1-1战平对手。");
        bean19.setShow(false);
        mList.add(bean19);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mList != null && mList.size() != 0){
            mList.clear();
            mList = null;
        }
    }
}
