package com.nikitayankov.grappstriangle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    TriangleView mTriangleView;

    TextView mLastTriangleArea;
    TextView mNextAreaUpdate;

    volatile String countArea;
    volatile String countTime;

    Timer timer = new Timer();
    TimerTask mTimerTask = new TimerTask() {
        int timePassed = 0;
        int waitTime = 5;

        @Override
        public void run() {
            timePassed += 1;
            countTime = countTime();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mNextAreaUpdate.setText(countTime);
                }
            });

            if (timePassed == waitTime) {
                timePassed = 0;
                countArea = countArea();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mLastTriangleArea.setText(countArea);
                    }
                });
            }
        }

        private String countArea() {
            String area = String.valueOf(mTriangleView.calculateArea());

            return getString(R.string.last_triangle_area, area);
        }

        private String countTime() {
            String time = String.valueOf(waitTime - timePassed);

            return getString(R.string.next_area_update_in, time);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTriangleView = (TriangleView)findViewById(R.id.main_triangle_view);
        mTriangleView.setTriangle(new Triangle());

        mLastTriangleArea = (TextView)findViewById(R.id.main_last_triangle_area);
        mNextAreaUpdate = (TextView)findViewById(R.id.main_next_area_update);

        mLastTriangleArea.setText(getString(R.string.last_triangle_area, String.valueOf(mTriangleView.calculateArea())));

        startTimer();
    }

    void startTimer() {
        timer.scheduleAtFixedRate(mTimerTask, 0, 1000);
    }
}
