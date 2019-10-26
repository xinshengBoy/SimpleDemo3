package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.yks.simpledemo3.R;
import com.yks.simpledemo3.adapter.BaseRecyclerAdapter;
import com.yks.simpledemo3.bean.BluetoothBean;
import com.yks.simpledemo3.tools.Info;
import com.yks.simpledemo3.view.MyActionBar;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 描述：蓝牙的打开、搜索、列表、配对、连接、发送文件等
 * 作者：zzh
 * time:2019/10/22
 */
public class BluetoothActivity extends Activity {

    private Context mContext = BluetoothActivity.this;
    private Activity mActivity = BluetoothActivity.this;

    private final int GETCONTECTEDLIST = 0;//获取已配对列表
    private final int GETCONTECTEDLISTSUCCESS = 1;//获取已匹配列表成功
    private final int UPDATESEARCHLIST = 2;//搜索成功
    private final int UPDATELOCALDEVICENAME = 3;//更新本设备名称
    private final int UPDATECONTECTEDDEVICENAME = 4;//更新已配对设备名称
    private final int CONNECTFAIL = 5;//连接失败
    public final int MISC = 0x0000;//麦克风
    public final int COMPUTER = 0x0100;//电脑
    public final int PHONE = 0x0200;//手机
    public final int NETWORKING = 0x0300;//网络
    public final int AUDIO_VIDEO = 0x0400;//音视频
    public final int PERIPHERAL = 0x0500;//外围设备
    public final int IMAGING = 0x0600;
    public final int WEARABLE = 0x0700;//穿戴设备
    public final int TOY = 0x0800;//玩具
    public final int HEALTH = 0x0900;
    public final int UNCATEGORIZED = 0x1F00;

    private TextView txt_bluetooth_tip2,txt_bluetooth_device_name,txt_bluetooth_name;
    private Switch sw_bluetooth;
    private RelativeLayout bluetooth_device_name_layout,bluetooth_receive_file;
    private LinearLayout bluetooth_open_layout,bluetooth_scan_layout,bluetooth_help_layout;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothHeadset headset;
    private MyHandler handler;
    private BluetoothContectedAdapter contectedAdapter;
    private BluetoothUsingAdapter usingAdapter;
    private List<BluetoothBean> mContectedList = new ArrayList<>();
    private List<BluetoothBean> mUsingList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        handler = new MyHandler(mActivity);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.getProfileProxy(mContext, new BluetoothProfile.ServiceListener() {
            @Override
            public void onServiceConnected(int profile, BluetoothProfile proxy) {
                headset = (BluetoothHeadset) proxy;
            }

            @Override
            public void onServiceDisconnected(int profile) {
                if (profile == BluetoothProfile.HEADSET){
                    headset = null;
                }
            }
        },BluetoothProfile.HEADSET);
        initView();
        initBluetooth();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity, title_layout, "蓝牙", "", false);

        txt_bluetooth_tip2 = findViewById(R.id.txt_bluetooth_tip2);//蓝牙打开后的文字提示
        sw_bluetooth = findViewById(R.id.sw_bluetooth);//蓝牙开关按钮
        bluetooth_device_name_layout = findViewById(R.id.bluetooth_device_name_layout);//设备名称layout
        txt_bluetooth_device_name = findViewById(R.id.txt_bluetooth_device_name);//设备名称
        txt_bluetooth_name = findViewById(R.id.txt_bluetooth_name);//蓝牙名称
        bluetooth_receive_file = findViewById(R.id.bluetooth_receive_file);//接收的文件
        bluetooth_open_layout = findViewById(R.id.bluetooth_open_layout);//关闭蓝牙后的隐藏视图

        RecyclerView rv_bluetooth_contected = findViewById(R.id.rv_bluetooth_contected);//已连接设备列表
        contectedAdapter = new BluetoothContectedAdapter(mContectedList);
        Info.setRecycviewAdapter(mContext,rv_bluetooth_contected,contectedAdapter);
//        contectedAdapter.setOnItemClickListner(new BaseRecyclerAdapter.OnItemClickListner() {
//            @Override
//            public void onItemClickListner(View v, int position) {
//                //todo 先停止搜索
//                if (bluetoothAdapter.isDiscovering()){
//                    bluetoothAdapter.cancelDiscovery();
//                }
//                BluetoothDevice device = mContectedList.get(position).getDevice();
//                connectDevice(device);
//            }
//        });

        RecyclerView rv_bluetooth_using = findViewById(R.id.rv_bluetooth_using);//可用设备列表
        usingAdapter = new BluetoothUsingAdapter(mUsingList);
        Info.setRecycviewAdapter(mContext,rv_bluetooth_using,usingAdapter);
        //todo 匹配连接
        usingAdapter.setOnItemClickListner(new BaseRecyclerAdapter.OnItemClickListner() {
            @Override
            public void onItemClickListner(View v, int position) {
                try {
                    //todo 先停止搜索
                    if (bluetoothAdapter.isDiscovering()){
                        bluetoothAdapter.cancelDiscovery();
                    }
                    Method method = BluetoothDevice.class.getMethod("createBond");
                    method.invoke(mUsingList.get(position).getDevice());
                    getContectedList();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });

        bluetooth_scan_layout = findViewById(R.id.bluetooth_scan_layout);//扫描
        bluetooth_help_layout = findViewById(R.id.bluetooth_help_layout);//帮助

        txt_bluetooth_name.setText(bluetoothAdapter.getName());

        sw_bluetooth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    txt_bluetooth_tip2.setVisibility(View.VISIBLE);
                    bluetoothOpenState();
                }else {
                    txt_bluetooth_tip2.setVisibility(View.INVISIBLE);
                    bluetoothCloseState();
                }
            }
        });
        //重命名本机设备名称
        bluetooth_device_name_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new XPopup.Builder(mContext).asInputConfirm("重命名", "请输入设备名", new OnInputConfirmListener() {
                    @Override
                    public void onConfirm(String text) {
                        bluetoothAdapter.setName(text);
                        handler.sendEmptyMessage(UPDATELOCALDEVICENAME);
                    }
                }).show();
            }
        });
        //已接收的文件
        bluetooth_receive_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity,BluetoothReceivedFileListActivity.class));
            }
        });
        //扫描
        bluetooth_scan_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Info.showToast(mContext,"开始扫描",true);
                bluetoothAdapter.startDiscovery();
            }
        });
        //帮助
        bluetooth_help_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new XPopup.Builder(mContext).asCenterList("您想连接的蓝牙设备是？", new String[]{"车载", "耳机/音箱","穿戴设备","手机/平板、电脑"}, new OnSelectListener() {
                    @Override
                    public void onSelect(int position, String text) {
                        Info.showToast(mContext,"正在为您寻找"+text+"，请稍等",true);
                    }
                }).show();
            }
        });
    }

    private class MyHandler extends Handler {

        final WeakReference<Activity> mWeakReference;
        MyHandler(Activity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mWeakReference.get() != null) {
                if (msg.what == GETCONTECTEDLIST){//todo 获取已匹配列表
                    getContectedList();
                }else if (msg.what == GETCONTECTEDLISTSUCCESS){//todo 获取已匹配蓝牙列表成功
                    contectedAdapter.refresh(mContectedList);
                }else if (msg.what == UPDATESEARCHLIST) {//todo 获取可用蓝牙成功
                    usingAdapter.refresh(mUsingList);
                }else if (msg.what == UPDATELOCALDEVICENAME){//todo 更新本机设备名称
                    txt_bluetooth_name.setText(bluetoothAdapter.getName());
                }else if (msg.what == UPDATECONTECTEDDEVICENAME){//todo 更新已配对设备名称
                    getContectedList();
                }else if (msg.what == CONNECTFAIL){//todo 连接失败
                    Info.showToast(mContext,"连接失败，请重试",false);
                    Info.playRingtone(mContext,false);
                }
            }
        }

    }
    /**
     * 描述：初始化蓝牙
     * 作者：zzh
     */
    private void initBluetooth() {
        if (bluetoothAdapter.isEnabled()){
            sw_bluetooth.setChecked(true);
            bluetoothOpenState();
        }else {
            sw_bluetooth.setChecked(false);
            bluetoothCloseState();
        }
    }
    /**
     * 描述：开启蓝牙
     * 作者：zzh
     */
    private void openBluetooth(){
        bluetoothAdapter.enable();//打开蓝牙
        //设置默认可见时间
        if (bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);//默认是120秒
            discoverIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);//设置持续时间，一直开启。当我们需要设置具体可被发现的时间时，最多只能设置300秒。
            startActivity(discoverIntent);
        }
    }

    /**
     * 描述：蓝牙已打开状态
     * 作者：zzh
     */
    private void bluetoothOpenState(){
        txt_bluetooth_tip2.setVisibility(View.VISIBLE);
        bluetooth_device_name_layout.setClickable(true);
        txt_bluetooth_device_name.setTextColor(getResources().getColor(R.color.colorBlack2));
        bluetooth_open_layout.setVisibility(View.VISIBLE);
        openBluetooth();
        searchDevice();
        getContectedList();
        registerBluetoothBroadcast();
    }

    /**
     * 描述：蓝牙执行关闭动作
     * 作者：zzh
     */
    private void bluetoothCloseState(){
        bluetooth_open_layout.setVisibility(View.GONE);
        txt_bluetooth_tip2.setVisibility(View.INVISIBLE);
        bluetooth_device_name_layout.setClickable(false);
        txt_bluetooth_device_name.setTextColor(getResources().getColor(R.color.colorGray2));
        bluetooth_open_layout.setVisibility(View.GONE);
        mContectedList.clear();
        mUsingList.clear();
    }

    /**
     * 描述：获取已匹配列表
     * 作者：zzh
     */
    private void getContectedList(){
        if (mContectedList.size() != 0){
            mContectedList.clear();
        }
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice bluetoothDevice : devices){
            mContectedList.add(new BluetoothBean(bluetoothDevice.getName(),bluetoothDevice.getAddress(),bluetoothDevice.getBondState(),bluetoothDevice.getBluetoothClass().getMajorDeviceClass(),bluetoothDevice));
        }
        handler.sendEmptyMessage(GETCONTECTEDLIST);
    }

    /**
     * 描述：搜索设备
     * 作者：zzh
     */
    private void searchDevice(){
        //如果当前蓝牙正在搜索，则取消搜索，重新开始搜索
        if (bluetoothAdapter.isDiscovering()){
            bluetoothAdapter.cancelDiscovery();
        }
        bluetoothAdapter.startDiscovery();
    }

    /**
     * 描述：注册蓝牙广播
     * 作者：zzh
     */
    private void registerBluetoothBroadcast(){
        //设置意图过滤器，过滤广播信息
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//蓝牙状态改变的广播
        filter.addAction(BluetoothDevice.ACTION_FOUND);//找到设备的广播
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//搜索完成的广播
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);//开始扫描的广播
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);//设备状态改变的广播
        registerReceiver(receivers,filter);
    }

    /**
     * 描述：蓝牙连接
     * 作者：zzh
     * @param device 连接设备的信息
     */
    private void connectDevice(BluetoothDevice device) {
        try {
            Method method = headset.getClass().getDeclaredMethod("connect",BluetoothDevice.class);
            method.setAccessible(true);
            boolean isSuccess = (boolean) method.invoke(headset,device);
            if (isSuccess){
                Info.showToast(mContext,"连接成功",true);
                Info.playRingtone(mContext,true);
            }else {
                Info.showToast(mContext,"连接失败，请重试",false);
                Info.playRingtone(mContext,false);
            }

//            // 固定的UUID
//            final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
//            UUID uuid = UUID.fromString(SPP_UUID);
//            //创建socket
//            BluetoothSocket socket = device.createInsecureRfcommSocketToServiceRecord(uuid);
//            //启动连接线程
//            if (!socket.isConnected()){
//                socket.connect();
//            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 描述：获取搜索结果
     * 作者：zzh
     */
    private final BroadcastReceiver receivers = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)){//发现
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED){//未匹配过的
                    if (mUsingList.size() == 0) {
                        mUsingList.add(new BluetoothBean(device.getName(), device.getAddress(), device.getBondState(), device.getBluetoothClass().getMajorDeviceClass(), device));
                        handler.sendEmptyMessage(UPDATESEARCHLIST);
                    }else {
                        boolean isHave = false;
                        for (int i=0;i<mUsingList.size();i++){
                            if (device.getAddress().equals(mUsingList.get(i).getBluetoothAddress())){
                                isHave = true;
                            }
                        }
                        if (!isHave){
                            mUsingList.add(new BluetoothBean(device.getName(), device.getAddress(), device.getBondState(), device.getBluetoothClass().getMajorDeviceClass(), device));
                            handler.sendEmptyMessage(UPDATESEARCHLIST);
                        }
                    }
                    return;
                }
            }else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
                Toast.makeText(mContext,"开始搜索",Toast.LENGTH_SHORT).show();
//            }else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
//                Toast.makeText(mContext,"搜索完毕",Toast.LENGTH_SHORT).show();
//                bluetoothAdapter.cancelDiscovery();
            }
        }
    };

    private class BluetoothContectedAdapter extends BaseRecyclerAdapter<BluetoothBean>{

        BluetoothContectedAdapter(List<BluetoothBean> mDatas) {
            super(mDatas, R.layout.item_bluetooth);
        }

        @Override
        protected void bindData(BaseViewHolder holder, final int position, final BluetoothBean bluetoothBean) {
            ImageView devicetype = (ImageView) holder.getView(R.id.item_iv_bluetooth_devicetype);
            TextView devicename = (TextView) holder.getView(R.id.item_iv_bluetooth_devicename);
            ImageView devicesetting = (ImageView) holder.getView(R.id.item_iv_bluetooth_devicesetting);

            if (bluetoothBean.getBluetoothType() == AUDIO_VIDEO){
                devicetype.setImageResource(R.drawable.ic_headset);
            }else if (bluetoothBean.getBluetoothType() == PHONE){
                devicetype.setImageResource(R.drawable.ic_phone);
            }else if (bluetoothBean.getBluetoothType() == COMPUTER){
                devicetype.setImageResource(R.drawable.ic_computer);
            }else{
                devicetype.setImageResource(R.drawable.ic_icon_bluetooth);
            }
            devicename.setText(bluetoothBean.getBluetoonName());
            devicesetting.setVisibility(View.VISIBLE);

            devicename.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //todo 先停止搜索
                if (bluetoothAdapter.isDiscovering()){
                    bluetoothAdapter.cancelDiscovery();
                }
                BluetoothDevice device = bluetoothBean.getDevice();
                connectDevice(device);
                }
            });

            devicesetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new XPopup.Builder(mContext).asCenterList("蓝牙操作", new String[]{"重命名", "取消配对"}, new OnSelectListener() {
                        @Override
                        public void onSelect(int position, String text) {
                            if (text.equals("重命名")){
                                new XPopup.Builder(mContext).asInputConfirm("重命名", "请输入设备名", new OnInputConfirmListener() {
                                    @Override
                                    public void onConfirm(String text) {
                                        BluetoothDevice device = bluetoothBean.getDevice();
                                        try {
                                            Method method = device.getClass().getMethod("setAlias",String.class);
                                            if (method != null){
                                                method.invoke(device,text);
                                            }
                                        } catch (NoSuchMethodException e) {
                                            e.printStackTrace();
                                        } catch (IllegalAccessException e) {
                                            e.printStackTrace();
                                        } catch (InvocationTargetException e) {
                                            e.printStackTrace();
                                        }
                                        handler.sendEmptyMessage(UPDATECONTECTEDDEVICENAME);
                                    }
                                }).show();
                            }else if (text.equals("取消配对")){
                                try {
                                    Method method = bluetoothBean.getDevice().getClass().getMethod("removeBond",(Class[]) null);
                                    method.setAccessible(true);
                                    method.invoke(bluetoothBean.getDevice(),(Object[])null);
                                    handler.sendEmptyMessage(GETCONTECTEDLIST);
                                } catch (NoSuchMethodException e) {
                                    e.printStackTrace();
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                } catch (InvocationTargetException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).show();
                }
            });
        }
    }

    private class BluetoothUsingAdapter extends BaseRecyclerAdapter<BluetoothBean>{

        BluetoothUsingAdapter(List<BluetoothBean> mDatas) {
            super(mDatas, R.layout.item_bluetooth);
        }

        @Override
        protected void bindData(BaseViewHolder holder, int position, BluetoothBean bluetoothBean) {
            ImageView devicetype = (ImageView) holder.getView(R.id.item_iv_bluetooth_devicetype);
            TextView devicename = (TextView) holder.getView(R.id.item_iv_bluetooth_devicename);
            TextView deviceaddress = (TextView) holder.getView(R.id.item_iv_bluetooth_deviceaddress);

            if (bluetoothBean.getBluetoothType() == AUDIO_VIDEO){
                devicetype.setImageResource(R.drawable.ic_headset);
            }else if (bluetoothBean.getBluetoothType() == PHONE){
                devicetype.setImageResource(R.drawable.ic_phone);
            }else if (bluetoothBean.getBluetoothType() == COMPUTER){
                devicetype.setImageResource(R.drawable.ic_computer);
            }else{
                devicetype.setImageResource(R.drawable.ic_icon_bluetooth);
            }
            devicename.setText(bluetoothBean.getBluetoonName());
            deviceaddress.setText(bluetoothBean.getBluetoothAddress());
            deviceaddress.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receivers);
        handler.removeCallbacksAndMessages(null);
        bluetoothAdapter.cancelDiscovery();//停止搜索
        bluetoothAdapter.closeProfileProxy(BluetoothHeadset.HEADSET,headset);
        if (mContectedList != null && mContectedList.size() != 0){
            mContectedList.clear();
            mContectedList = null;
        }
        if (mUsingList != null && mUsingList.size() != 0){
            mUsingList.clear();
            mUsingList = null;
        }
    }
}
