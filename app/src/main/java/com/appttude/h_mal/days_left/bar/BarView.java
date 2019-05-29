package com.appttude.h_mal.days_left.bar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class BarView extends View {

    private RectF topRect;
    private RectF bottomRect;
    private Paint colourOne;
    private Paint colourTwo;
    private RectF hardEdge;
    private float cover;

    public BarView(Context context) {
        super(context);
        init(null);
    }

    public BarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public BarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public BarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public void init (@Nullable AttributeSet attrs){
        topRect = new RectF();
        bottomRect = new RectF();
        hardEdge = new RectF();

        colourOne = new Paint(Paint.ANTI_ALIAS_FLAG);
        colourTwo = new Paint(Paint.ANTI_ALIAS_FLAG);

        colourOne.setColor(Color.BLUE);
        colourTwo.setColor(Color.GREEN);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        topRect.centerX();
        topRect.centerY();
        topRect.left = 0f;
        topRect.top = 0f;
        topRect.right = getWidth();
        topRect.bottom = getHeight();

        canvas.drawRoundRect(topRect,20,20,colourOne);


        bottomRect.centerX();
        bottomRect.centerY();
        if (cover < 0.05f){
            cover = 0.05f;
        }
        bottomRect.left = ((getWidth()-20)*cover);
        bottomRect.top = 0f;
        bottomRect.right = getWidth();
        bottomRect.bottom = getHeight();

        canvas.drawRoundRect(bottomRect,20,20,colourTwo);

        hardEdge.centerX();
        hardEdge.centerY();
        hardEdge.top = bottomRect.top;
        hardEdge.bottom = bottomRect.bottom;

        hardEdge.left = bottomRect.left;
        hardEdge.right = hardEdge.left+60;

        canvas.drawRoundRect(hardEdge,0,0,colourTwo);
    }

    public float getCover() {
        return cover;
    }

    public void setCover(float cover) {
        this.cover = cover;
    }

    public void setCover(float total,float number) {
        this.cover = number/total;
    }

    public void setColourOne(int colour) {
        colourOne.setColor(colour);
    }

    public void setColourTwo(int colour) {
        colourTwo.setColor(colour);
    }
}
