package com.yks.simpledemo3.tools;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.view.KhKeyboardView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 * 作者：
 * time:2019/07/15
 */
public class KeyBoardDialogUtils implements View.OnClickListener {

    protected Activity mActivity;
    private List<String> contentList;
    private Dialog popWindow;
    protected View view;
    private EditText contentView;
    private KhKeyboardView keyboardUtil;

    public KeyBoardDialogUtils(Activity context){
        try {
            this.mActivity = context;
            if (contentList == null){
                contentList = new ArrayList<>();
            }

            if (popWindow == null){
                view = LayoutInflater.from(mActivity).inflate(R.layout.keyboard_key_board_popu,null);
                popWindow = new Dialog(mActivity,R.style.keyboard_popupAnimation);
                view.findViewById(R.id.keyboard_finish).setOnClickListener(this);
                view.findViewById(R.id.keyboard_back_hide).setOnClickListener(this);
            }

            popWindow.setContentView(view);
            popWindow.setCanceledOnTouchOutside(true);
            Window window = popWindow.getWindow();
            window.setWindowAnimations(R.style.keyboard_popupAnimation);
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            window.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL);
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            popWindow.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (contentView != null && contentView.isFocused()){
                        contentView.clearFocus();
                    }
                }
            });
            initView();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initView() {
        try {
            if (keyboardUtil == null){
                keyboardUtil = new KhKeyboardView(mActivity,view);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        try {
            int i = v.getId();
            if (i == R.id.keyboard_finish){
                keyboardUtil.hideKeyboard();
                dismiss();
            }else if (i == R.id.keyboard_back_hide){
                keyboardUtil.hideKeyboard();
                dismiss();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 描述：隐藏系统软键盘
     * @param et 编辑框
     */
    public void hideSystemSoftKeyBoard(EditText et){
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= 11){
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus",boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(et,false);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }else {
            et.setInputType(InputType.TYPE_NULL);
        }

        //当前已显示软键盘则隐藏软键盘
        InputMethodManager imm = (InputMethodManager) mActivity.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(),0);
    }

    public void show(EditText et){
        et.setFocusable(true);
        et.setFocusableInTouchMode(true);
        et.requestFocus();
        popWindow.show();
        keyboardUtil.showKeyboard(et);
    }

    public void dismiss(){
        if (popWindow != null && popWindow.isShowing()){
            popWindow.dismiss();
        }
    }
}
