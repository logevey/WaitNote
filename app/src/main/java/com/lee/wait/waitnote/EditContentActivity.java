package com.lee.wait.waitnote;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Vibrator;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lee.wait.activity.swipeback.lib.SwipeBackLayout;
import com.lee.wait.activity.swipeback.SwipeBackActivity;
import com.lee.wait.database.NoteContent;
import com.lee.wait.database.NoteDatabaseService;

public class EditContentActivity extends SwipeBackActivity {

    private static final String TAG = "EditContentActivity";
    private static final int VIBRATE_DURATION = 20;
    private SwipeBackLayout mSwipeBackLayout;
    private EditText etContent;
    private LinearLayout llEditTool;
    private NoteContent noteContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_content);
        noteContent = (NoteContent)getIntent().getSerializableExtra("noteContent");

        final Toolbar toolbar = (Toolbar) findViewById(R.id.tbEditContent);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(noteContent.getCategory());


        llEditTool = (LinearLayout) findViewById(R.id.llEditTool);

        etContent = (EditText) findViewById(R.id.etContent);
        etContent.setText(noteContent.getContent());
        etContent.setSelection(etContent.length());
        etContent.setFocusableInTouchMode(false);
        etContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (llEditTool.getVisibility() == View.VISIBLE) {
                    llEditTool.setVisibility(View.GONE);
                } else {
                    llEditTool.setVisibility(View.VISIBLE);
                }
                etContent.setFocusableInTouchMode(true);
                etContent.requestFocus();
            }
        });

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
    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            NoteDatabaseService noteDatabase = new NoteDatabaseService(this);
            if(etContent.getText().toString().equals("")){
                if(noteContent.getId()==0){

                }else{
                    noteDatabase.delete(noteContent.getId());
                }
            }else{
                if(noteContent.getId()==0){
                    noteContent.setContent(etContent.getText().toString());
                    noteContent.setTime(MainActivity.getCurrentTimeStr());
                    noteDatabase.insert(noteContent);
                }else{
                    noteContent.setContent(etContent.getText().toString());
                    noteDatabase.update(noteContent);
                }
            }

        }
        return super.onKeyDown(keyCode,event);
    }
    private void vibrate(long duration) {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {
                0, duration
        };
        vibrator.vibrate(pattern, -1);
    }
}
