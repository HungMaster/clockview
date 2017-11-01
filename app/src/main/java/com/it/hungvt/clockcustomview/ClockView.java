package com.it.hungvt.clockcustomview;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 11/1/2017.
 */

public class ClockView extends View {

    private final int BORDER_COLOR_DEFAULT = 0xff4caf50;
    private final int BORDER_WIDTH_DEFAULT = 16;
    private final int BACKGROUND_COLOR_DEFAULT = 0xff4ca50;
    private final int TEXT_COLOR_DEFAULT = 0xffffffff;
    private final int TEXT_HM_SIZE_DEFAULT = 36;
    private final int TEXT_AP_SIZE_DEFAULT = 36;
    private final int TEXT_DATE_SIZE_DEFAULT = 36;


    private int borderColor;
    private int borderWidth;
    private int backgroundColor;
    private int textColor;
    private int textHmSize; // hour, mininute
    private int textApSize; // AM, PM
    private int textDateSize; // Date, Month, Year


    private String hm;
    private String ap;
    private String date;

    private Paint paint;

    
    
    public ClockView(Context context) {
        super(context);
        setup();
    }



    public ClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setup();
        setupXml(context,attrs);
    }


    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup();
        setupXml(context,attrs);
    }

    private void setup() {
        paint = new Paint();
        paint.setAntiAlias(true);

        borderColor = BORDER_COLOR_DEFAULT;
        borderWidth = BORDER_WIDTH_DEFAULT;
        borderColor = BORDER_COLOR_DEFAULT;
        textColor = TEXT_COLOR_DEFAULT;
        textHmSize = TEXT_HM_SIZE_DEFAULT;
        textApSize = TEXT_AP_SIZE_DEFAULT;
        textDateSize = TEXT_DATE_SIZE_DEFAULT;

        hm = "08:00:00";
        ap = "PM";
        date = "Wednesday, November 01,2017";

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true){
                    getDateTimeNow();

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }

    private void getDateTimeNow() {
        final Date dateTime = new Date();
        SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm:ss", Locale.US);
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        final String resultHour = sdfHour.format(dateTime);
        final String resultDate = sdfDate.format(dateTime);

        ((Activity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hm = resultHour;
                date = resultDate;
                invalidate();

            }
        });


    }

    private void setupXml(Context context, AttributeSet attrs) {

        TypedArray ta=null;
        ta = context.obtainStyledAttributes(attrs,R.styleable.ClockView);
        borderColor = ta.getColor(R.styleable.ClockView_border_color,BORDER_COLOR_DEFAULT);
        borderWidth = ta.getDimensionPixelSize(R.styleable.ClockView_border_width,BORDER_WIDTH_DEFAULT);
        backgroundColor = ta.getColor(R.styleable.ClockView_background_color,BACKGROUND_COLOR_DEFAULT);
        textColor = ta.getColor(R.styleable.ClockView_text_color,TEXT_COLOR_DEFAULT);
        textHmSize = ta.getDimensionPixelSize(R.styleable.ClockView_text_hm_size,TEXT_HM_SIZE_DEFAULT);
        textApSize = ta.getDimensionPixelSize(R.styleable.ClockView_text_ap_size,TEXT_AP_SIZE_DEFAULT);
        textDateSize = ta.getDimensionPixelSize(R.styleable.ClockView_text_date_size,TEXT_DATE_SIZE_DEFAULT);

        ta.recycle();

    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setTextHmSize(int textHmSize) {
        this.textHmSize = textHmSize;
    }

    public void setTextApSize(int textApSize) {
        this.textApSize = textApSize;
    }

    public void setTextDateSize(int textDateSize) {
        this.textDateSize = textDateSize;
    }


    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        int width = getWidth();
        int height = getHeight();

        paint.setColor(borderColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(new Rect(0,0,width,height),paint);

        paint.setColor(backgroundColor);
        canvas.drawRect(new Rect(borderWidth,borderWidth,width-borderWidth,height-borderWidth),paint);

        paint.setColor(textColor);
        paint.setTextSize(textDateSize);


        // Draw Text
        Rect rectDate = new Rect();
        paint.getTextBounds(date,0,date.length(),rectDate);
        canvas.drawText(date,(width-rectDate.width())/2,height/2+16+rectDate.height(),paint);

        // Draw Time
        paint.setTextSize(textHmSize);
        Rect rectHm = new Rect();
        paint.getTextBounds(hm,0,hm.length(),rectHm);

        Rect rectAp = new Rect();
        paint.getTextBounds(ap,0,ap.length(),rectAp);

        int widthHmAp = rectHm.width() +  rectAp.width() + 16;
        canvas.drawText(hm,(width-widthHmAp)/2,height/2-16,paint);

        paint.setTextSize(textApSize);
        canvas.drawText(ap,(width-widthHmAp)/2+rectHm.width()+16,height/2-16,paint);

    }
}
