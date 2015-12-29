package com.lee.wait.waitnote;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private String[] iconName = {"通讯录", "日历", "照相机", "时钟", "游戏", "短信", "铃声","通讯录", "日历", "照相机", "时钟", "游戏", "短信", "铃声","通讯录", "日历", "照相机", "时钟", "游戏", "短信", "铃声","通讯录", "日历", "照相机", "时钟", "游戏", "短信", "铃声","通讯录", "日历", "照相机", "时钟", "游戏", "短信", "铃声","通讯录", "日历", "照相机", "时钟", "游戏", "短信", "铃声"};
    private SimpleAdapter sim_adapter;
    private GridView gview;
    private List<Map<String, Object>> data_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Hello,Wait");


        gview = (GridView) findViewById(R.id.gridView);
        //新建List
        data_list = new ArrayList<Map<String, Object>>();
        //获取数据
        getData();
        //新建适配器
        String[] from = {"text"};
        int[] to = {R.id.text};
        sim_adapter = new SimpleAdapter(this, data_list, R.layout.content_item, from, to);
        //配置适配器

        gview.setAdapter(sim_adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "敬请期待", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public List<Map<String, Object>> getData() {
        for (int i = 0; i < iconName.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("text", iconName[i]);
            data_list.add(map);
        }
        return data_list;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
