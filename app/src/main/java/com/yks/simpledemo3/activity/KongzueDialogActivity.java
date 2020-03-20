package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.interfaces.OnDismissListener;
import com.kongzue.dialog.interfaces.OnInputDialogButtonClickListener;
import com.kongzue.dialog.interfaces.OnMenuItemClickListener;
import com.kongzue.dialog.interfaces.OnNotificationClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.util.InputInfo;
import com.kongzue.dialog.util.TextInfo;
import com.kongzue.dialog.v3.BottomMenu;
import com.kongzue.dialog.v3.InputDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.kongzue.dialog.v3.Notification;
import com.kongzue.dialog.v3.TipDialog;
import com.kongzue.dialog.v3.WaitDialog;
import com.yks.simpledemo3.R;
import com.yks.simpledemo3.tools.Info;
import com.yks.simpledemo3.view.MyActionBar;

import net.lemonsoft.lemonbubble.LemonBubble;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：空竹对话框
 * 作者：zzh
 * time:2020/03/16
 * https://github.com/kongzue/DialogV3
 */
public class KongzueDialogActivity extends AppCompatActivity {

    private Activity mActivity = KongzueDialogActivity.this;
    private Context mContext = KongzueDialogActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_kongzue_dialog);

        initView();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity,title_layout,"空竹对话框","",false);
    }

    public void basicSingleDialog(View view){
        MessageDialog.show(this,"体育","AC米兰计划2500万出售帕克塔，暂时无人问津","确定");
    }

    public void basicDoubleDialog(View view){
        MessageDialog.show(this,"财经","OPPO向海外援助30万只口罩","确定","取消");
    }

    public void basicThirdDialog(View view){
        MessageDialog.show(this,"要闻","载600名英国人游轮在巴哈马海域搁浅，5人确诊感染新冠病毒","确定","取消","其他")
        .setOnOtherButtonClickListener(new OnDialogButtonClickListener() {
            @Override
            public boolean onClick(BaseDialog baseDialog, View v) {
                Info.playRingtone(mContext,true);
                return false;
            }
        });
    }

    public void basicVerticalDialog(View view){
        MessageDialog.show(this,"足球","今夏离开曼联？伊哈洛：我仍是上海申花的一员，希望中超尽快开始","确定","取消","去看看")
                .setButtonOrientation(LinearLayout.VERTICAL)
                .setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        Info.playRingtone(mContext,true);
                        return false;
                    }
                });
    }

    public void basicInputDialog(View view){
        InputDialog.show(this,"提示","请输入密码","确定","取消")
                .setOnOkButtonClickListener(new OnInputDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {
                        Info.showToast(mContext,inputStr,true);
                        Info.playRingtone(mContext,true);
                        return false;
                    }
                });
    }

    public void basicInputHintDialog(View view){
        InputDialog.show(this,"用户名","请输入昵称","确定")
                .setInputText("牛叉叉的华爷")
                .setHintText("请输入昵称");
    }

    public void inputLimitDialog(View view){
        InputDialog.show(this,"密码","请输入密码","确定","取消")
                .setInputInfo(new InputInfo()
                .setMAX_LENGTH(8)
                .setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .setTextInfo(new TextInfo()
                .setFontColor(Color.RED)))
                .setOnOkButtonClickListener(new OnInputDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {
                        Info.showToast(mContext,inputStr,true);
                        Info.playRingtone(mContext,true);
                        return false;
                    }
                }).setBackgroundResId(R.mipmap.bg);
    }

    public void inputAreaDialog(View view){
        InputDialog.show(this,"备注","请输入备注","确定","取消")
                .setInputInfo(new InputInfo()
                .setMultipleLines(true))
                .setOnOkButtonClickListener(new OnInputDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {
                        TipDialog.show(KongzueDialogActivity.this,inputStr,TipDialog.TYPE.SUCCESS).setTipTime(2000);
                        return false;
                    }
                });
    }

    public void waitingDialog(View view){
        WaitDialog.show(this,"获取中...");
    }

    public void warnningDialog(View view){
        TipDialog.show(this,"当前数据不一定准确",TipDialog.TYPE.WARNING);
    }

    public void successDialog(View view){
        TipDialog.show(this,"提交成功",TipDialog.TYPE.SUCCESS).setTipTime(3000);
    }

    public void errorDialog(View view){
        TipDialog.show(this,"释放失败",TipDialog.TYPE.ERROR);
    }

    public void tipImageDialog(View view){
        TipDialog.show(this,"申请",R.mipmap.card).setTheme(DialogSettings.THEME.LIGHT);
    }

    public void bottomMenuDialog1(View view){
        BottomMenu.show(this, new String[]{"打开相机", "相册选择"}, new OnMenuItemClickListener() {
            @Override
            public void onClick(String text, int index) {
                Info.showToast(mContext,text,true);
                Info.playRingtone(mContext,true);
            }
        }).setTitle("上传头像");
    }

    public void customBottomMenuDialog(View view){
        List<String> mList = new ArrayList<>();
        mList.add("A301");
        mList.add("A302");
        mList.add("A303");
        mList.add("A304");
        BaseAdapter adapter = new ArrayAdapter<>(mContext,R.layout.item_botton_menu,R.id.text_items,mList);
        BottomMenu.show(this, adapter, new OnMenuItemClickListener() {
            @Override
            public void onClick(String text, int index) {
                Info.showToast(mContext,text,true);
                Info.playRingtone(mContext,true);
            }
        }).setTitle("请选择SKU");
    }

    public void topNotifucationDialog1(View view){
        Notification.show(this,"新消息","出来坐坐呀").setDurationTime(Notification.DURATION_TIME.LONG);
    }

    public void topNotifucationDialog2(View view){
        Notification.show(this,"新消息","出来坐坐呀",R.mipmap.message_verification)
                .setOnNotificationClickListener(new OnNotificationClickListener() {
                    @Override
                    public void onClick() {
                        Info.showToast(mContext,"小姐姐叫你吃饭了",true);
                        Info.playRingtone(mContext,true);
                    }
                }).setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                Info.showToast(mContext,"没时间",false);
                Info.playRingtone(mContext,false);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LemonBubble.forceHide();
        BaseDialog.unload();
    }
}
