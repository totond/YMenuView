package com.yanzhikai.ymenuview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

import com.yanzhikai.ymenuview.PositionBuilders.MenuPositionBuilder;
import com.yanzhikai.ymenuview.PositionBuilders.OptionPositionBuilder;
import com.yanzhikai.ymenuview.PositionBuilders.PositionBuilder;

/**
 * Created by yany on 2017/9/15.
 */

public class SquareYMenuView extends YMenu {
    private static final int[] xTimes = {-1,-1,0,-2,-2,-1,-2,0};
    private static final int[] yTimes = {-1,0,-1,-2,-1,-2,0,-2};

    public SquareYMenuView(Context context) {
        super(context);
    }

    public SquareYMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareYMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void setMenuPosition(View menuButton) {
        new MenuPositionBuilder(menuButton)
                .setWidthAndHeight(getYMenuButtonWidth(), getYMenuButtonHeight())
                .setMarginOrientation(PositionBuilder.MARGIN_RIGHT,PositionBuilder.MARGIN_BOTTOM)
                .setIsXYCenter(false,false)
                .setXYMargin(getYMenuToParentXMargin(),getYMenuToParentYMargin())
                .finish();
    }

    @Override
    public void setOptionPosition(OptionButton2 optionButton, View menuButton, int index) {
        if (index > 7){
            try {
                throw new Exception("SquareYMenuView的OptionPosition不能大于8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int x = xTimes[index];
        int y = yTimes[index];

        OptionPositionBuilder OptionPositionBuilder = new OptionPositionBuilder(optionButton,menuButton);
        OptionPositionBuilder
                .isAlignMenuButton(false)//是否以MenuButton为参考
                .setWidthAndHeight(getYOptionButtonWidth(), getYOptionButtonHeight())
                .setMarginOrientation(PositionBuilder.MARGIN_LEFT,PositionBuilder.MARGIN_TOP)
                .setXYMargin(
                        x * (getYOptionXMargin() + getYOptionButtonWidth()) + getYMenuButton().getLeft()
                        ,y * (getYOptionYMargin() + getYOptionButtonHeight()) + getYMenuButton().getTop()
                )
                .finish();
    }

    @Override
    public Animation createOptionShowAnimation(OptionButton2 optionButton, int index) {
        float fromX,fromY;
        AnimationSet animationSet = new AnimationSet(true);
        if (index < 3){
            fromX = getYMenuButton().getX() - optionButton.getX();
            fromY = getYMenuButton().getY() - optionButton.getY();
        }else if (index < 6){

            fromX = getOptionButtonList().get(0).getX() - optionButton.getX();
            fromY = getOptionButtonList().get(0).getY() - optionButton.getY();
            animationSet.setStartOffset(getOptionSD_AnimationDuration());
        }else {
            int oldIndex = index % 5;
            Log.d(TAG, "createOptionShowAnimation: oldIndex" + oldIndex);
            fromX = getOptionButtonList().get(oldIndex).getX() - optionButton.getX();
            fromY = getOptionButtonList().get(oldIndex).getY() - optionButton.getY();
            animationSet.setStartOffset(getOptionSD_AnimationDuration());
        }

        TranslateAnimation translateAnimation= new TranslateAnimation(
                fromX
                ,0
                ,fromY
                ,0);
        translateAnimation.setDuration(getOptionSD_AnimationDuration());
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(getOptionSD_AnimationDuration());
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(translateAnimation);

        return animationSet;

    }

    @Override
    public Animation createOptionDisappearAnimation(OptionButton2 optionButton, int index) {
        float toX,toY;
        AnimationSet animationSet = new AnimationSet(true);
        if (index < 3){
            toX = getYMenuButton().getX() - optionButton.getX();
            toY = getYMenuButton().getY() - optionButton.getY();
            animationSet.setStartOffset(getOptionSD_AnimationDuration());
        }else if (index < 6){
            toX = getOptionButtonList().get(0).getX() - optionButton.getX();
            toY = getOptionButtonList().get(0).getY() - optionButton.getY();
        }else {
            int oldIndex = index % 5;
            Log.d(TAG, "createOptionShowAnimation: oldIndex" + oldIndex);
            toX = getOptionButtonList().get(oldIndex).getX() - optionButton.getX();
            toY = getOptionButtonList().get(oldIndex).getY() - optionButton.getY();

        }

        TranslateAnimation translateAnimation= new TranslateAnimation(
                0
                ,toX
                ,0
                ,toY);
        translateAnimation.setDuration(getOptionSD_AnimationDuration());
        AlphaAnimation alphaAnimation = new AlphaAnimation(1,0);
        alphaAnimation.setDuration(getOptionSD_AnimationDuration());
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(translateAnimation);

        return animationSet;
    }
}
