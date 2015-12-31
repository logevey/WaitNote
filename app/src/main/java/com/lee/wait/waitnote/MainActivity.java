package com.lee.wait.waitnote;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.lee.wait.database.NoteContent;
import com.lee.wait.database.NoteDatabaseService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //    private SwipeRefreshLayout swipeView;
    private static final String TAG = "MainActivity";
    private ListView lvLeftMenu;
    private Cursor cursor;
    private NoteDatabaseService noteDatabaseService;
    private SimpleCursorAdapter simpleCursorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //标题栏
        final Toolbar toolbar = (Toolbar) findViewById(R.id.tb_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("所有");
        //滑动抽屉
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_main);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this , drawerLayout , toolbar,R.string.drawer_open,R.string.drawer_close);
        drawerToggle.syncState();
        drawerLayout.setDrawerListener(drawerToggle);

        lvLeftMenu = (ListView) findViewById(R.id.lv_left_menu);
        noteDatabaseService = new NoteDatabaseService(this);
        cursor = noteDatabaseService.getRawScrollData(0, noteDatabaseService.getCount());
        if (cursor != null) {
            simpleCursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, new String[]{"category"}, new int[]{android.R.id.text1}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            lvLeftMenu.setAdapter(simpleCursorAdapter);
            lvLeftMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    simpleCursorAdapter.
                    toolbar.setTitle(cursor.getString(1));
                    drawerLayout.closeDrawers();
                }
            });
        }

//        NavigationView naiView = (NavigationView) findViewById(R.id.navigation_view);
//        naiView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(MenuItem menuItem) {
//                switch (menuItem.getItemId()){
//                    case R.id.item_one:
////                        getSupportFragmentManager() .beginTransaction().replace((R.id.))
//                        toolbar.setTitle(menuItem.getTitle());
//                        break;
//                    case R.id.item_two:
////                        getSupportFragmentManager() .beginTransaction().replace((R.id.))
//                        toolbar.setTitle(menuItem.getTitle());
//                        break;
//                    case R.id.item_three:
////                        getSupportFragmentManager() .beginTransaction().replace((R.id.))
//                        toolbar.setTitle(menuItem.getTitle());
//                        break;
//                }
//                menuItem.setChecked(true);
//                drawerLayout.closeDrawers();
//                return false;
//            }
//        });


        //浮动按键
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_main);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "敬请期待", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent editContentIntent = new Intent();
                editContentIntent.setClass(view.getContext(), EditContentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("noteContent", new NoteContent());
                editContentIntent.putExtras(bundle);
                startActivity(editContentIntent);
            }
        });
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
    public static  String getCurrentTimeStr() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String strTime = formatter.format(curDate);
        return strTime;
    }
}
