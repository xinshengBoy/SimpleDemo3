package com.yks.simpledemo3.view;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.KeyEvent;

import java.util.regex.Pattern;

/**
 * 描述：
 * 作者：zzh
 * time:2020/03/09
 */
public class BankCardEditText extends AppCompatEditText {

    private static final char KEY = ' ';

    public BankCardEditText(Context context) {
        super(context);
        init();
    }

    public BankCardEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        //todo 添加过滤器
        setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (dstart != dend){//删除字符
                    return null;
                }
                if (!isInteger(source.toString())){
                    return null;
                }
                //todo 在输入框的文本后面继续输入
                if (dstart >= dest.toString().length()){
                    if (isKeyPosition(dend)){
                        return " " + source;
                    }
                }else {
                    //重新设置格式
                    StringBuffer buffer = new StringBuffer(dest);
                    buffer.insert(dstart,source);
                    checkPattern(buffer);
                    return null;
                }
                return null;
            }
        }});
    }

    /**
     * 描述：删除事件处理，有些键盘监听不到此事件
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        checkPattern();
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        String b = text.toString();
        super.setText(text, type);
    }

    @Override
    public boolean onTextContextMenuItem(int id) {
        switch (id){
            case android.R.id.paste://处理粘贴事件
                super.onTextContextMenuItem(id);
                checkPattern();
                return true;
            default:
                return super.onTextContextMenuItem(id);
        }

    }

    /**
     * 描述：删除多余空格，在指定位置输入空格
     * 作者：zzh
     */
    private void checkPattern(){
        StringBuffer text = new StringBuffer(getText());
        //获取当前光标的位置
        int selectionStart = getSelectionStart();
        for (int i=0;i<text.length();i++){
            char charAt = text.charAt(i);
            if (isKeyPosition(i)){
                if (charAt != KEY){
                    text.insert(i,KEY);
                    if (i<selectionStart){
                        selectionStart++;
                    }
                }
            }else {
                if (charAt == KEY){
                    text.delete(i,i+1);
                    //移动光标
                    if (i<selectionStart){
                        selectionStart--;
                    }
                }
            }
        }
        setText(text);
        setSelection(Math.min(text.length(),selectionStart));
    }
    /**
     * 处理中间输入
     */
    private void checkPattern(StringBuffer text) {
        //获取当前光标的位置
        int selectionStart = getSelectionStart();
        for (int i = 0; i < text.length(); i++) {
            char charAt = text.charAt(i);
            if (isKeyPosition(i)) {
                if (charAt != KEY) {
                    text.insert(i, KEY);
                }
            }else{
                if (charAt==KEY){
                    text.delete(i,i+1);
                }
            }
        }
        setText(text);
        if (text.charAt(selectionStart)==KEY){
            selectionStart+=2;
        }else {
            selectionStart++;
        }
        setSelection(Math.min(text.length(), selectionStart));
    }
    /**
     * 描述：计算规则，每输入4个号码加一个空格
     * @param position 当前输入的位置
     * @return 是否需要加空格
     */
    private boolean isKeyPosition(int position){
        return (position+1) % 5 == 0;
    }

    private boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
}
