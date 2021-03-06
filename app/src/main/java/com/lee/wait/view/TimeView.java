package com.lee.wait.view;

/**
 * Created by Administrator on 2015/12/28.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.lee.wait.waitnote.R;

import java.util.Calendar;

public class TimeView extends View
{
    /**
     * 文本
     */
    private String mTitleText;
    /**
     * 文本的颜色
     */
    private int mTitleTextColor;
    /**
     * 文本的大小
     */
    private int mTitleTextSize;

    /**
     * 绘制时控制文本绘制的范围
     */
    private Paint paint;

    private int timeHour;
    private  int timeMinute;
    private int timeSecond;

    public TimeView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public TimeView(Context context)
    {
        this(context, null);
    }

    /**
     * 获得我自定义的样式属性
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public TimeView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        /**
         * 获得我们所定义的自定义样式属性
         */
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,  R.styleable.TimeView, defStyle, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++)
        {
            int attr = a.getIndex(i);
            switch (attr)
            {
                case R.styleable.TimeView_timeSecond:
                    //mTitleText = a.getString(attr);
                    break;
                case R.styleable.TimeView_timeMinute:
                    // 默认颜色设置为黑色
                    //mTitleTextColor = a.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.TimeView_timeHour:
                    // 默认设置为16sp，TypeValue也可以把sp转化为px
                    // mTitleTextSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                    //    TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;

            }

        }
        a.recycle();
        Calendar c = Calendar.getInstance();
        int second = c.get(Calendar.SECOND);
        int minute = c.get(Calendar.MINUTE);
        int hour = c.get(Calendar.HOUR);

        timeSecond = second;
        timeMinute = minute;
        if(hour > 12)
        {
            timeHour = hour -12;
        }
        else
        {
            timeHour = hour;
        }
        new Thread()
        {
            public void run()
            {
                while (true)
                {
                    timeSecond ++ ;
                    if (timeSecond == 60)
                    {
                        timeMinute++;
                        timeSecond = 0;
                    }
                    if (timeMinute == 60)
                    {
                        timeHour++;
                        timeMinute = 0;
                    }
                    if (timeHour == 12)
                    {
                        timeHour = 0;
                    }

                    postInvalidate();


                    try
                    {
                        Thread.sleep(1000);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }


    @Override
    protected void onDraw(Canvas canvas)
    {
        paint =  new Paint();
        paint.setAntiAlias(true);

        paint.setStyle(Paint.Style.STROKE);

        canvas.translate(getWidth()/2, getHeight()/2); //将位置移动画纸的坐标点:150,150

        canvas.drawCircle(0, 0, getWidth()/2, paint); //画圆圈



        //使用path绘制路径文字

        canvas.save();

        //  canvas.translate(-75, -75);

        //  Path path = new Path();

        // path.addArc(new RectF(0,0,150,150), -180, 180);

        Paint citePaint = new Paint(paint);

        citePaint.setTextSize(14);

        citePaint.setStrokeWidth(1);

        canvas.drawText("wait.lee", -20, -(getWidth()/3), citePaint);

        canvas.restore();



        Paint tmpPaint = new Paint(paint); //小刻度画笔对象

        tmpPaint.setStrokeWidth(1);



        float  y=getWidth()/2;

        int count = 60; //总刻度数



        for(int i=0 ; i <count ; i++){

            if(i%5 == 0){
                String num ;
                canvas.drawLine(0f, y, 0, y  - 12f, paint);
                if(i / 5 + 6 > 12){
                    num = String.valueOf(i / 5 - 6);
                }else{
                    num = String.valueOf(i / 5 + 6);
                }
                canvas.drawText(num, -4f, y-25f, tmpPaint);



            }else{

                canvas.drawLine(0f, y, 0f, y - 5f, tmpPaint);

            }

            canvas.rotate(360 / count, 0f, 0f); //旋转画纸

        }



        //绘制指针

        tmpPaint.setColor(Color.GRAY);

        tmpPaint.setStrokeWidth(4);

        canvas.drawCircle(0, 0, 7, tmpPaint);

        tmpPaint.setStyle(Paint.Style.FILL);

        tmpPaint.setColor(Color.YELLOW);

        canvas.drawCircle(0, 0, 5, tmpPaint);



        canvas.save();
        canvas.rotate(6 * 5 * timeHour);
        paint.setStrokeWidth(4);
        canvas.drawLine(0, 10, 0, -(getWidth() / 2 - 100), paint);
        canvas.restore();

        canvas.save();
        canvas.rotate(6 * timeMinute);
        paint.setStrokeWidth(2);
        canvas.drawLine(0, 10, 0, -(getWidth() / 2 - 60), paint);
        canvas.restore();

        canvas.save();
        canvas.rotate(6*timeSecond);
        paint.setStrokeWidth(1);
        canvas.drawLine(0, 10, 0,  -(getWidth()/2-30), paint);
        canvas.restore();
    }
}