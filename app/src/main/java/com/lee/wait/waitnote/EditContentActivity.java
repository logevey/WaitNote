package com.lee.wait.waitnote;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Vibrator;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.lee.wait.activity.swipeback.lib.SwipeBackLayout;
import com.lee.wait.activity.swipeback.SwipeBackActivity;
import com.lee.wait.database.NoteContent;
import com.lee.wait.database.NoteDatabaseService;

public class EditContentActivity extends SwipeBackActivity implements View.OnClickListener {

    private static final String TAG = "EditContentActivity";
    private static final int VIBRATE_DURATION = 20;
    private SwipeBackLayout mSwipeBackLayout;
    private EditText etContent;
    private LinearLayout llEditTool;
    private NoteContent noteContent;
    private TextView tvTitle;
    private Cursor cursor;
    private NoteDatabaseService noteDatabaseService;
    private SimpleCursorAdapter simpleCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_content);
        noteContent = (NoteContent) getIntent().getSerializableExtra("noteContent");

        final Toolbar toolbar = (Toolbar) findViewById(R.id.tb_edit_content);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        tvTitle = (TextView) findViewById(R.id.tv_edit_content_title);
        tvTitle.setText(noteContent.getCategory());
        tvTitle.setOnClickListener(this);

//        llEditTool = (LinearLayout) findViewById(R.id.ll_edit_tool);

        etContent = (EditText) findViewById(R.id.et_content);
        etContent.setText(noteContent.getContent());
        etContent.setSelection(etContent.length());
        etContent.setFocusableInTouchMode(false);
        etContent.setOnClickListener(this);

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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
           saveChangeContent();
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                saveChangeContent();
                finish();
                break;
            default:
                break;
        }
        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    private void vibrate(long duration) {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {
                0, duration
        };
        vibrator.vibrate(pattern, -1);
    }

    private void saveChangeContent() {
        NoteDatabaseService noteDatabaseService = new NoteDatabaseService(this);
        if (etContent.getText().toString().equals("")) {
            if (noteContent.getId() == 0) {

            } else {
                noteDatabaseService.delete(noteContent.getId());
            }
        } else {
            if (noteContent.getId() == 0) {
                noteContent.setCategory(tvTitle.getText().toString());
                noteContent.setContent(etContent.getText().toString());
                noteContent.setTime(MainActivity.getCurrentTimeStr());
                noteDatabaseService.insert(noteContent);
            } else {
                noteContent.setCategory(tvTitle.getText().toString());
                noteContent.setContent(etContent.getText().toString());
                noteDatabaseService.update(noteContent);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_edit_content_title:
                Log.e(TAG, "textview");
                final AlertDialog.Builder builder = new AlertDialog.Builder(EditContentActivity.this);
                builder.setIcon(R.mipmap.ic_launcher);
                //    通过LayoutInflater来加载一个xml的布局文件作为一个View对象
                View view = LayoutInflater.from(EditContentActivity.this).inflate(R.layout.dialog_choose_category, null);
                //    设置我们自己定义的布局文件作为弹出框的Content
                builder.setView(view);

                final ListView lvCategory = (ListView) view.findViewById(R.id.lv_category);

                noteDatabaseService = new NoteDatabaseService(this);
                cursor = noteDatabaseService.getRawScrollData("where content = '' order by time");
                simpleCursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, new String[]{"category"}, new int[]{android.R.id.text1}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

                lvCategory.setAdapter(simpleCursorAdapter);
                final Dialog dialog = builder.show();
                lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        tvTitle.setText(cursor.getString(1));
                        dialog.dismiss();
                    }
                });


                break;
            case R.id.et_content:
//                if (llEditTool.getVisibility() == View.VISIBLE) {
//                    llEditTool.setVisibility(View.GONE);
//                } else {
//                    llEditTool.setVisibility(View.VISIBLE);
//                }
                etContent.setFocusableInTouchMode(true);
                etContent.requestFocus();
                break;

        }
    }
}
