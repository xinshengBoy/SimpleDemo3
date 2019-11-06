package com.yks.simpledemo3.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.yks.simpledemo3.R;
import com.yks.simpledemo3.bean.ProvinceBean;
import com.yks.simpledemo3.tools.Info;
import com.yks.simpledemo3.view.MyActionBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 描述：时间选择器
 * 作者：zzh
 * time:2019/11/05
 * 参考：https://github.com/Bigkoo/Android-PickerView
 */
public class TimePickerActivity extends Activity implements View.OnClickListener {

    private Context mContext = TimePickerActivity.this;
    private Activity mActivity = TimePickerActivity.this;

    private WheelView picker_wheelview;
    private Button btn_time_picker,btn_options_picker;

    private List<ProvinceBean> provinceList = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_picker);

        initView();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity, title_layout, "时间选择器", "", false);

        picker_wheelview = findViewById(R.id.picker_wheelview);
        btn_time_picker = findViewById(R.id.btn_time_picker);
        btn_options_picker = findViewById(R.id.btn_options_picker);

        initWheelData();
        initOptionsData();
        btn_time_picker.setOnClickListener(this);
        btn_options_picker.setOnClickListener(this);
    }

    private void initWheelData(){
        picker_wheelview.setCyclic(false);
        final List<String> mList = new ArrayList<>();
        mList.add("Android");
        mList.add("Java");
        mList.add("PHP");
        mList.add("Python");
        mList.add("Kotlin");

        picker_wheelview.setAdapter(new ArrayWheelAdapter(mList));
        picker_wheelview.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                Info.showToast(mContext,"选择的语言是："+mList.get(index),true);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == btn_time_picker){
            TimePickerView pvTime = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Toast.makeText(mContext, format.format(date), Toast.LENGTH_SHORT).show();
                }
            }).setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                @Override
                public void onTimeSelectChanged(Date date) {

                }
            }).setType(new boolean[]{true,true,true,true,true,true})
                    .isDialog(true)
                    .addOnCancelClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //取消
                        }
                    }).setItemVisibleCount(5)//可见值
                    .setLineSpacingMultiplier(2.0f)
                    .isAlphaGradient(true)
                    .build();

            Dialog dialog = pvTime.getDialog();
            if (dialog != null){
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM);
                params.leftMargin = 0;
                params.rightMargin = 0;
                pvTime.getDialogContainerLayout().setLayoutParams(params);

                Window window = dialog.getWindow();
                if (window != null){
                    window.setGravity(Gravity.BOTTOM);
                    window.setDimAmount(0.3f);
                }
            }

            pvTime.show(btn_time_picker);
        }else if (v == btn_options_picker){
            OptionsPickerView options = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    Toast.makeText(mContext,provinceList.get(options1).getName()+"省"+options2Items.get(options1).get(options2)+"市",Toast.LENGTH_SHORT).show();
                }
            }).setTitleText("城市选择")
                    .setContentTextSize(20)//滚轮文字大小
            .setDividerColor(getResources().getColor(R.color.colorGray2))
                    .setSelectOptions(0,1)//设置默认选项
            .setBgColor(Color.WHITE)
                    .isRestoreItem(true)//切换是还原
            .isCenterLabel(false)//是否只显示中间选中项的lable文字
            .setLabels("省","市","区")
                    .setOutSideColor(getResources().getColor(R.color.colorGray))
                    .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                        @Override
                        public void onOptionsSelectChanged(int options1, int options2, int options3) {

                        }
                    }).build();

            options.setPicker(provinceList,options2Items);
            options.show(btn_options_picker);
        }
    }

    private void initOptionsData(){
        //选项1
        provinceList.add(new ProvinceBean(0,"湖南","很能吃辣","辣妹子"));
        provinceList.add(new ProvinceBean(1,"广东","基本不吃辣","吃胡建人"));
        provinceList.add(new ProvinceBean(2,"河南","面食之乡","好有钱"));

        //选项二
        ArrayList<String> hunan = new ArrayList<>();
        hunan.add("长沙");
        hunan.add("郴州");
        hunan.add("湘潭");
        hunan.add("永州");
        hunan.add("衡阳");
        ArrayList<String> guangdong = new ArrayList<>();
        guangdong.add("广州");
        guangdong.add("深圳");
        guangdong.add("韶关");
        guangdong.add("惠州");
        guangdong.add("潮州");
        ArrayList<String> henan = new ArrayList<>();
        henan.add("郑州");
        henan.add("南阳");
        henan.add("开封");
        henan.add("襄阳");
        henan.add("平顶山");

        options2Items.add(hunan);
        options2Items.add(guangdong);
        options2Items.add(henan);
    }
}
