package com.appttude.h_mal.days_left.arc;

import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ArcAnimation extends Animation {

    private CircleView arcView;

    private float oldAngle;
    private float newAngle;

    public ArcAnimation(CircleView arcView, int newAngle) {
        this.oldAngle = arcView.getArcAngle();
        this.newAngle = newAngle;
        this.arcView = arcView;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation transformation) {
        float angle = 0 + ((newAngle - oldAngle) * interpolatedTime);

        arcView.setArcAngle(angle);
        arcView.requestLayout();
    }
}