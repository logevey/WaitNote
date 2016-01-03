package com.lee.wait.waitnote;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.lee.wait.adapter.ListViewMenuAdapter;
import com.lee.wait.adapter.ListViewMenuItem;
import com.lee.wait.database.NoteContent;
import com.lee.wait.database.NoteDatabaseService;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //    private SwipeRefreshLayout swipeView;
    private static final String TAG = "MainActivity";
    private ListView lvLeftMenu;
    private Cursor cursor;
    private String title = "所有";
    private NoteDatabaseService noteDatabaseService;
    private SimpleCursorAdapter simpleCursorAdapter;
    private ListViewMenuAdapter listViewMenuAdapter;
    private List<NoteContent> noteContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //标题栏
        final Toolbar toolbar = (Toolbar) findViewById(R.id.tb_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);

        setDefaultFragment();
        //滑动抽屉
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_main);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerToggle.syncState();
        drawerLayout.setDrawerListener(drawerToggle);

        lvLeftMenu = (ListView) findViewById(R.id.lv_left_menu);
        LayoutInflater inflater = LayoutInflater.from(this);
//        lvLeftMenu.addHeaderView(inflater.inflate(R.layout.navigation_header, lvLeftMenu, false));
        List<ListViewMenuItem> items = new ArrayList<ListViewMenuItem>();
        noteDatabaseService = new NoteDatabaseService(this);
        //第一次把所有写进去
        if (noteDatabaseService.insert(new NoteContent("所有", "", MainActivity.getCurrentTimeStr()))) {
            Log.e(TAG, "insert success");
        } else {
            Log.e(TAG, "insert failed");
        }
        noteContents = noteDatabaseService.getScrollData("where content = '' order by time");
        for (NoteContent noteContent : noteContents) {
            items.add(new ListViewMenuItem(noteContent.getCategory()));
        }
        items.add(new ListViewMenuItem());
        items.add(new ListViewMenuItem(android.R.drawable.ic_input_add, " "));
        listViewMenuAdapter = new ListViewMenuAdapter(this, items);
        lvLeftMenu.setVerticalScrollBarEnabled(false);
        lvLeftMenu.setAdapter(listViewMenuAdapter);
        lvLeftMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Log.e(TAG, "position=" + position + ";id=" + id);
                //如果加了header就要-1
                listViewMenuAdapter.setSelectedItemPosition(position);
                if (position >= noteContents.size()) {

                    View viewMain = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_add_category, null);
                    final TextView etAddCategroyName = (TextView) viewMain.findViewById(R.id.et_add_category_name);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setIcon(R.mipmap.ic_launcher);
                    builder.setTitle("输入文件夹名字：");
                    builder.setView(viewMain);

                    DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case Dialog.BUTTON_POSITIVE:
                                    String categoryName = etAddCategroyName.getText().toString();
                                    if (!categoryName.equals("") && !categoryName.contains("'")) {
                                        if (noteDatabaseService.insert(new NoteContent(categoryName, "", MainActivity.getCurrentTimeStr()))) {
                                            Log.e(TAG, "insert success");
                                        } else {
                                            Log.e(TAG, "insert failed");
                                        }
                                        //因为有一个横线
                                        listViewMenuAdapter.setSelectedItemPosition(position - 1);
                                        title = categoryName;
                                        toolbar.setTitle(title);
                                        switchFragment();
                                        refreshLeftListView();
                                    } else {
                                        Log.e(TAG, "involid name");
                                    }
                                    dialog.dismiss();
                                    break;
                                case Dialog.BUTTON_NEGATIVE:
//                                Toast.makeText(view.getContext(), "取消" + which, Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    };

                    builder.setPositiveButton("确认", dialogOnclicListener);
                    builder.setNegativeButton("取消", dialogOnclicListener);

                    builder.create().show();
                } else if (position >= 0) {
                    //由于加了分割线
                    title = noteContents.get((int) id).getCategory();
                    toolbar.setTitle(noteContents.get((int) id).getCategory());
                    refreshLeftListView();
                    switchFragment();
                    drawerLayout.closeDrawers();
                }
            }
        });
        lvLeftMenu.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (position >= noteContents.size()) {


                } else if (!noteContents.get((int) id).getCategory().equals("所有") && position >= 0) {
                    DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case Dialog.BUTTON_POSITIVE:
                                    noteDatabaseService.delete("where category = '" + noteContents.get(position).getCategory() + "'");
                                    refreshLeftListView();
                                    dialog.dismiss();
                                    title = "所有";
                                    listViewMenuAdapter.setSelectedItemPosition(0);
                                    toolbar.setTitle(title);
                                    switchFragment();
                                    drawerLayout.closeDrawers();
                                    break;
                                case Dialog.BUTTON_NEGATIVE:
//                                Toast.makeText(view.getContext(), "取消" + which, Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    };
                    //dialog参数设置
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(view.getContext());  //先得到构造器
                    builder.setTitle("提示"); //设置标题
                    builder.setMessage("是否删除该文件夹以及其中的所有笔记?"); //设置内容
                    builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
                    builder.setPositiveButton("确认", dialogOnclicListener);
                    builder.setNegativeButton("取消", dialogOnclicListener);
                    builder.create().show();
                }
                return false;
            }
        });

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
                bundle.putSerializable("noteContent", new NoteContent(title, "", ""));
                editContentIntent.putExtras(bundle);
                startActivity(editContentIntent);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        refreshLeftListView();
    }

    private void refreshLeftListView() {
        noteContents = noteDatabaseService.getScrollData("where content = '' order by time");
        List<ListViewMenuItem> items = new ArrayList<ListViewMenuItem>();
        for (NoteContent noteContent : noteContents) {
            items.add(new ListViewMenuItem(noteContent.getCategory()));
        }
        items.add(new ListViewMenuItem());
        items.add(new ListViewMenuItem(android.R.drawable.ic_input_add, " "));
        listViewMenuAdapter.changeItems(items);
        listViewMenuAdapter.notifyDataSetChanged();
    }

    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        MainActivityFragment mainActivityFragment = new MainActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        mainActivityFragment.setArguments(bundle);
        transaction.add(R.id.fragment_main, mainActivityFragment);
//        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    private void switchFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        MainActivityFragment mainActivityFragment = new MainActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        mainActivityFragment.setArguments(bundle);
        transaction.add(R.id.fragment_main, mainActivityFragment);
//        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
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

    public static String getCurrentTimeStr() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String strTime = formatter.format(curDate);
        return strTime;
    }
}
