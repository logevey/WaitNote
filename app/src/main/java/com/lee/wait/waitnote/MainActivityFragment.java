package com.lee.wait.waitnote;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
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
    private String[] iconName = {"通讯录"};


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
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
                            addGridViewItem("test");
                        }
                    },1000);
                }
            };
            swipeView.setOnRefreshListener(orl);
        }

        gview = (GridView) view.findViewById(R.id.gridView);
        //新建List
        data_list = new ArrayList<Map<String, Object>>();
        //获取数据
        getData();
        //新建适配器
        String[] from = {"text"};
        int[] to = {R.id.text};
        sim_adapter = new SimpleAdapter(view.getContext(), data_list, R.layout.content_item, from, to);
        //配置适配器

        gview.setAdapter(sim_adapter);
        addGridViewItem("test");
        return view;
    }

    public List<Map<String, Object>> getData() {
        for (int i = 0; i < iconName.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("text", iconName[i]);
            data_list.add(map);
        }
        return data_list;
    }


    private void addGridViewItem(String strContent)
    {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("text", strContent);
        data_list.add(map);
        sim_adapter.notifyDataSetChanged();
    }

    private void deleteGridViewItem()
    {
        int size = data_list.size();
        if( size > 0 )
        {
            data_list.remove(data_list.size() - 1);
            sim_adapter.notifyDataSetChanged();
        }
    }

}
