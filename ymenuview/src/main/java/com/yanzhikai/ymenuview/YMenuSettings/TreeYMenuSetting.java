package com.yanzhikai.ymenuview.YMenuSettings;

import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.yanzhikai.ymenuview.OptionButton2;
import com.yanzhikai.ymenuview.PositionBuilders.MenuPositionBuilder;
import com.yanzhikai.ymenuview.PositionBuilders.OptionPositionBuilder;
import com.yanzhikai.ymenuview.PositionBuilders.PositionBuilder;
import com.yanzhikai.ymenuview.YMenuView2;

/**
 * Created by yany on 2017/9/14.
 */

public class TreeYMenuSetting extends YMenuSetting {
    private static final int[] xTimes = {-1,1,0,-3,-2,3,2,-1};
    private static final int[] yTimes = {-1,-1,-2,-1,-2,-1,-2,-3};

    public TreeYMenuSetting(YMenuView2 yMenuView) {
        super(yMenuView);
    }

    @Override
    public void setMenuPosition(View menuButton) {
        Log.d("layoutParams", "setMenuPosition: ");
        new MenuPositionBuilder(menuButton)
                .setWidthAndHeight(yMenuView.getYMenuButtonWidth(), yMenuView.getYMenuButtonHeight())
                .setMarginOrientation(PositionBuilder.MARGIN_RIGHT,PositionBuilder.MARGIN_BOTTOM)
                .setIsXYCenter(true,false)
                .setXYMargin(yMenuView.getYMenuToParentXMargin(),yMenuView.getYMenuToParentYMargin())
                .finish();
    }

    @Override
    public void setOptionPosition(OptionButton2 optionButton, View menuButton, int index) {
        int x = xTimes[index];
        int y = yTimes[index];
        OptionPositionBuilder OptionPositionBuilder = new OptionPositionBuilder(optionButton,menuButton);
        OptionPositionBuilder
                .isAlignMenuButton(false)
                .setWidthAndHeight(yMenuView.getYOptionButtonWidth(), yMenuView.getYOptionButtonHeight())
                .setMarginOrientation(PositionBuilder.MARGIN_LEFT,PositionBuilder.MARGIN_TOP)
                .setXYMargin(
                        x * yMenuView.getYOptionXMargin() + yMenuView.getYMenuButton().getLeft()
                        ,y * yMenuView.getYOptionYMargin() + yMenuView.getYMenuButton().getTop()
                )
                .finish();
    }



    @Override
    public Animation setOptionShowAnimation(OptionButton2 optionButton, int duration,int index) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(duration);
        return alphaAnimation;
    }

    @Override
    public Animation setOptionDisappearAnimation(OptionButton2 optionButton, int duration,int index) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1,0);
        alphaAnimation.setDuration(duration);
        return alphaAnimation;
    }
}
