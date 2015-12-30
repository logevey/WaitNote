package com.lee.wait.waitnote;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.lee.wait.database.NoteContent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private GridView gview;
    private SimpleAdapter sim_adapter;
    private List<Map<String, Object>> data_list;
   // private String[] iconName = {"通讯录", "日历", "照相机", "时钟", "游戏", "短信", "铃声"};
    private View view;
    private NoteDatabase noteDatabase;
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        //下拉刷新布局控件
        final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
        if (swipeView == null) {
            Log.e("MainActivity", "Null");
        } else {
            Log.e("MainActivity", swipeView.toString());
            swipeView.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);

            SwipeRefreshLayout.OnRefreshListener orl=new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeView.setRefreshing(true);
                    Log.d("Swipe", "Refreshing Number");
                    ( new Handler()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeView.setRefreshing(false);
                            double f = Math.random();
                            addGridViewItemToStart("test");
                        }
                    },1000);
                }
            };
            swipeView.setOnRefreshListener(orl);
        }

        noteDatabase = new NoteDatabase(getActivity());
//        for (int i = 0; i <  iconName.length; i++) {
//
//            String strTime = getCurrentTimeStr();
//
//            noteDatabase.insert(new NoteContent(iconName[i],strTime));
//        }
        //新建List
        data_list = new ArrayList<Map<String, Object>>();
        List<NoteContent> noteContents = noteDatabase.getScrollData(0,noteDatabase.getCount());
        for (int i = 0; i < noteContents.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("text", i+":"+noteContents.get(i).getCategory()+":"+noteContents.get(i).getContent()+":"+noteContents.get(i).getTime());
            data_list.add(map);
        }

        gview = (GridView) view.findViewById(R.id.gridView);

        //新建适配器
        String[] from = {"text"};
        int[] to = {R.id.text};
        sim_adapter = new SimpleAdapter(view.getContext(), data_list, R.layout.content_item, from, to);
        //配置适配器
        gview.setAdapter(sim_adapter);
        gview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent editContentIntent = new Intent();
                editContentIntent.setClass(view.getContext(), EditContentActivity.class);
                editContentIntent.putExtra("str_content", (String) data_list.get(position).get("text"));
                startActivity(editContentIntent);
            }
        });
        gview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {
                final int tmpPosition = position;
                DialogInterface.OnClickListener dialogOnclicListener=new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which){
                            case Dialog.BUTTON_POSITIVE:
                                Toast.makeText(view.getContext(), "已删除" , Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                deleteGridViewItem(tmpPosition);
                                break;
                            case Dialog.BUTTON_NEGATIVE:
//                                Toast.makeText(view.getContext(), "取消" + which, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                break;
                        }
                    }
                };
                //dialog参数设置
                AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());  //先得到构造器
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


//    public List<Map<String, Object>> getData() {
//        for (int i = 0; i < iconName.length; i++) {
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("text", iconName[i]);
//            data_list.add(map);
//        }
//        return data_list;
//    }

    private boolean dialog(){
        //先new出一个监听器，设置好监听
        DialogInterface.OnClickListener dialogOnclicListener=new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case Dialog.BUTTON_POSITIVE:
                        Toast.makeText(view.getContext(), "确认" + which, Toast.LENGTH_SHORT).show();
                        break;
                    case Dialog.BUTTON_NEGATIVE:
                        Toast.makeText(view.getContext(), "取消" + which, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        //dialog参数设置
        AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());  //先得到构造器
        builder.setTitle("提示"); //设置标题
        builder.setMessage("是否删除?"); //设置内容
        builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
        builder.setPositiveButton("确认", dialogOnclicListener);
        builder.setNegativeButton("取消", dialogOnclicListener);
        builder.create().show();

        return true;
    }
    private void addGridViewItemToStart(String strContent)
    {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("text", strContent);
        //data_list.add(map);
        data_list.add(0, map);
        sim_adapter.notifyDataSetChanged();
        noteDatabase.insert(new NoteContent(strContent,getCurrentTimeStr() ));
    }
    private void addGridViewItemToEnd(String strContent)
    {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("text", strContent);
        //data_list.add(map);
        data_list.add(0, map);
        sim_adapter.notifyDataSetChanged();
    }

    private void deleteGridViewItem(int itemIndex)
    {
        int size = data_list.size();
        if( size > 0 && size > itemIndex )
        {
            data_list.remove(itemIndex);
            sim_adapter.notifyDataSetChanged();
            noteDatabase.delete(itemIndex);
        }
    }
    private String getCurrentTimeStr(){
        SimpleDateFormat formatter    =   new    SimpleDateFormat    ("yyyy年MM月dd日  HH:mm:ss");
        Date curDate    =   new    Date(System.currentTimeMillis());//获取当前时间
        String    strTime    =    formatter.format(curDate);
        return strTime;
    }

}
