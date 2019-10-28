package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.view.LrcView;
import com.yks.simpledemo3.view.MyActionBar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 描述：歌词同步
 * 作者：zzh
 * time:2019/10/26
 * 参考：https://github.com/lenve/LrcView/tree/master/lrcview/src/main/java/org/sang/lrcview
 */
public class MusicLrcActivity extends Activity {

    private Activity mActivity = MusicLrcActivity.this;

    private LrcView view_music_lrc;
    private MediaPlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_lrc);

        initView();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity, title_layout, "歌词同步", "", false);

        view_music_lrc = findViewById(R.id.view_music_lrc);
        //todo 开启线程读取lrc的文件
        new ReadLrcTask().execute();

    }

    private class ReadLrcTask extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... voids) {
            //todo 读取lrc文件
            AssetManager manager = getResources().getAssets();
            try {
                InputStream inputStream = manager.open("xiaopingguo.lrc");
                InputStreamReader reader = new InputStreamReader(inputStream,"UTF-8");
                BufferedReader br = new BufferedReader(reader);
                StringBuilder sb = new StringBuilder();
                String length;
                while ((length = br.readLine()) != null){
                    sb.append(length + "\n");
                }
                br.close();
                reader.close();
                inputStream.close();
                return sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                //todo 同步播放此歌曲
                AssetFileDescriptor fd = getAssets().openFd("xiaopingguo.mp3");
                player = new MediaPlayer();
                player.setDataSource(fd.getFileDescriptor(),fd.getStartOffset(),fd.getLength());
                player.setLooping(true);
                player.prepare();
                player.start();
                view_music_lrc.setLrc(s);
                view_music_lrc.setPlayer(player);
                view_music_lrc.init();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null && player.isPlaying()){
            player.stop();
            player.release();
            player = null;
        }
    }
}
