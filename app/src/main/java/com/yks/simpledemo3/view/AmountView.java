package com.yks.simpledemo3.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.yks.simpledemo3.R;

/**
 * 描述：自定义购物选择数量的控件
 * 作者：zzh
 * time:2019/11/27
 * https://blog.csdn.net/whitley_gong/article/details/51956429
 */
public class AmountView extends LinearLayout implements View.OnClickListener, TextWatcher {

    private int amount = 1;//购买数量
    private int goodsStock = 99;//商品库存
    private Button btn_decrease,btn_increase;
    private EditText et_amount;
    private OnAmountChangeListener mListener;

    public AmountView(Context context) {
        this(context,null);
    }

    public AmountView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_amountview,this);
        btn_decrease = findViewById(R.id.btn_decrease);
        et_amount = findViewById(R.id.et_amount);
        btn_increase = findViewById(R.id.btn_increase);

        btn_decrease.setOnClickListener(this);
        btn_increase.setOnClickListener(this);
        et_amount.addTextChangedListener(this);

        TypedArray attributes = context.obtainStyledAttributes(attrs,R.styleable.AmountView);
        int btnWidth = attributes.getDimensionPixelSize(R.styleable.AmountView_amountBtnWidth,LayoutParams.WRAP_CONTENT);
        int btnTextSize = attributes.getDimensionPixelSize(R.styleable.AmountView_amountBtnTextSize,16);
        int tvWidth = attributes.getDimensionPixelSize(R.styleable.AmountView_amountTvWidth,80);
        int tvTextSize = attributes.getDimensionPixelSize(R.styleable.AmountView_amountTvTextSize,18);
        int tvTextColor = attributes.getColor(R.styleable.AmountView_amountTvTextColor, Color.BLACK);
        attributes.recycle();

        LayoutParams btnParams = new LayoutParams(btnWidth,LayoutParams.MATCH_PARENT);
        btn_decrease.setLayoutParams(btnParams);
        btn_increase.setLayoutParams(btnParams);
        if (btnTextSize != 0){
            btn_decrease.setTextSize(TypedValue.COMPLEX_UNIT_PX,btnTextSize);
            btn_increase.setTextSize(TypedValue.COMPLEX_UNIT_PX,btnTextSize);
        }

        LayoutParams tvParams = new LayoutParams(tvWidth,LayoutParams.MATCH_PARENT);
        et_amount.setLayoutParams(tvParams);
        if (tvTextSize != 0){
            et_amount.setTextSize(tvTextSize);
        }
        if (tvTextColor != 0){
            et_amount.setTextColor(tvTextColor);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btn_decrease){
            if (amount > 1){
                amount --;
                et_amount.setText(amount+"");
            }
        }else if (v == btn_increase){
            if (amount < goodsStock){
                amount ++;
                et_amount.setText(amount+"");
            }
        }
        et_amount.clearFocus();
        if (mListener != null){
            mListener.onAmountChange(this,amount);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().isEmpty()){
            return;
        }
        amount = Integer.parseInt(s.toString());
        if (amount > goodsStock){
            amount = goodsStock;
            et_amount.setText(goodsStock+"");
            return;
        }
        if (mListener != null){
            mListener.onAmountChange(this,amount);
        }
    }

    public interface OnAmountChangeListener{
        void onAmountChange(View view,int amount);
    }

    public void setOnAmountChangeListener(OnAmountChangeListener onAmountChangeListener){
        this.mListener = onAmountChangeListener;
    }

    /**
     * 描述：设置库存数量
     * 作者：zzh
     * @param stock 库存数量
     */
    public void setGoodsStock(int stock){
        this.goodsStock = stock;
    }
}
