package com.yanzhikai.ymenuview.PositionBuilders;

import android.view.View;
import android.widget.RelativeLayout;

import com.yanzhikai.ymenuview.OptionButton2;

import static android.widget.RelativeLayout.ABOVE;
import static android.widget.RelativeLayout.ALIGN_PARENT_BOTTOM;
import static android.widget.RelativeLayout.ALIGN_PARENT_LEFT;
import static android.widget.RelativeLayout.ALIGN_PARENT_RIGHT;
import static android.widget.RelativeLayout.ALIGN_PARENT_TOP;
import static android.widget.RelativeLayout.BELOW;
import static android.widget.RelativeLayout.LEFT_OF;
import static android.widget.RelativeLayout.RIGHT_OF;

/**
 * Created by yany on 2017/9/13.
 */

public class OptionPositionBuilder extends PositionBuilder {
    private OptionButton2 mOptionButton;
    private View mMenuButton;
    private int mWidth = 0, mHeight = 0;
    private int mXMargin = 0, mYMargin = 0;
    private @PositionBuilder.MarginOrientationX int mMarginOrientationX = MARGIN_LEFT;
    private @PositionBuilder.MarginOrientationY int mMarginOrientationY = MARGIN_TOP;
    private boolean mIsAlignMenuButton = false;

    public OptionPositionBuilder(OptionButton2 optionButton, View menuButton) {
        mOptionButton = optionButton;
        mMenuButton = menuButton;
    }

    @Override
    public OptionPositionBuilder setWidthAndHeight(int width,int height){
        mWidth = width;
        mHeight = height;
        return this;
    }

    @Override
    public OptionPositionBuilder setXYMargin(int XMargin, int YMargin) {
        mXMargin = XMargin;
        mYMargin = YMargin;
        return this;
    }

    @Override
    public OptionPositionBuilder setMarginOrientation(@MarginOrientationX int marginOrientationX, @MarginOrientationY int marginOrientationY) {
        mMarginOrientationX = marginOrientationX;
        mMarginOrientationY = marginOrientationY;
        return this;
    }

    public OptionPositionBuilder isAlignMenuButton(boolean isAlignMenuButton){
        mIsAlignMenuButton = isAlignMenuButton;
        return this;
    }

    @Override
    public void finish(){
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(mWidth,mHeight);

        if (mMarginOrientationX == MARGIN_LEFT){
            layoutParams.leftMargin = mXMargin;
            if (mIsAlignMenuButton){
                layoutParams.addRule(RIGHT_OF,mMenuButton.getId());
            }else {
                layoutParams.addRule(ALIGN_PARENT_LEFT);
            }
        }else {
            layoutParams.rightMargin = mXMargin;
            if (mIsAlignMenuButton){
                layoutParams.addRule(LEFT_OF,mMenuButton.getId());
            }else {
                layoutParams.addRule(ALIGN_PARENT_RIGHT);
            }
        }
        if (mMarginOrientationY == MARGIN_TOP){
            layoutParams.topMargin = mYMargin;
            if (mIsAlignMenuButton){
                layoutParams.addRule(BELOW,mMenuButton.getId());
            }else {
                layoutParams.addRule(ALIGN_PARENT_TOP);
            }
        }else {
            layoutParams.bottomMargin = mYMargin;
            if (mIsAlignMenuButton){
                layoutParams.addRule(ABOVE,mMenuButton.getId());
            }else {
                layoutParams.addRule(ALIGN_PARENT_BOTTOM);
            }
        }
        mOptionButton.setLayoutParams(layoutParams);
    }


}
