package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.adapter.BaseRecyclerAdapter;
import com.yks.simpledemo3.bean.CitySelectBean;
import com.yks.simpledemo3.tools.Info;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 描述：
 * 作者：zzh
 * time:2020/03/20
 */
public class InternationalSelectActivity extends Activity {

    private Context mContext = InternationalSelectActivity.this;

    private List<CitySelectBean> mList = new ArrayList<>();
    private List<CitySelectBean.Country> countryList = new ArrayList<>();

    private TextView txt_international_city;
    private StateAdapter adapter;
    private CountryAdapter countryAdapter;
    private String stateStr = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_international_select);

        initData();
        initView();
    }

    private void initView() {
        txt_international_city = findViewById(R.id.txt_international_city);
        RecyclerView cv_state = findViewById(R.id.cv_state);
        RecyclerView cv_country = findViewById(R.id.cv_country);

        adapter = new StateAdapter(mList);
        Info.setRecycviewAdapter(mContext,cv_state, adapter);
        adapter.setOnItemClickListner(new BaseRecyclerAdapter.OnItemClickListner() {
            @Override
            public void onItemClickListner(View v, int position) {
                stateStr = mList.get(position).getStateName();//选择的大洲
                for (int i=0;i<mList.size();i++){
                    if (stateStr.equals(mList.get(i).getStateName())){
                        mList.get(i).setSelect(true);
                    }else {
                        mList.get(i).setSelect(false);
                    }
                }
                adapter.refresh(mList);
                countryList = mList.get(position).getCountry();
                countryAdapter.refresh(countryList);
            }
        });

        countryList = mList.get(0).getCountry();
        stateStr = mList.get(0).getStateName();
        countryAdapter = new CountryAdapter(countryList);
        Info.setRecycviewAdapter(mContext,cv_country,countryAdapter);
    }

    private class StateAdapter extends BaseRecyclerAdapter<CitySelectBean>{

        StateAdapter(List<CitySelectBean> mDatas) {
            super(mDatas, R.layout.item_state);
        }

        @Override
        protected void bindData(final BaseViewHolder holder, final int position, CitySelectBean citySelectBean) {
            TextView txt_item_statename = (TextView) holder.getView(R.id.txt_item_statename);
            txt_item_statename.setText(citySelectBean.getStateName());
            if (citySelectBean.isSelect()){
                txt_item_statename.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            }else {
                txt_item_statename.setBackgroundColor(getResources().getColor(R.color.colorGray4));
            }
        }
    }

    private class CountryAdapter extends BaseRecyclerAdapter<CitySelectBean.Country>{

        CountryAdapter(List<CitySelectBean.Country> mDatas) {
            super(mDatas, R.layout.item_country);
        }

        @Override
        protected void bindData(BaseViewHolder holder, int position, final CitySelectBean.Country citySelectBean) {
            TextView txt_item_countryname = (TextView) holder.getView(R.id.txt_item_countryname);
            final ImageView iv_item_open = (ImageView) holder.getView(R.id.iv_item_open);
            final RecyclerView cv_city = (RecyclerView) holder.getView(R.id.cv_city);

            txt_item_countryname.setText(citySelectBean.getCountryName());

            GridLayoutManager manager = new GridLayoutManager(mContext,3);
            cv_city.setLayoutManager(manager);
            String city = citySelectBean.getCityName();
            String[] citys = city.split(",");
            final List<String> cityList = new ArrayList<>(Arrays.asList(citys));
            CityAdapter cityAdapter = new CityAdapter(cityList);
            cv_city.setAdapter(cityAdapter);

            cv_city.setVisibility(View.GONE);
            iv_item_open.setImageResource(R.mipmap.ic_open);

            final boolean[] isOpen = {false};
            iv_item_open.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isOpen[0]){
                        cv_city.setVisibility(View.GONE);
                        iv_item_open.setImageResource(R.mipmap.ic_open);
                        isOpen[0] = false;
                    }else {
                        cv_city.setVisibility(View.VISIBLE);
                        iv_item_open.setImageResource(R.mipmap.ic_close);
                        isOpen[0] = true;
                    }
                }
            });

            cityAdapter.setOnItemClickListner(new OnItemClickListner() {
                @Override
                public void onItemClickListner(View v, int position) {
                    txt_international_city.setText(cityList.get(position));
                    Info.showToast(mContext,stateStr + "-" + citySelectBean.getCountryName()+"-"+cityList.get(position),true);
                    Info.playRingtone(mContext,true);
                }
            });
        }
    }

    private class CityAdapter extends BaseRecyclerAdapter<String>{

        CityAdapter(List<String> mDatas) {
            super(mDatas, R.layout.item_city);
        }

        @Override
        protected void bindData(BaseViewHolder holder, int position, String s) {
            TextView txt_item_cityname = (TextView) holder.getView(R.id.txt_item_cityname);
            txt_item_cityname.setText(s);

        }
    }

    private void initData(){
        //亚洲
        List<CitySelectBean.Country> asiaList = new ArrayList<>();
        asiaList.add(new CitySelectBean.Country("中国","北京,上海,广州,深圳,武汉,长沙"));
        asiaList.add(new CitySelectBean.Country("中国香港","香港,"));
        asiaList.add(new CitySelectBean.Country("中国台湾","台北,高雄,台南"));
        asiaList.add(new CitySelectBean.Country("日本","东京,广岛,大阪,京都,札幌,福冈"));
        asiaList.add(new CitySelectBean.Country("韩国","首尔,釜山,光州,济州,蔚山,三星"));
        asiaList.add(new CitySelectBean.Country("印度","新德里,德里,孟买,加尔各答,班加罗尔,印多尔"));
        mList.add(new CitySelectBean(true,"亚洲", asiaList));
        //北美洲
        List<CitySelectBean.Country> northAmericaList = new ArrayList<>();
        northAmericaList.add(new CitySelectBean.Country("美国","华盛顿,纽约,洛杉矶,西雅图,芝加哥,迈阿密"));
        northAmericaList.add(new CitySelectBean.Country("墨西哥","墨西哥城,蒙特雷,科利马,格雷罗,麦卡伦"));
        northAmericaList.add(new CitySelectBean.Country("加拿大","渥太华,魁北克,蒙特利尔,温哥华,卡里加尔"));
        mList.add(new CitySelectBean(false,"北美洲", northAmericaList));
        //南美洲
        List<CitySelectBean.Country> southAmericaList = new ArrayList<>();
        southAmericaList.add(new CitySelectBean.Country("巴西","巴西利亚,里约热内卢,圣保罗,累西腓,维多利亚,拉波萨"));
        southAmericaList.add(new CitySelectBean.Country("阿根廷","布宜诺斯艾利斯,蒙德维的亚,科尔杜瓦,圣拉斐尔"));
        southAmericaList.add(new CitySelectBean.Country("乌拉圭","巴伦迪内斯,拉斯卡诺,特立尼达"));
        mList.add(new CitySelectBean(false,"南美洲", southAmericaList));
        //欧洲
        List<CitySelectBean.Country> europeList = new ArrayList<>();
        europeList.add(new CitySelectBean.Country("英国","伦敦,利物浦,曼彻斯特,普利茅斯,南安普顿,谢菲尔德,剑桥,布莱顿,利兹,格拉斯哥,爱丁堡,伯明翰,纽尔斯卡,伯恩利"));
        europeList.add(new CitySelectBean.Country("法国","巴黎,波尔多,蒙彼利埃,里昂,尼斯,图卢兹,里尔"));
        europeList.add(new CitySelectBean.Country("德国","柏林,科隆,莱比锡,杜塞尔多夫,沃尔夫斯堡"));
        europeList.add(new CitySelectBean.Country("意大利","罗马,米兰,威尼斯,都灵,热那亚,博洛尼亚,那不勒斯"));
        europeList.add(new CitySelectBean.Country("俄罗斯","莫斯科,喀山,圣彼得堡,叶卡捷琳堡,索契,伏尔加格勒"));
        mList.add(new CitySelectBean(false,"欧洲", europeList));
        //非洲
        List<CitySelectBean.Country> africaList = new ArrayList<>();
        africaList.add(new CitySelectBean.Country("埃及","开罗,卢克索,亚历山大,塞得港,苏伊士,阿斯旺,贝尼苏韦夫,坦塔,伊斯梅利亚,曼索拉,达曼胡尔,迪斯沃克,十月六日城,法尤姆"));
        africaList.add(new CitySelectBean.Country("埃塞尔比亚","亚的斯亚贝巴,德赛,内格默特,贡德尔,胡梅拉,季马,阿尔巴门奇"));
        africaList.add(new CitySelectBean.Country("南非","比勒陀利亚,开普敦,好望角,伊丽莎白港,约翰内斯堡"));
        mList.add(new CitySelectBean(false,"非洲", africaList));
        //大洋洲
        List<CitySelectBean.Country> oceaniaList = new ArrayList<>();
        oceaniaList.add(new CitySelectBean.Country("澳大利亚","堪培拉,悉尼,墨尔本,阿德莱德,布里斯班,纽尔斯卡"));
        oceaniaList.add(new CitySelectBean.Country("新西兰","惠灵顿,基督城,奥克兰,纳皮尔,吉斯伯恩"));
        oceaniaList.add(new CitySelectBean.Country("密克罗尼西亚联邦","帕利基尔,波纳佩,科洛尼亚,南马杜尔,泰蒙岛"));
        mList.add(new CitySelectBean(false,"大洋洲", oceaniaList));
    }
}
