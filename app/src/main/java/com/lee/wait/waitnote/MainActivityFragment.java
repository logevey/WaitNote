package com.lee.wait.waitnote;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.lee.wait.database.NoteContent;
import com.lee.wait.database.NoteDatabaseService;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private static final String TAG = "MainActivityFragment";
    private GridView gridView;
    private SimpleCursorAdapter simpleCursorAdapter;
    private View view;
    private NoteDatabaseService noteDatabaseService;
    private Cursor cursor;
    private String title;
    public MainActivityFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        Bundle bundle = getArguments();
        title = bundle.getString("title");
        //下拉刷新布局控件
        final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) view.findViewById(R.id.sl_main_fragment);
        if (swipeView == null) {
            Log.e(TAG, "Null");
        } else {
//            Log.e(TAG, swipeView.toString());
            swipeView.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
            SwipeRefreshLayout.OnRefreshListener orl = new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeView.setRefreshing(true);
//                    Log.d("Swipe", "Refreshing Number");
                    (new Handler()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getCursor();
                            simpleCursorAdapter.changeCursor(cursor);
                            swipeView.setRefreshing(false);
                        }
                    }, 1000);
                }
            };
            swipeView.setOnRefreshListener(orl);
        }

        noteDatabaseService = new NoteDatabaseService(getActivity());


        getCursor();
        gridView = (GridView) view.findViewById(R.id.gv_main_fragment);
        if (cursor != null) {
            simpleCursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.item_fragment_content, cursor, new String[]{"_id", "content", "time"}, new int[]{R.id.iv_id, R.id.tv_content, R.id.tv_time}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            gridView.setAdapter(simpleCursorAdapter);
        }


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent editContentIntent = new Intent();
                editContentIntent.setClass(view.getContext(), EditContentActivity.class);
                Bundle bundle = new Bundle();
                Log.e(TAG, cursor.getInt(0) + " " + cursor.getString(2));
                bundle.putSerializable("noteContent", new NoteContent(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
                editContentIntent.putExtras(bundle);
                startActivity(editContentIntent);
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {
                DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case Dialog.BUTTON_POSITIVE:
                                Toast.makeText(view.getContext(), "已删除", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
//
                                noteDatabaseService.delete(cursor.getInt(0));
                                getCursor();
                                simpleCursorAdapter.changeCursor(cursor);
                                break;
                            case Dialog.BUTTON_NEGATIVE:
//                                Toast.makeText(view.getContext(), "取消" + which, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                break;
                        }
                    }
                };
                //dialog参数设置
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());  //先得到构造器
                builder.setTitle("提示"); //设置标题
                builder.setMessage("是否删除?"); //设置内容
                builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
                builder.setPositiveButton("确认", dialogOnclicListener);
                builder.setNegativeButton("取消", dialogOnclicListener);
                builder.create().show();

                return false;
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getCursor();

        simpleCursorAdapter.changeCursor(cursor);
    }
    public void getCursor(){
        if(title.equals("所有")){
            cursor = noteDatabaseService.getRawScrollData("where content != '' order by time DESC");
        }else{
            cursor = noteDatabaseService.getRawScrollData("where content != '' and category = '" + title+"' order by time DESC");
        }
    }
}
