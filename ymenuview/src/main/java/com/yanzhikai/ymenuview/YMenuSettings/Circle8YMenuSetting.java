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
import com.yanzhikai.ymenuview.YAnimationUtils;
import com.yanzhikai.ymenuview.YMenuView2;

/**
 * Created by yany on 2017/9/13.
 */

public class Circle8YMenuSetting extends YMenuSetting {
    //8个Option位置的x、y乘积
    private float[] xyTimes = {0,0.707f,1,0.707f,0,-0.707f,-1,-0.707f};

    public Circle8YMenuSetting(YMenuView2 yMenuView) {
        super(yMenuView);
    }

    @Override
    public void setMenuPosition(View menuButton) {
        new MenuPositionBuilder(menuButton)
                .setWidthAndHeight(yMenuView.getYMenuButtonWidth(), yMenuView.getYMenuButtonHeight())
                .setMarginOrientation(PositionBuilder.MARGIN_RIGHT,PositionBuilder.MARGIN_BOTTOM)
                .setIsXYCenter(true,true)
                .setXYMargin(yMenuView.getYMenuToParentXMargin(),yMenuView.getYMenuToParentYMargin())
                .finish();
    }

    @Override
    public void setOptionPosition(OptionButton2 optionButton, View menuButton, int index) {
        if (index >= 8){
            try {
                throw new Exception("OptionPosition最大数量为8，超过将会发生错误");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //计算OptionButton的位置
        int centerX = menuButton.getLeft() + menuButton.getWidth()/2;
        int centerY = menuButton.getTop() + menuButton.getHeight()/2;
        int halfOptionWidth = yMenuView.getYOptionButtonWidth()/2;
        int halfOptionHeight = yMenuView.getYOptionButtonHeight()/2;
        float x = xyTimes[index % 8];
        float y = xyTimes[(index + 6) % 8];

        Log.d("ymenuview", "setOptionPosition:x " + x);

        OptionPositionBuilder OptionPositionBuilder = new OptionPositionBuilder(optionButton,menuButton);
        OptionPositionBuilder
                .isAlignMenuButton(false)
                .setWidthAndHeight(yMenuView.getYOptionButtonWidth(), yMenuView.getYOptionButtonHeight())
                .setMarginOrientation(PositionBuilder.MARGIN_LEFT,PositionBuilder.MARGIN_TOP)
                .setXYMargin(
                        (int)(centerX + x * yMenuView.getYOptionXMargin() - halfOptionWidth)
                        ,(int)(centerY + y * yMenuView.getYOptionYMargin() - halfOptionHeight)
                        )
                .finish();
    }





    @Override
    public Animation setOptionShowAnimation(OptionButton2 optionButton, int duration,int index) {
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation translateAnimation= new TranslateAnimation(
                yMenuView.getYMenuButton().getX() - optionButton.getX()
                ,0
                ,yMenuView.getYMenuButton().getY() - optionButton.getY()
                ,0);
        translateAnimation.setDuration(duration);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(duration);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(translateAnimation);
        return animationSet;
    }

    @Override
    public Animation setOptionDisappearAnimation(OptionButton2 optionButton, int duration,int index) {
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation translateAnimation= new TranslateAnimation(
                0
                ,yMenuView.getYMenuButton().getX() - optionButton.getX()
                ,0
                ,yMenuView.getYMenuButton().getY() - optionButton.getY()
                );
        translateAnimation.setDuration(duration);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1,0);
        alphaAnimation.setDuration(duration);
//        animationSet.addAnimation(YAnimationUtils.reverseAlphaAnimation(alphaAnimation));
        animationSet.addAnimation(translateAnimation);
        return YAnimationUtils.reverseAnimation(optionButton.getShowAnimation());
//        return animationSet;
    }
}
