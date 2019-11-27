package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.view.MyActionBar;
import com.yks.simpledemo3.view.OrientSensor;
import com.yks.simpledemo3.view.SensorUtil;
import com.yks.simpledemo3.view.StepSensorBase;
import com.yks.simpledemo3.view.StepSensorPedometer;
import com.yks.simpledemo3.view.StepView;

/**
 * 描述：通过方向传感器记录行走轨迹
 * 作者：zzh
 * time:2019/11/26
 * https://blog.csdn.net/qq_32115439/article/details/62961016
 * https://github.com/lioilwin/StepOrient/blob/master/app/src/main/AndroidManifest.xml
 */
public class RecordFootLineActivity extends Activity implements StepSensorBase.StepCallBack , OrientSensor.OrientCallBack {

    private Context mContext = RecordFootLineActivity.this;
    private Activity mActivity = RecordFootLineActivity.this;
    private StepView view_step;
    private TextView step_text,orient_text;
    private StepSensorPedometer mStepSensor;
    private OrientSensor mOrientSenser;

    private int mStepLen = 50;//步长
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SensorUtil.getInstance().printAllSensor(mContext);//打印所有可用的传感器
        setContentView(R.layout.activity_record_footline);

        initView();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity, title_layout, "行走轨迹", "", false);

        view_step = findViewById(R.id.view_step);
        step_text = findViewById(R.id.step_text);
        orient_text = findViewById(R.id.orient_text);

        //todo 注册计步监听
        mStepSensor = new StepSensorPedometer(mContext,this);
        if (!mStepSensor.registerStep()){//不可用
            Toast.makeText(mContext,"计步功能不可用",Toast.LENGTH_SHORT).show();
        }
        //todo 注册方向监听
        mOrientSenser = new OrientSensor(mContext,this);
        if (!mOrientSenser.registerOrient()){
            Toast.makeText(mContext,"方向功能不可用",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void Step(int stepNum) {
        //回调
        step_text.setText("步数："+stepNum);
        view_step.autoAddoint(mStepLen);
    }

    @Override
    public void Orient(int orient) {
        orient_text.setText("方位："+orient);
        view_step.autoDrawArrow(orient);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mStepSensor.unregisterStep();
        mOrientSenser.unregisterOrient();
    }
}
