package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.view.MovieSeatView;
import com.yks.simpledemo3.view.MyActionBar;

/**
 * 描述：电影选座
 * 作者：zzh
 * time:2019/10/29
 */
public class MovieSeatActivity extends Activity {

    private Context mContext = MovieSeatActivity.this;
    private Activity mActivity = MovieSeatActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_seat);

        initView();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity, title_layout, "电影选座", "", false);

        final MovieSeatView view_movie_seat = findViewById(R.id.view_movie_seat);
        view_movie_seat.setScreenName("屏幕中央");//todo 设置屏幕名称
        view_movie_seat.setMaxSelected(6);

        view_movie_seat.setSeatChecker(new MovieSeatView.SeatChecker() {
            @Override
            public boolean isValidSeat(int row, int column) {
                Log.d("movieseat","isValidSeat");
                if (column == 2){
                    return false;
                }
                return true;
            }

            @Override
            public boolean isSold(int row, int column) {
                Log.d("movieseat","isSold");
                if (row == 6 && column == 6){
                    return true;
                }
                return false;
            }

            @Override
            public void checked(int row, int column) {
                Log.d("movieseat","checked");
            }

            @Override
            public void unCheck(int row, int column) {
                Log.d("movieseat","uncheck");
            }

            @Override
            public String[] checkedSeatTxt(int row, int column) {
                return null;
            }
        });
        view_movie_seat.setData(10,15);//todo 设置行列

    }
}
