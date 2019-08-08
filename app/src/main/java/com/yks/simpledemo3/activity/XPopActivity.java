package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.ImageViewerPopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopup.interfaces.OnSrcViewUpdateListener;
import com.lxj.xpopup.interfaces.XPopupImageLoader;
import com.yks.simpledemo3.R;
import com.yks.simpledemo3.tools.Info;
import com.yks.simpledemo3.view.MyActionBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述：XPop对话框，样式全
 * 作者：zzh
 * time:2019/08/07
 * https://github.com/li-xiaojun/XPopup
 */
public class XPopActivity extends Activity {

    private Activity mActivity = XPopActivity.this;
    private Context mContext = XPopActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xpop);

        initView();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity,title_layout,"XPop","",false);
    }
    /**
     * 描述：普通对话框
     * 作者：zzh
     */
    public void confirmDialog(View view){
        new XPopup.Builder(mContext).asConfirm("普通标题", "普通内容", new OnConfirmListener() {
            @Override
            public void onConfirm() {
                Info.showToast(mContext,"确认成功",true);
                Info.playRingtone(mContext,true);
            }
        }).show();
    }
    /**
     * 描述：输入对话框
     * 作者：zzh
     */
    public void inputDialog(View view){
        new XPopup.Builder(mContext).asInputConfirm("注册", "请输入用户名", new OnInputConfirmListener() {
            @Override
            public void onConfirm(String text) {
                Info.showToast(mContext,"用户名："+text+"可以在程序中使用",true);
                Info.playRingtone(mContext,true);
            }
        }).show();
    }
    /**
     * 描述：列表未选中对话框（默认）
     * 作者：zzh
     */
    public void listNoSelectedDialog(View view){
        new XPopup.Builder(mContext).asCenterList("你最喜欢的运动", new String[]{"篮球", "足球", "羽毛球", "乒乓球", "游泳"}, new OnSelectListener() {
            @Override
            public void onSelect(int position, String text) {
                Info.showToast(mContext,"你最喜欢的运动："+text,true);
                Info.playRingtone(mContext,true);
            }
        }).show();
    }
    /**
     * 描述：列表已选中对话框
     * 作者：zzh
     */
    public void listSelectedDialog(View view){
        new XPopup.Builder(mContext).asCenterList("你最喜欢的女明星", new String[]{"景甜", "杨超越", "马思纯", "傅晶", "高圆圆"}, new OnSelectListener() {
            @Override
            public void onSelect(int position, String text) {
                Info.showToast(mContext,"你最喜欢的女明星："+text,true);
                Info.playRingtone(mContext,true);
            }
        }).setCheckedPosition(0)
                .show();
    }
    /**
     * 描述：底部弹出列表选择框（未选中）
     * 作者：zzh
     */
    public void buttomListNoSelectedDialog(View view){
        new XPopup.Builder(mContext).asBottomList("你最喜欢的运动员", new String[]{"C罗", "梅西", "詹姆斯", "库里", "杜兰特", "汤神"}, new OnSelectListener() {
            @Override
            public void onSelect(int position, String text) {
                Info.showToast(mContext,"你最喜欢的运动员："+text,true);
                Info.playRingtone(mContext,true);
            }
        }).show();
    }
    /**
     * 描述：底部弹出列表选择框（已选中）
     * 作者：zzh
     */
    public void buttomListSelectedDialog(View view){
        new XPopup.Builder(mContext).asBottomList("你最喜欢的男明星", new String[]{"王宝强", "徐峥", "彭于晏", "胡歌", "金瀚"}, new OnSelectListener() {
            @Override
            public void onSelect(int position, String text) {
                Info.showToast(mContext,"你最喜欢的男明星："+text,true);
                Info.playRingtone(mContext,true);
            }
        }).setCheckedPosition(2).show();
    }
    /**
     * 描述：依附控件列表弹框
     * 作者：zzh
     */
    public void attachListDialog(View view){
        new XPopup.Builder(mContext)
                .atView(view)
                .asAttachList(new String[]{"删除", "分享", "取消"}, new int[]{R.mipmap.delete, R.mipmap.message, R.mipmap.clear}, new OnSelectListener() {
                    @Override
                    public void onSelect(int position, String text) {
                        Info.showToast(mContext,text + "成功",true);
                        Info.playRingtone(mContext,true);
                    }
                }).show();
    }
    /**
     * 描述：依附控件触碰到的列表弹窗
     * 作者：zzh
     */
    public void watchListDialog(View view){
        final XPopup.Builder builder = new XPopup.Builder(mContext).watchView(view);
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                builder.asAttachList(new String[]{"发送", "撤回", "取消"}, null, new OnSelectListener() {
                    @Override
                    public void onSelect(int position, String text) {
                        Info.showToast(mContext,text + "成功",true);
                        Info.playRingtone(mContext,true);
                    }
                }).show();
                return false;
            }
        });
    }
    /**
     * 描述：多张大图弹框
     * 作者：zzh
     */
    public void multiPictureDialog(final ImageView view){
        List<Object> multiPictures = new ArrayList<>();
        multiPictures.add(R.mipmap.banner1);
        multiPictures.add(R.mipmap.banner2);
        multiPictures.add(R.mipmap.banner3);
        multiPictures.add(R.mipmap.banner4);
//        int[] multiPictures = new int[]{R.mipmap.banner1,R.mipmap.banner2,R.mipmap.banner3,R.mipmap.banner4};
        new XPopup.Builder(mContext).asImageViewer(view, 0, multiPictures, new OnSrcViewUpdateListener() {
            @Override
            public void onSrcViewUpdate(@NonNull ImageViewerPopupView popupView, int position) {
//                popupView.updateSrcView((ImageView) multiPictures.getChildAt(position));
            }
        }, new XPopupImageLoader() {
            @Override
            public void loadImage(int position, @NonNull Object uri, @NonNull ImageView imageView) {

            }

            @Override
            public File getImageFile(@NonNull Context context, @NonNull Object uri) {
                return null;
            }
        }).show();
    }
}
