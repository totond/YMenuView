package com.yanzhikai.ymenuview.PositionBuilders;

import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import static android.widget.RelativeLayout.ABOVE;
import static android.widget.RelativeLayout.ALIGN_PARENT_BOTTOM;
import static android.widget.RelativeLayout.ALIGN_PARENT_LEFT;
import static android.widget.RelativeLayout.ALIGN_PARENT_RIGHT;
import static android.widget.RelativeLayout.ALIGN_PARENT_TOP;
import static android.widget.RelativeLayout.BELOW;
import static android.widget.RelativeLayout.CENTER_HORIZONTAL;
import static android.widget.RelativeLayout.CENTER_VERTICAL;
import static android.widget.RelativeLayout.LEFT_OF;
import static android.widget.RelativeLayout.RIGHT_OF;

/**
 * Created by yany on 2017/9/13.
 */

public class MenuPositionBuilder extends PositionBuilder {
    private View mMenuButton;
    private int mWidth = 0, mHeight = 0;
    private int mXMargin = 0, mYMargin = 0;
    private
    @PositionBuilder.MarginOrientationX
    int mMarginOrientationX = MARGIN_LEFT;
    private
    @PositionBuilder.MarginOrientationY
    int mMarginOrientationY = MARGIN_TOP;
    private boolean mIsXCenter = false, mIsYCenter = false;

    public MenuPositionBuilder(View menuButton) {
        mMenuButton = menuButton;
    }

    @Override
    public MenuPositionBuilder setWidthAndHeight(int width, int height) {
        mWidth = width;
        mHeight = height;
        return this;
    }

    @Override
    public MenuPositionBuilder setXYMargin(int XMargin, int YMargin) {
        mXMargin = XMargin;
        mYMargin = YMargin;
        return this;
    }

    @Override
    public MenuPositionBuilder setMarginOrientation(@MarginOrientationX int marginOrientationX, @MarginOrientationY int marginOrientationY) {
        mMarginOrientationX = marginOrientationX;
        mMarginOrientationY = marginOrientationY;
        return this;
    }

    public MenuPositionBuilder setIsXYCenter(boolean isXCenter, boolean isYCenter) {
        mIsXCenter = isXCenter;
        mIsYCenter = isYCenter;
        return this;
    }


    @Override
    public void finish() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(mWidth, mHeight);

        Log.d("layoutParams", "mIsXCenter: " + mIsXCenter);

        if (mIsXCenter){
            layoutParams.addRule(CENTER_HORIZONTAL);
            Log.d("layoutParams", "finish: 0000");
        }else {
            if (mMarginOrientationX == MARGIN_LEFT) {
                Log.d("layoutParams", "finish: 1111");
                layoutParams.leftMargin = mXMargin;
                layoutParams.addRule(ALIGN_PARENT_LEFT);
            } else {
                Log.d("layoutParams", "finish: 3333");
                layoutParams.rightMargin = mXMargin;
                layoutParams.addRule(ALIGN_PARENT_RIGHT);
            }
        }

        if (mIsYCenter){
            layoutParams.addRule(CENTER_VERTICAL);
        }else {
            if (mMarginOrientationY == MARGIN_TOP) {
                layoutParams.topMargin = mYMargin;
                layoutParams.addRule(ALIGN_PARENT_TOP);
                Log.d("layoutParams", "finish: 2222");

            } else {
                layoutParams.bottomMargin = mYMargin;
                Log.d("layoutParams", "finish: ");
                layoutParams.addRule(ALIGN_PARENT_BOTTOM);
            }
        }


        mMenuButton.setLayoutParams(layoutParams);
    }
}
