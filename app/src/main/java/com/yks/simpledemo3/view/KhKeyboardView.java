package com.yks.simpledemo3.view;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.yks.simpledemo3.R;

import java.util.List;

/**
 * 描述：
 * 作者：
 * time:2019/07/15
 */
public class KhKeyboardView {
    private Activity mContext;
    private View parentBiew;
    private KeyboardView mLetterView;//字母键盘view
    private KeyboardView mNumberView;//数字键盘view
    private Keyboard mNumberKeyboard;//数字键盘
    private Keyboard mLetterKeyboard;//字母键盘
    private Keyboard mSymbolKeyboard;//符号键盘
    private boolean isNumber = true;//是否为数字键盘
    public static boolean isUpper = false;//是否大写
    private boolean isSymbol = false;//是否为符号键盘
    private EditText mEditText;
    private View headerView;//顶部完成视图

    public KhKeyboardView(Activity context, View view){
        mContext = context;
        parentBiew = view;

        mNumberKeyboard = new Keyboard(mContext, R.xml.keyboard_numbers);
        mLetterKeyboard = new Keyboard(mContext,R.xml.keyboard_word);
        mSymbolKeyboard = new Keyboard(mContext,R.xml.keyboard_symbol);

        mNumberView = parentBiew.findViewById(R.id.keyboard_view);
        mLetterView = parentBiew.findViewById(R.id.keyboard_view2);

        mNumberView.setKeyboard(mNumberKeyboard);
        mNumberView.setEnabled(true);
        mNumberView.setPreviewEnabled(false);
        mNumberView.setOnKeyboardActionListener(listener);

        mLetterView.setKeyboard(mLetterKeyboard);
        mLetterView.setEnabled(true);
        mLetterView.setPreviewEnabled(true);
        mLetterView.setOnKeyboardActionListener(listener);

        headerView = parentBiew.findViewById(R.id.keyboard_header);
    }

    public void setEditText(EditText et){
        mEditText = et;
    }

    private KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int primaryCode) {
            if (primaryCode == Keyboard.KEYCODE_SHIFT){
                List<Keyboard.Key> keyList = mLetterKeyboard.getKeys();
                mLetterView.setPreviewEnabled(false);
            }else if (primaryCode == Keyboard.KEYCODE_DELETE){
                mLetterView.setPreviewEnabled(false);
            }else if (primaryCode == 32){
                mLetterView.setPreviewEnabled(false);
            }else {
                mLetterView.setPreviewEnabled(true);
            }
        }

        @Override
        public void onRelease(int primaryCode) {

        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            try {
                if (mEditText == null){
                    return;
                }
                Editable editable = mEditText.getText();
                int start = mEditText.getSelectionStart();
                if (primaryCode == Keyboard.KEYCODE_CANCEL){//隐藏软键盘
                    hideKeyboard();
                }else if (primaryCode == Keyboard.KEYCODE_DELETE || primaryCode == -35){//回退键，删除字符
                    if (editable != null && editable.length() > 0){
                        if (start > 0){
                            editable.delete(start-1,start);
                        }
                    }
                }else if (primaryCode == Keyboard.KEYCODE_SHIFT){//大小写切换
                    changeKeyBoard();
                    mLetterView.setKeyboard(mLetterKeyboard);
                }else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE){//字母和数字的切换
                    if (isNumber){
                        showLetterView();
                        showLetterView2();
                    }else {
                        showNumberView();
                    }
                }else if (primaryCode == 90001){//字母与符号的切换
                    if (isSymbol){
                        showLetterView2();
                    }else {
                        showSymbolView();
                    }
                }else {
                    editable.insert(start,Character.toString((char) primaryCode));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void onText(CharSequence text) {

        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }
    };

    private void showSymbolView() {
        try {
            if (mLetterKeyboard != null){
                isSymbol = true;
                mLetterView.setKeyboard(mSymbolKeyboard);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void showNumberView() {
        try {
            if (mNumberView != null && mLetterView != null){
                isNumber = true;
                mNumberView.setVisibility(View.VISIBLE);
                mLetterView.setVisibility(View.INVISIBLE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void showLetterView2() {
        if (mLetterView != null){
            isSymbol = false;
            mLetterView.setKeyboard(mLetterKeyboard);
        }
    }

    private void showLetterView() {
        try {
            if (mLetterView != null && mNumberView != null){
                isNumber = false;
                mLetterView.setVisibility(View.VISIBLE);
                mNumberView.setVisibility(View.INVISIBLE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void changeKeyBoard() {
        List<Keyboard.Key> mList = mLetterKeyboard.getKeys();
        if (isUpper){
            //当前是大写，切换成小写
            isUpper = false;
            for (Keyboard.Key key : mList){
                Drawable icon = key.icon;
                if (key.label != null && isLetter(key.label.toString())){
                    key.label = key.label.toString().toLowerCase();
                    key.codes[0] = key.codes[0] + 32;
                }
            }
        }else {
            //当前是小写，切换成大写
            isUpper = true;
            for (Keyboard.Key key : mList){
                if (key.label != null && isLetter(key.label.toString())){
                    key.label = key.label.toString().toUpperCase();
                    key.codes[0] = key.codes[0] - 32;
                }
            }
        }
    }

    /**
     * 描述：判断是否是字母
     * @param str 传入的字符串
     * @return 返回判断结果
     */
    private boolean isLetter(String str) {
        String wordStr = "abcdefghijklmnopqrstuvwxyz";
        return wordStr.contains(str.toLowerCase());
    }

    /**
     * 描述：隐藏软键盘
     * 作者：zzh
     */
    public void hideKeyboard() {
        try{
            int visible = mLetterView.getVisibility();
            if (visible == View.VISIBLE){
                headerView.setVisibility(View.GONE);
                mLetterView.setVisibility(View.GONE);
            }
            visible = mNumberView.getVisibility();
            if (visible == View.VISIBLE){
                headerView.setVisibility(View.GONE);
                mNumberView.setVisibility(View.GONE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 描述：显示软键盘
     * 作者：zzh
     * @param editText 要显示的控件
     */
    public void showKeyboard(EditText editText){
        try {
            this.mEditText = editText;
            int visibility = 0;
            int inputText = mEditText.getInputType();
            headerView.setVisibility(View.VISIBLE);

            switch (inputText){
                case InputType.TYPE_CLASS_NUMBER:
                    showNumberView();
                    break;
                case InputType.TYPE_CLASS_PHONE:
                    showNumberView();
                    break;
                case InputType.TYPE_NUMBER_FLAG_DECIMAL:
                    showNumberView();
                    break;
                default:
                    showLetterView();
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
