package com.appttude.h_mal.days_left.bar;

import android.view.animation.Animation;
import android.view.animation.Transformation;

public class BarAnimation extends Animation {

    private BarView barView;

    private int targetWidth;
    private int startWidth;

    public BarAnimation(BarView barView, int targetWidth, int startWidth) {
        this.barView = barView;
        this.targetWidth = targetWidth;
        this.startWidth = startWidth;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {

        int newWidth = (int) (startWidth + targetWidth * interpolatedTime);

        barView.getLayoutParams().width = newWidth;
        barView.requestLayout();
    }
}
