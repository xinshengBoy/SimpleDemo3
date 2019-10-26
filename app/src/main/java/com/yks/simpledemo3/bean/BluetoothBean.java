package com.yks.simpledemo3.bean;

import android.bluetooth.BluetoothDevice;

/**
 * 描述：蓝牙bean
 * 作者：zzh
 * time:2019/10/23
 */
public class BluetoothBean {
    private String bluetoonName;//设备名称
    private String bluetoothAddress;//设备地址
    private int bluetoothBondState;//是否已匹配
    private int bluetoothType;//设备类型
    private BluetoothDevice device;//设备对象

    public BluetoothBean(String bluetoonName, String bluetoothAddress, int bluetoothBondState, int bluetoothType, BluetoothDevice device) {
        this.bluetoonName = bluetoonName;
        this.bluetoothAddress = bluetoothAddress;
        this.bluetoothBondState = bluetoothBondState;
        this.bluetoothType = bluetoothType;
        this.device = device;
    }

    public String getBluetoonName() {
        return bluetoonName;
    }

    public String getBluetoothAddress() {
        return bluetoothAddress;
    }

    public int getBluetoothBondState() {
        return bluetoothBondState;
    }

    public int getBluetoothType() {
        return bluetoothType;
    }

    public BluetoothDevice getDevice() {
        return device;
    }
}
