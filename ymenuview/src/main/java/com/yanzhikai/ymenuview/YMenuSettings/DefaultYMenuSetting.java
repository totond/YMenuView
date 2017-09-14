package com.yanzhikai.ymenuview.YMenuSettings;

import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

import com.yanzhikai.ymenuview.OptionButton2;
import com.yanzhikai.ymenuview.PositionBuilders.MenuPositionBuilder;
import com.yanzhikai.ymenuview.PositionBuilders.OptionPositionBuilder;
import com.yanzhikai.ymenuview.PositionBuilders.PositionBuilder;
import com.yanzhikai.ymenuview.YMenuView2;

import static com.yanzhikai.ymenuview.OptionButton2.FROM_BOTTOM;
import static com.yanzhikai.ymenuview.OptionButton2.FROM_BUTTON_LEFT;
import static com.yanzhikai.ymenuview.OptionButton2.FROM_BUTTON_TOP;
import static com.yanzhikai.ymenuview.OptionButton2.FROM_RIGHT;

/**
 * Created by yany on 2017/9/12.
 */

public class DefaultYMenuSetting extends YMenuSetting {

    public DefaultYMenuSetting(YMenuView2 yMenuView) {
        super(yMenuView);
    }

    @Override
    public void setMenuPosition(View menuButton) {
        new MenuPositionBuilder(menuButton)
                .setWidthAndHeight(yMenuView.getYMenuButtonWidth(), yMenuView.getYMenuButtonHeight())
                .setMarginOrientation(PositionBuilder.MARGIN_RIGHT,PositionBuilder.MARGIN_BOTTOM)
                .setIsXYCenter(true,false)
                .setXYMargin(yMenuView.getYMenuToParentXMargin(),yMenuView.getYMenuToParentYMargin())
                .finish();



//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(yMenuView.getYMenuButtonWidth(),
//                yMenuView.getYMenuButtonHeight());
//        layoutParams.setMarginEnd(yMenuView.getYMenuToParentXMargin());
//        layoutParams.bottomMargin = yMenuView.getYMenuToParentYMargin();
//        layoutParams.addRule(ALIGN_PARENT_RIGHT);
//        layoutParams.addRule(ALIGN_PARENT_BOTTOM);
//        menuButton.setLayoutParams(layoutParams);
    }

    @Override
    public void setOptionPosition(OptionButton2 optionButton, View menuButton, int index) {
        Log.d("ymenuview", "setOptionPosition: " + menuButton.getX());

        //设置动画模式和时长
        optionButton.setSD_Animation(yMenuView.getOptionSD_AnimationMode());
        optionButton.setDuration(yMenuView.getOptionSD_AnimationDuration());

        //计算OptionButton的位置
        int position = index % yMenuView.getOptionColumns();

        OptionPositionBuilder OptionPositionBuilder = new OptionPositionBuilder(optionButton,menuButton);
        OptionPositionBuilder
                .isAlignMenuButton(false)
                .setWidthAndHeight(yMenuView.getYOptionButtonWidth(), yMenuView.getYOptionButtonHeight())
                .setMarginOrientation(PositionBuilder.MARGIN_RIGHT,PositionBuilder.MARGIN_BOTTOM)
                .setXYMargin(
                        yMenuView.getYOptionToParentXMargin()
                                + yMenuView.getYOptionXMargin() * position
                                + yMenuView.getYOptionButtonWidth() * position,
                        yMenuView.getYOptionToParentYMargin()
                                + (yMenuView.getYOptionButtonHeight() + yMenuView.getYOptionYMargin())
                                * (index / yMenuView.getOptionColumns()))
                .finish();


//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
//                yMenuView.getYOptionButtonWidth()
//                , yMenuView.getYOptionButtonHeight());
//
//        layoutParams.rightMargin = yMenuView.getYOptionToParentXMargin()
//                + yMenuView.getYOptionXMargin() * position
//                + yMenuView.getYOptionButtonWidth() * position;
//
//        layoutParams.bottomMargin = yMenuView.getYOptionToParentYMargin()
//                + (yMenuView.getYOptionButtonHeight() + yMenuView.getYOptionYMargin())
//                * (index / yMenuView.getOptionColumns());
//        layoutParams.addRule(ALIGN_PARENT_BOTTOM);
//        layoutParams.addRule(ALIGN_PARENT_RIGHT);
//
//        optionButton.setLayoutParams(layoutParams);
    }



    @Override
    public Animation setOptionShowAnimation(OptionButton2 optionButton,int duration,int index) {
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(duration);
        TranslateAnimation translateAnimation = new TranslateAnimation(0,0,0,0);
        switch (optionButton.getmSD_Animation()){
            //从MenuButton的左边移入
            case FROM_BUTTON_LEFT:
                translateAnimation= new TranslateAnimation(yMenuView.getYMenuButton().getX() - optionButton.getRight(),0
                        ,0,0);
                translateAnimation.setDuration(duration);
                break;
            case FROM_RIGHT:
                //从右边缘移入
                translateAnimation= new TranslateAnimation((yMenuView.getWidth() - optionButton.getX()),0,0,0);
                translateAnimation.setDuration(duration);
//                showAnimation.setInterpolator(new OvershootInterpolator(1.3f));
                break;
            case FROM_BUTTON_TOP:
                //从MenuButton的上边缘移入
                translateAnimation= new TranslateAnimation(0,0,
                        yMenuView.getYMenuButton().getY() - optionButton.getBottom(),0);
                translateAnimation.setDuration(duration);
                break;
            case FROM_BOTTOM:
                //从下边缘移入
                translateAnimation = new TranslateAnimation(0,0,yMenuView.getHeight() - optionButton.getY(),0);
                translateAnimation.setDuration(duration);
        }

        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(alphaAnimation);
        return animationSet;
    }

    @Override
    public Animation setOptionDisappearAnimation(OptionButton2 optionButton, int duration,int index) {
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1,0);
        alphaAnimation.setDuration(duration);
        TranslateAnimation translateAnimation = new TranslateAnimation(0,0,0,0);
        switch (optionButton.getmSD_Animation()) {
            case FROM_BUTTON_LEFT:
                //从MenuButton的左边移入
                translateAnimation= new TranslateAnimation(0,yMenuView.getYMenuButton().getX() - optionButton.getRight()
                        ,0,0);
                translateAnimation.setDuration(duration);
                break;
            case FROM_RIGHT:
                //从右边缘移出
                translateAnimation = new TranslateAnimation(0, (yMenuView.getWidth()- optionButton.getX()),
                        0, 0);
                translateAnimation.setDuration(duration);
                break;
            case FROM_BUTTON_TOP:
                //从MenuButton的上边移入
                translateAnimation = new TranslateAnimation(0, 0,
                        0, yMenuView.getYMenuButton().getY() - optionButton.getBottom());
                translateAnimation.setDuration(duration);
                break;
            case FROM_BOTTOM:
                //从下边缘移出
                translateAnimation = new TranslateAnimation(0,0,0,yMenuView.getHeight() - optionButton.getY());
                translateAnimation.setDuration(duration);
        }

        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(alphaAnimation);
        return animationSet;
    }
}
