package com.yks.simpledemo3.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.tools.Info;
import com.yks.simpledemo3.view.MyActionBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import net.lemonsoft.lemonbubble.LemonBubble;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

import okhttp3.Call;

/**
 * 描述：城市定位，通过gps或网络获取经纬度，然后获取城市名称
 * 作者：zzh
 * time:2019/10/15
 */
public class CityLocationActivity extends Activity {

    private Activity mActivity = CityLocationActivity.this;
    private Context mContext = CityLocationActivity.this;

    private final int GETLONGITUDESUCCESS = 0;//获取经纬度成功
    private final int GETERROR = 1;//获取失败
    private final int GETCITIINFOSUCCESS = 2;//获取城市信息成功
    private final int GETIPADDRESSSUCCESS = 3;//获取ip地址成功
    private MyHandler handler;

    private LocationManager locationManager;
    private double longitude = 0.0;//经度
    private double latitude = 0.0;//纬度
    private String ipAddress = "";
    private String formatted_address = "", business = "", city = "", direction = "", distance = "", district = "", province = "", street = "", street_number = "", cityCode = "";

    private TextView txt_longitude, txt_latitude, txt_formatted_address, txt_business, txt_province, txt_city, txt_district, txt_street, txt_street_number, txt_direction, txt_distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_location);

        handler = new MyHandler(mActivity);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        initView();
        getLatitudeAndLongitude();
//
//        longitude = 22.6965448590;
//        latitude = 114.1144163930;
//        getCityLocationInfo();

        getIPAddress();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity, title_layout, "城市定位", "", false);

        txt_longitude = findViewById(R.id.txt_longitude);
        txt_latitude = findViewById(R.id.txt_latitude);
        txt_formatted_address = findViewById(R.id.txt_formatted_address);
        txt_business = findViewById(R.id.txt_business);
        txt_province = findViewById(R.id.txt_province);
        txt_city = findViewById(R.id.txt_city);
        txt_district = findViewById(R.id.txt_district);
        txt_street = findViewById(R.id.txt_street);
        txt_street_number = findViewById(R.id.txt_street_number);
        txt_direction = findViewById(R.id.txt_direction);
        txt_distance = findViewById(R.id.txt_distance);
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
                if (msg.what == GETLONGITUDESUCCESS) {//todo 获取经纬度成功
                    getCityLocationInfo();
                } else if (msg.what == GETCITIINFOSUCCESS) {//todo 获取城市信息成功
                    txt_longitude.setText(longitude + "");
                    txt_latitude.setText(latitude + "");
                    txt_formatted_address.setText(formatted_address);
                    txt_business.setText(business);
                    txt_province.setText(province);
                    txt_city.setText(city);
                    txt_district.setText(district);
                    txt_street.setText(street);
                    txt_street_number.setText(street_number);
                    txt_direction.setText(direction);
                    txt_distance.setText(distance);

                    Info.playRingtone(mContext, true);
                } else if (msg.what == GETERROR) {//todo 失败
                    Bundle bundle = msg.getData();
                    Info.showToast(mContext, bundle.getString("msg"), false);
                    Info.playRingtone(mContext, false);
                } else if (msg.what == GETIPADDRESSSUCCESS){
                    getIpCityInfo();
                }
            }
        }
    }

    /**
     * 描述：获取经纬度
     * 作者：zzh
     */
    private void getLatitudeAndLongitude() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);
        String locationProvider;
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            Info.sendMessage(handler,GETERROR,"没有可用的位置提供器");
            return;
        }
        //获取Location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //监视地理位置变化
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 1, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 1, locationListener);
        Location location = locationManager.getLastKnownLocation(locationProvider);
        if (location != null) {
            //不为空,显示地理位置经纬度
            showLocation(location);
        }else {
            Info.sendMessage(handler,GETERROR,"位置为空,请到室外使用");
        }
    }

    /**
     * LocationListern监听器
     * 参数：地理位置提供器、监听位置变化的时间间隔、位置变化的距离间隔、LocationListener监听器
     */

    private final LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {
            Log.d("mylocation1",provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d("mylocation2",provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d("mylocation3",provider);
            showLocation(null);
        }

        @Override
        public void onLocationChanged(Location location) {
            //如果位置发生变化,重新显示
            if (location != null){
                showLocation(location);
            }else {
                Info.sendMessage(handler,GETERROR,"位置为空");
            }
            assert location != null;
            Log.d("mylocation",location.getLongitude()+","+location.getLatitude());
        }
    };

    private void showLocation(Location location) {
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        handler.sendEmptyMessage(GETLONGITUDESUCCESS);
    }

    /**
     * 描述：获取手机ip地址
     * 作者：zzh
     * https://blog.csdn.net/it_fighter/article/details/81777991
     * https://ip-api.com/docs/api:json
     * https://www.jianshu.com/p/d98519faf7b6
     */
    private void getIPAddress() {
        NetworkInfo info = ((ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {    // 当前使用2G/3G/4G网络
                try {
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                ipAddress = inetAddress.getHostAddress();
                                handler.sendEmptyMessage(GETIPADDRESSSUCCESS);
                                return;
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {    // 当前使用无线网络
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                ipAddress = intIP2StringIP(wifiInfo.getIpAddress());    // 得到IPV4地址
                handler.sendEmptyMessage(GETIPADDRESSSUCCESS);
            }
        } else {
            // 当前无网络连接,请在设置中打开网络
            Info.sendMessage(handler,GETERROR,"当前无网络连接,请在设置中打开网络");
        }
    }
    /**
     * 描述：将得到的int类型的IP转换为String类型
     * 作者：zzh
     * @param ip 获取的ip
     * @return 转换为字符串的ip地址
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    /**
     * 描述：获取城市信息成功，调用百度地图接口
     * 作者：zzh
     */
    private void getCityLocationInfo(){
        String url = "http://api.map.baidu.com/geocoder?output=json&location="+longitude+","+latitude+"&ak=esNPFDwwsXWtsQfw4NMNmur1";

        OkHttpUtils.get().url(url)
                .tag(this)
                .build()
                .connTimeOut(10000L)
                .readTimeOut(10000L)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Info.sendMessage(handler,GETERROR,"获取位置失败，请重试1"+e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject object = new JSONObject(response);
                            String status = object.getString("status");
                            if (status.equals("OK")){
                                JSONObject object1 = object.getJSONObject("result");
                                formatted_address = object1.getString("formatted_address");
                                business = object1.getString("business");
                                cityCode = object1.getString("cityCode");
                                JSONObject object2 = object1.getJSONObject("addressComponent");
                                city = object2.getString("city");
                                direction = object2.getString("direction");
                                distance = object2.getString("distance");
                                district = object2.getString("district");
                                province = object2.getString("province");
                                street = object2.getString("street");
                                street_number = object2.getString("street_number");
                                handler.sendEmptyMessage(GETCITIINFOSUCCESS);
                            }else {
                                Info.sendMessage(handler,GETERROR,"获取失败");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Info.sendMessage(handler,GETERROR,"获取位置失败，请重试3"+e);
                        }
                    }
                });
    }

    /**
     * 描述：获取ip地址对应的城市信息
     * 作者：zzh
     */
    private void getIpCityInfo(){
        OkHttpUtils.get().url("http://ip-api.com/json/"+ipAddress)
                .tag(this)
                .build()
                .connTimeOut(10000L)
                .readTimeOut(10000L)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Info.sendMessage(handler,GETERROR,"获取失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject object = new JSONObject(response);
                            String status = object.getString("status");
                            if (status.equals("fail")){
                                Info.sendMessage(handler,GETERROR,object.getString("message"));
                                return;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LemonBubble.forceHide();
        handler.removeCallbacksAndMessages(null);
        OkHttpUtils.getInstance().cancelTag(this);
        if (locationManager != null){
            locationManager.removeUpdates(locationListener);
        }
    }
}
