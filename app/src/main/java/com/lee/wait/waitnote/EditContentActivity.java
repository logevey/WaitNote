package com.lee.wait.waitnote;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.os.Bundle;
import android.widget.TextView;

import com.lee.wait.activity.swipeback.lib.SwipeBackLayout;
import com.lee.wait.activity.swipeback.SwipeBackActivity;

public class EditContentActivity extends SwipeBackActivity {

    private static final int VIBRATE_DURATION = 20;
    private SwipeBackLayout mSwipeBackLayout;
    private String mKeyTrackingMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_content);
        Intent intent = getIntent();
        String strContent = intent.getStringExtra("str_content");
        TextView tvContent = (TextView)findViewById(R.id.tvContent);
        tvContent.setText(strContent);

        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.addSwipeListener(new SwipeBackLayout.SwipeListener() {
            @Override
            public void onScrollStateChange(int state, float scrollPercent) {

            }
            @Override
            public void onEdgeTouch(int edgeFlag) {
                vibrate(VIBRATE_DURATION);
            }

            @Override
            public void onScrollOverThreshold() {
                vibrate(VIBRATE_DURATION);
            }
        });
    }
    private void vibrate(long duration) {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {
                0, duration
        };
        vibrator.vibrate(pattern, -1);
    }
}
