package com.yks.simpledemo3.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
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
import com.yks.simpledemo3.view.AmountView;
import com.yks.simpledemo3.view.MyActionBar;
import com.yks.simpledemo3.view.MyGridView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Button btn_selects,btn_time_picker,btn_options_picker,btn_bottom_dialog;
    private View popupView;
    private PopupWindow popupWindow;
    private TranslateAnimation animation;

    private List<ProvinceBean> provinceList = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_picker);

        Info.getScreenHeight(mActivity);
        initView();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity, title_layout, "时间选择器", "", false);

        btn_selects = findViewById(R.id.btn_selects);
        picker_wheelview = findViewById(R.id.picker_wheelview);
        btn_time_picker = findViewById(R.id.btn_time_picker);
        btn_options_picker = findViewById(R.id.btn_options_picker);
        btn_bottom_dialog = findViewById(R.id.btn_bottom_dialog);

        initWheelData();
        initOptionsData();
        btn_selects.setOnClickListener(this);
        btn_time_picker.setOnClickListener(this);
        btn_options_picker.setOnClickListener(this);
        btn_bottom_dialog.setOnClickListener(this);
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
                picker_wheelview.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == btn_selects){
            picker_wheelview.setVisibility(View.VISIBLE);
        }else if (v == btn_time_picker){
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
        }else if (v == btn_bottom_dialog){
            showPopWindow();
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

    /**
     * 描述：显示底部弹出框
     */
    private void showPopWindow(){
        if (popupWindow == null){
            popupView = View.inflate(mContext,R.layout.popwindow_select,null);

            popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT,Info.KEY_HEIGHT*2);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    lightOffOrOn(true);
                }
            });

            String[] sizes = {"39","40","41","42","43","44","45"};
            String[] colors = {"黑色(单鞋款)","卡其(单鞋款)","米色(单鞋款)","气垫增高垫(非赠品)","黑色(加绒款)","卡其(加绒款)"};
            //尺寸
            MyGridView gv_size = popupView.findViewById(R.id.gv_size);
            List<Map<String,String>> sizeList = new ArrayList<>();
            for (int i=0;i<sizes.length;i++){
                Map<String,String> map = new HashMap<>();
                map.put("size",sizes[i]);
                sizeList.add(map);
            }
            gv_size.setAdapter(new SimpleAdapter(mContext,sizeList,R.layout.item_shopping_sizes,new String[]{"size"},new int[]{R.id.txt_item_sizes}));
            //颜色
            MyGridView gv_color = popupView.findViewById(R.id.gv_color);
            List<Map<String,String>> colorList = new ArrayList<>();
            for (int i=0;i<colors.length;i++){
                Map<String,String> map = new HashMap<>();
                map.put("color",colors[i]);
                colorList.add(map);
            }
            gv_color.setAdapter(new SimpleAdapter(mContext,colorList,R.layout.item_shopping_sizes,new String[]{"color"},new int[]{R.id.txt_item_sizes}));
            //数量
            AmountView view_amounts = popupView.findViewById(R.id.view_amounts);
            view_amounts.setGoodsStock(99);
            view_amounts.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
                @Override
                public void onAmountChange(View view, int amount) {
                    Toast.makeText(mContext,"数量："+amount,Toast.LENGTH_SHORT).show();
                }
            });

            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setFocusable(true);

            popupWindow.setOutsideTouchable(true);//点击其他地方会消失
            //平移动画
            animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,0,Animation.RELATIVE_TO_PARENT,0,Animation.RELATIVE_TO_PARENT,1,Animation.RELATIVE_TO_PARENT,0);
            animation.setInterpolator(new AccelerateInterpolator());
            animation.setDuration(200);
        }

        if (popupWindow.isShowing()){
            popupWindow.dismiss();
            lightOffOrOn(true);
        }

        popupWindow.showAtLocation(btn_bottom_dialog,Gravity.BOTTOM,0,0);
        popupView.startAnimation(animation);
        lightOffOrOn(false);
    }

    /**
     * 描述：设置手机屏幕暗亮
     * 作者：zzh
     * @param isOpen 是否需要暗亮
     */
    private void lightOffOrOn(boolean isOpen){
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = isOpen ? 1f : 0.3f;
        getWindow().setAttributes(lp);
    }
}
