package com.lee.wait.adapter;

import android.text.TextUtils;

/**
 * Created by Administrator on 2016/1/3.
 */
public class ListViewMenuItem {
    public ListViewMenuItem(int icon, String name)
    {
        this.icon = icon;
        this.name = name;

        if (icon == NO_ICON && TextUtils.isEmpty(name))
        {
            type = TYPE_SEPARATOR;
        } else if (icon == NO_ICON)
        {
            type = TYPE_NO_ICON;
        } else
        {
            type = TYPE_NORMAL;
        }

        if (type != TYPE_SEPARATOR && TextUtils.isEmpty(name))
        {
            throw new IllegalArgumentException("you need set a name for a non-SEPARATOR item");
        }
    }

    public ListViewMenuItem(String name)
    {
        this(NO_ICON, name);
    }

    public ListViewMenuItem()
    {
        this(null);
    }

    private static final int NO_ICON = 0;
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_NO_ICON = 1;
    public static final int TYPE_SEPARATOR = 2;

    int type;
    String name;
    int icon;

}
