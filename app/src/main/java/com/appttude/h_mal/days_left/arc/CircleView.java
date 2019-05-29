package com.appttude.h_mal.days_left.arc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class CircleView extends View {

    private float arcAngle;
    private RectF mRect;
    private RectF mRect2;
    private Paint mPaint;
    private Paint paint2;
    private int thinkness;

    public CircleView(Context context) {
        super(context);
        init(null);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs);
    }

    private void init(@Nullable AttributeSet attributeSet){
        mRect = new RectF();
        mRect2 = new RectF();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint2 = new Paint();
        paint2.setAntiAlias(true);
        paint2.setColor(Color.TRANSPARENT);
        paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);

    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (thinkness == 0){
            thinkness = 35;
        }


        mRect.centerX();
        mRect.centerY();
        mRect.left = 10f;
        mRect.top = 10f;
        mRect.right = getWidth()-10;
        mRect.bottom = getWidth()-10;

        mRect2.centerX();
        mRect2.centerY();
        mRect2.left = mRect.left + thinkness;
        mRect2.top = mRect.top + thinkness;
        mRect2.right = mRect.right - thinkness;
        mRect2.bottom = mRect.bottom - thinkness;

        setBackgroundColor(Color.TRANSPARENT);
        if (mPaint.getColor() == 0){
            mPaint.setColor(Color.BLUE);
        }

        setLayerType(View.LAYER_TYPE_SOFTWARE,null);

        canvas.drawArc(mRect,270f,arcAngle,true,mPaint);
        canvas.drawOval(mRect2,paint2);
    }

    public void setPaintColor(int color){
        mPaint.setColor(color);
    }

    public void setThickness(int thickness){
        this.thinkness = thickness;
    }

    public float getArcAngle() {
        return arcAngle;
    }

    public void setArcAngle(float arcAngle) {
        this.arcAngle = arcAngle;
    }
}
