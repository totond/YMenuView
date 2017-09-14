package com.yanzhikai.ymenuview.PositionBuilders;

import android.support.annotation.IntDef;

import com.yanzhikai.ymenuview.OptionButton2;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by yany on 2017/9/13.
 */

public abstract class PositionBuilder {
    public static final int MARGIN_LEFT = 0, MARGIN_TOP = 1, MARGIN_RIGHT = 2, MARGIN_BOTTOM = 3;

    @IntDef({MARGIN_LEFT,MARGIN_RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public  @interface MarginOrientationX {}

    @IntDef({MARGIN_TOP,MARGIN_BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public  @interface MarginOrientationY {}

    public abstract PositionBuilder setWidthAndHeight(int width,int height);

    public abstract PositionBuilder setXYMargin(int XMargin,int YMargin);

    public abstract PositionBuilder setMarginOrientation(@MarginOrientationX int marginOrientationX,@MarginOrientationY int marginOrientationY);

    public abstract void finish();

}
