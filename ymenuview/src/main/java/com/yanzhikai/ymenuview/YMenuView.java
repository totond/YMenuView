package com.yanzhikai.ymenuview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import com.yanzhikai.ymenuview.PositionBuilders.MenuPositionBuilder;
import com.yanzhikai.ymenuview.PositionBuilders.OptionPositionBuilder;
import com.yanzhikai.ymenuview.PositionBuilders.PositionBuilder;

import static com.yanzhikai.ymenuview.OptionButton2.FROM_BOTTOM;
import static com.yanzhikai.ymenuview.OptionButton2.FROM_BUTTON_LEFT;
import static com.yanzhikai.ymenuview.OptionButton2.FROM_BUTTON_TOP;
import static com.yanzhikai.ymenuview.OptionButton2.FROM_RIGHT;

/**
 * @author Yanzhikai
 * Description: 一个可以弹出收回菜单栏的自定义View，带有动画效果
 */

public class YMenuView extends YMenu implements OptionButton2.OptionPrepareListener{
    public final static String TAG = "ymenuview";

    public YMenuView(Context context) {
        super(context);
    }

    public YMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public YMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setMenuPosition(View menuButton){
        new MenuPositionBuilder(menuButton)
                .setWidthAndHeight(getYMenuButtonWidth(), getYMenuButtonHeight())
                .setMarginOrientation(PositionBuilder.MARGIN_RIGHT,PositionBuilder.MARGIN_BOTTOM)
                .setIsXYCenter(false,false)
                .setXYMargin(getYMenuToParentXMargin(),getYMenuToParentYMargin())
                .finish();


//        LayoutParams layoutParams = new LayoutParams(getYMenuButtonWidth(), getYMenuButtonHeight());
//        layoutParams.setMarginEnd(getYMenuToParentXMargin());
//        layoutParams.bottomMargin = getYMenuToParentYMargin();
//        layoutParams.addRule(ALIGN_PARENT_RIGHT);
//        layoutParams.addRule(ALIGN_PARENT_BOTTOM);
//        menuButton.setLayoutParams(layoutParams);
    }


    @Override
    public void setOptionPosition(OptionButton2 optionButton, View menuButton, int index){
        Log.d(TAG, "setOptionPosition: " + menuButton.getX());
        //设置动画模式和时长
        optionButton.setSD_Animation(getOptionSD_AnimationMode());
        optionButton.setDuration(getOptionSD_AnimationDuration());

        //计算OptionButton的位置
        int position = index % getOptionColumns();

        OptionPositionBuilder OptionPositionBuilder = new OptionPositionBuilder(optionButton,menuButton);
        OptionPositionBuilder
                .isAlignMenuButton(false)
                .setWidthAndHeight(getYOptionButtonWidth(), getYOptionButtonHeight())
                .setMarginOrientation(PositionBuilder.MARGIN_RIGHT,PositionBuilder.MARGIN_BOTTOM)
                .setXYMargin(
                        getYOptionToParentXMargin()
                                + getYOptionXMargin() * position
                                + getYOptionButtonWidth() * position,
                        getYOptionToParentYMargin()
                                + (getYOptionButtonHeight() + getYOptionYMargin())
                                * (index / getOptionColumns()))
                .finish();

//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
//                getYOptionButtonWidth()
//                , getYOptionButtonHeight());
//
//        layoutParams.rightMargin = getYOptionToParentXMargin()
//                + getYOptionXMargin() * position
//                + getYOptionButtonWidth() * position;
//
//        layoutParams.bottomMargin = getYOptionToParentYMargin()
//                + (getYOptionButtonHeight() + getYOptionYMargin())
//                * (index / getOptionColumns());
//        layoutParams.addRule(ALIGN_PARENT_BOTTOM);
//        layoutParams.addRule(ALIGN_PARENT_RIGHT);
//
//        optionButton.setLayoutParams(layoutParams);
    }

    @Override
    public Animation createOptionShowAnimation(OptionButton2 optionButton,int index){
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(getOptionSD_AnimationDuration());
        TranslateAnimation translateAnimation = new TranslateAnimation(0,0,0,0);
        switch (optionButton.getmSD_Animation()){
            //从MenuButton的左边移入
            case FROM_BUTTON_LEFT:
                translateAnimation= new TranslateAnimation(getYMenuButton().getX() - optionButton.getRight(),0
                        ,0,0);
                translateAnimation.setDuration(getOptionSD_AnimationDuration());
                break;
            case FROM_RIGHT:
                //从右边缘移入
                translateAnimation= new TranslateAnimation((getWidth() - optionButton.getX()),0,0,0);
                translateAnimation.setDuration(getOptionSD_AnimationDuration());
//                showAnimation.setInterpolator(new OvershootInterpolator(1.3f));
                break;
            case FROM_BUTTON_TOP:
                //从MenuButton的上边缘移入
                translateAnimation= new TranslateAnimation(0,0,
                        getYMenuButton().getY() - optionButton.getBottom(),0);
                translateAnimation.setDuration(getOptionSD_AnimationDuration());
                break;
            case FROM_BOTTOM:
                //从下边缘移入
                translateAnimation = new TranslateAnimation(0,0,getHeight() - optionButton.getY(),0);
                translateAnimation.setDuration(getOptionSD_AnimationDuration());
        }

        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(alphaAnimation);
        return animationSet;
    }

    @Override
    public Animation createOptionDisappearAnimation(OptionButton2 optionButton,int index){
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1,0);
        alphaAnimation.setDuration(getOptionSD_AnimationDuration());
        TranslateAnimation translateAnimation = new TranslateAnimation(0,0,0,0);
        switch (optionButton.getmSD_Animation()) {
            case FROM_BUTTON_LEFT:
                //从MenuButton的左边移入
                translateAnimation= new TranslateAnimation(0,getYMenuButton().getX() - optionButton.getRight()
                        ,0,0);
                translateAnimation.setDuration(getOptionSD_AnimationDuration());
                break;
            case FROM_RIGHT:
                //从右边缘移出
                translateAnimation = new TranslateAnimation(0, (getWidth()- optionButton.getX()),
                        0, 0);
                translateAnimation.setDuration(getOptionSD_AnimationDuration());
                break;
            case FROM_BUTTON_TOP:
                //从MenuButton的上边移入
                translateAnimation = new TranslateAnimation(0, 0,
                        0, getYMenuButton().getY() - optionButton.getBottom());
                translateAnimation.setDuration(getOptionSD_AnimationDuration());
                break;
            case FROM_BOTTOM:
                //从下边缘移出
                translateAnimation = new TranslateAnimation(0,0,0,getHeight() - optionButton.getY());
                translateAnimation.setDuration(getOptionSD_AnimationDuration());
        }

        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(alphaAnimation);
        return animationSet;
    }







}
