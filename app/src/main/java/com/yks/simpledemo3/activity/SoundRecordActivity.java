package com.yks.simpledemo3.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haozhang.lib.AnimatedRecordingView;
import com.yks.simpledemo3.R;
import com.yks.simpledemo3.adapter.BaseRecyclerAdapter;
import com.yks.simpledemo3.bean.ArmBean;
import com.yks.simpledemo3.tools.FileUtils;
import com.yks.simpledemo3.tools.Info;
import com.yks.simpledemo3.tools.TimeUtils;
import com.yks.simpledemo3.view.MyActionBar;

import net.lemonsoft.lemonbubble.LemonBubble;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import kr.co.namee.permissiongen.PermissionGen;

/**
 * 描述：录音及动画
 * 作者：zzh
 * time:2019/11/15
 * https://github.com/HeZaiJin/AnimatedRecordingView/blob/master/android-animated-recording-view/src/main/java/com/haozhang/lib/AnimatedRecordingView.java  动画
 * https://www.jianshu.com/p/de779d509e6c  录音
 */
public class SoundRecordActivity extends Activity implements SensorEventListener {
    private static final String TAG = "AudioSensorBinder";
    private Context mContext = SoundRecordActivity.this;
    private Activity mActivity = SoundRecordActivity.this;
    private final int GETRECORDINGFILE = 0;//扫描文件管理，获取录音文件信息
    private final int GETRECORDINGFAIL = 1;//获取失败
    private final int GETRECORDINGSUCCESS = 2;//获取成功
    private final int GETRECORDVOLUME = 3;//获取音量
    private final int PLAYRECORD = 4;//播放录音

    private AudioManager audioManager;
    private PowerManager powerManager;
    private SensorManager sensorManager;
    private Sensor sensor;
    private PowerManager.WakeLock wakeLock;

    private AnimatedRecordingView view_animate;
    private TextView txt_tip_nodata;
    private RecyclerView cv_recording_file;
    private MyHandler handler;
    private MediaRecorder recorder;
    private MediaPlayer player;
    private List<ArmBean> mList = new ArrayList<>();
    private SoundRecordAdapter adapter;
    private String filePath = "";
    private File path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_recording);

        handler = new MyHandler(mActivity);
        initView();
        handler.sendEmptyMessage(GETRECORDINGFILE);
        player = new MediaPlayer();

        path = Environment.getExternalStorageDirectory();
        audioManager = (AudioManager) mActivity.getSystemService(Context.AUDIO_SERVICE);
        powerManager = (PowerManager) mActivity.getSystemService(POWER_SERVICE);
        registerProximitSensorListener();//注册传感器
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity, title_layout, "录音及动画", "", false);

        cv_recording_file = findViewById(R.id.cv_recording_file);
        adapter = new SoundRecordAdapter(mList);
        Info.setRecycviewAdapter(mContext,cv_recording_file,adapter);
        adapter.setOnItemClickListner(new BaseRecyclerAdapter.OnItemClickListner() {
            @Override
            public void onItemClickListner(View v, int position) {
                filePath = mList.get(position).getPath();
                playRecord();
            }
        });

        txt_tip_nodata = findViewById(R.id.txt_tip_nodata);
        view_animate = findViewById(R.id.view_animate);
        view_animate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.view_animate){
                    switch (event.getAction()){
                        case MotionEvent.ACTION_UP://todo 抬起，停止录音
                            view_animate.stop();
                            stopRecord();
                            break;
                        case MotionEvent.ACTION_DOWN://todo 按下，开始录音
                            view_animate.start();
                            Random random = new Random(100);
                            view_animate.setVolume(random.nextInt());
                            view_animate.loading();
                            startRecord();
                            break;
                    }
                }
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        PermissionGen.needPermission(mActivity,100,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO});
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
                if (msg.what == GETRECORDINGFILE){//扫描文件
//                    getRecordingFile();
                    new MyTask().execute();
//                    findRecordFile(path);
                }else if (msg.what == GETRECORDVOLUME){//获取音量
                    view_animate.setVolume(recorder.getMaxAmplitude());
                }else if (msg.what == PLAYRECORD){//播放录音
                    playRecord();
                }else if (msg.what == GETRECORDINGSUCCESS){//获取文件成功
                    LemonBubble.hide();
                    adapter.refresh(mList);
                    cv_recording_file.setVisibility(View.VISIBLE);
                    txt_tip_nodata.setVisibility(View.GONE);
                    Info.playRingtone(mContext, true);
                }else if (msg.what == GETRECORDINGFAIL){//遍历文件失败
                    LemonBubble.hide();
                    txt_tip_nodata.setVisibility(View.VISIBLE);
                    cv_recording_file.setVisibility(View.GONE);
                    mList.clear();
                    adapter.refresh(mList);
                }
            }
        }
    }
    private void registerProximitSensorListener(){
        sensorManager = (SensorManager) mActivity.getSystemService(SENSOR_SERVICE);
        if (sensorManager == null){
            return;
        }
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (audioManager == null){
            return;
        }

        if (isHeadphonesPlugged()){//todo 耳机已插入，传感器失效
            return;
        }

        if (player.isPlaying()){
            //todo 如果音频正在播放
            float distance = event.values[0];//已播放到的位置距离
            if (distance >= sensor.getMaximumRange()){//todo 距离远离听筒，音频外放，亮屏
                changeToSpeaker();
            }else {//todo 靠近听筒
                playRecord();
                changeToReceiver();
                audioManager.setSpeakerphoneOn(false);//内放
            }
        }else {
            //todo 已经播放完毕
            changeToSpeaker();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * 描述：判断是否插入耳机
     * 作者：zzh
     * @return 是否插入耳机
     */
    private boolean isHeadphonesPlugged(){
        if (audioManager == null){
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            AudioDeviceInfo[] audioDevice = audioManager.getDevices(AudioManager.GET_DEVICES_ALL);
            for (AudioDeviceInfo info : audioDevice){
                if (info.getType() == AudioDeviceInfo.TYPE_WIRED_HEADPHONES || info.getType() == AudioDeviceInfo.TYPE_WIRED_HEADSET){
                    return true;
                }
            }
            return false;
        }else {
            return audioManager.isWiredHeadsetOn();
        }
    }

    /**
     * 描述：切换到外放
     * 作者：zzh
     */
    private void changeToSpeaker(){
        setScreenOn();//亮屏
        if (audioManager == null){
            return;
        }
        audioManager.setMode(AudioManager.MODE_NORMAL);//切换成普通模式
        audioManager.setSpeakerphoneOn(true);//音量外放
    }

    /**
     * 描述：切换成耳机
     * 作者：zzh
     */
    private void changeToHeadset(){
        setScreenOff();
        if (audioManager == null){
            return;
        }
        audioManager.setSpeakerphoneOn(false);//听筒播放
    }

    /**
     * 描述：切换到听筒
     * 作者：zzh
     */
    private void changeToReceiver(){
        setScreenOff();
        if (audioManager == null){
            return;
        }
        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        audioManager.setSpeakerphoneOn(false);
    }
    /**
     * 描述：亮屏
     * 作者：zzh
     */
    private void setScreenOn(){
        if (wakeLock != null){
            wakeLock.setReferenceCounted(false);
            wakeLock.release();
            wakeLock = null;
        }
    }

    @SuppressLint("InvalidWakeLockTag")
    private void setScreenOff(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            if (wakeLock == null){
                wakeLock = powerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK,TAG);
            }
            wakeLock.acquire(10*60*1000L);
        }
    }
    /**
     * 描述：获取录音文件
     * 作者：zzh
     */
    private void getRecordingFile(){
        if (mList.size() != 0){
            mList.clear();
        }

        String[] projection = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA
        };

        ContentResolver resolver = mContext.getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Audio.Media.DATE_ADDED);
        if (cursor == null){
            handler.sendEmptyMessage(GETRECORDINGFAIL);
            return;
        }
        if (cursor.moveToFirst()){
            int ids = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int names = cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);
            int sizes = cursor.getColumnIndex(MediaStore.Audio.Media.SIZE);
            int date = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);//DATE_ADDED
            int path = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            do {
                if (cursor.getString(names).contains(".amr")) {
                    ArmBean bean = new ArmBean(cursor.getString(ids), cursor.getString(names), FileUtils.FormetFileSize(Long.parseLong(cursor.getString(sizes))), cursor.getString(date), cursor.getString(path));//TimeUtils.getDateToString(Long.parseLong(cursor.getString(date)))
                    mList.add(bean);
                }
            }while (cursor.moveToNext());

            if (mList.size() == 0){
                handler.sendEmptyMessage(GETRECORDINGFAIL);
            }else {
                handler.sendEmptyMessage(GETRECORDINGSUCCESS);
            }
        }
        cursor.close();
    }

    /**
     * 描述：遍历SD卡文件
     * 作者：zzh
     * @param file 根目录文件
     */
    private void findRecordFile(final File file){
        if (file != null && file.exists() && file.isDirectory()){
            File[] files = file.listFiles();
            for (File file2 : files){
                if (file2.listFiles() == null && file2.getName().contains(".amr")){
                    ArmBean bean = new ArmBean("",file2.getName(),FileUtils.FormetFileSize(file2.length()), TimeUtils.getDateToString(file2.lastModified()),file2.getAbsolutePath());
                    mList.add(bean);
                }else {
                    findRecordFile(file2);
                }
            }
        }
    }

    private class MyTask extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... voids) {
            findRecordFile(path);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Info.showProgress(mContext,"查找中...");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (mList.size() == 0){
                handler.sendEmptyMessage(GETRECORDINGFAIL);
            }else {
                Collections.reverse(mList);
                handler.sendEmptyMessage(GETRECORDINGSUCCESS);
            }
        }
    }

    private class SoundRecordAdapter extends BaseRecyclerAdapter<ArmBean> {

        SoundRecordAdapter(List<ArmBean> mDatas) {
            super(mDatas, R.layout.item_sound_record);
        }

        @Override
        protected void bindData(BaseViewHolder helper, int position, ArmBean item) {
            TextView filename = (TextView) helper.getView(R.id.txt_item_record_filename);
            TextView filesize = (TextView) helper.getView(R.id.txt_item_record_filesize);
            TextView filedate = (TextView) helper.getView(R.id.txt_item_record_filedate);

            filename.setText(item.getName());
            filesize.setText(item.getSize());
            filedate.setText(item.getDate());
        }
    }

    /**
     * 描述：开始录音
     * 作者：zzh
     */
    private void startRecord(){
        if (recorder == null){
            recorder = new MediaRecorder();

            try {
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);//设置麦克风
                recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);//设置输出文件格式
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);//设置御品文件编码
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String fileName = "record"+format.format(new Date())+ ".amr";
                String saveDir = Environment.getExternalStorageDirectory().getAbsolutePath()+"/yks/";
                File file = new File(saveDir);

                if (!file.exists()){
                    if (!file.mkdirs()){
                        file.mkdirs();
                    }
                }

                filePath = saveDir + fileName;
                recorder.setOutputFile(filePath);
                recorder.prepare();
                recorder.start();
                handler.sendEmptyMessage(GETRECORDVOLUME);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 描述：停止录音
     * 作者：zzh
     */
    private void stopRecord(){
        try{
            recorder.stop();
            recorder.release();
            recorder = null;
            handler.sendEmptyMessageDelayed(GETRECORDINGFILE,200);
            handler.sendEmptyMessageDelayed(PLAYRECORD,500);
        }catch (RuntimeException e){
            recorder.reset();
            recorder.release();
            recorder = null;
        }
    }

    /**
     * 描述：播放录音
     * 作者：zzh
     */
    private void playRecord(){
        if (player != null){
            if (player.isPlaying()){
                player.stop();
            }
            try {//todo 顺序一定不能乱，不然会闪退
                player.reset();
                player.setDataSource(filePath);
                player.prepare();
                player.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        if (recorder != null){
            recorder.stop();
            recorder.release();
            recorder = null;
        }
        if (mList != null && mList.size() != 0){
            mList.clear();
            mList = null;
        }

        sensorManager.unregisterListener(this);
        sensorManager = null;
        wakeLock = null;
    }
}
