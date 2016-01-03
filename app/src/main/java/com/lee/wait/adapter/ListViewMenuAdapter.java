package com.lee.wait.adapter;


import android.content.Context;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.TextViewCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lee.wait.waitnote.R;

import java.util.List;

/**
 * Created by Administrator on 2016/1/3.
 */
public class ListViewMenuAdapter extends BaseAdapter {


    private final int mIconSize;
    private LayoutInflater mInflater;
    private Context mContext;
    private List<ListViewMenuItem> items;
    private int selectedItemPosition=0;
    public ListViewMenuAdapter(Context context, List<ListViewMenuItem> items) {
        mInflater = LayoutInflater.from(context);
        mContext = context;

        mIconSize = context.getResources().getDimensionPixelSize(R.dimen.drawer_icon_size);//24dp
        this.items = items;
    }


    public void changeItems(List<ListViewMenuItem> items) {
        this.items = items;
    }

    public void setSelectedItemPosition(int selectedItemPosition){
        this.selectedItemPosition=selectedItemPosition;
    }
    @Override
    public int getCount() {
        return items.size();
    }


    @Override
    public Object getItem(int position) {
        return items.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListViewMenuItem item = items.get(position);
        switch (item.type) {
            case ListViewMenuItem.TYPE_NORMAL:
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.item_drawer_category, parent,
                            false);
                }
                TextView itemView = (TextView) convertView;
                itemView.setText(item.name);
                Drawable icon = mContext.getResources().getDrawable(item.icon);
                setIconColor(icon);
                if (icon != null) {
                    icon.setBounds(0, 0, mIconSize, mIconSize);
                    TextViewCompat.setCompoundDrawablesRelative(itemView, icon, null, null, null);
                }

                break;
            case ListViewMenuItem.TYPE_NO_ICON:
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.item_drawer_category_noicon,
                            parent, false);
                }
                TextView subHeader = (TextView) convertView;
                subHeader.setText(item.name);
                if(selectedItemPosition == position){
                    Log.e("ListViewAdapter","selectItemPositon= "+selectedItemPosition);
                    subHeader.setSelected(true);
                    subHeader.setPressed(true);
                    subHeader.setTextColor(Color.parseColor("#009999"));
                    subHeader.setBackgroundColor(Color.parseColor("#EFEFEF"));
                }else{
                    subHeader.setTextColor(Color.GRAY);
                    subHeader.setBackgroundColor(Color.parseColor("#E5E5E5"));
                }
                break;
            case ListViewMenuItem.TYPE_SEPARATOR:
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.item_drawer_category_separator,
                            parent, false);
                }
                break;
        }

        return convertView;
    }

    public void setIconColor(Drawable icon) {
        int textColorSecondary = android.R.attr.textColorSecondary;
        TypedValue value = new TypedValue();
        if (!mContext.getTheme().resolveAttribute(textColorSecondary, value, true)) {
            return;
        }
        int baseColor = mContext.getResources().getColor(value.resourceId);
        icon.setColorFilter(baseColor, PorterDuff.Mode.MULTIPLY);
    }
}
