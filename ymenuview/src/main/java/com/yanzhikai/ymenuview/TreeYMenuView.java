package com.yanzhikai.ymenuview;

import android.content.Context;
import android.util.AttributeSet;
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

public class TreeYMenuView extends YMenu {
    private static final float[] xTimes = {-1,1,0,-2,-2,2,2,-1,1};
    private static final float[] yTimes = {-1,-1,-2,0,-2,0,-2,-3,-3};

    public TreeYMenuView(Context context) {
        super(context);
    }

    public TreeYMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TreeYMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setMenuPosition(View menuButton) {
        new MenuPositionBuilder(menuButton)
                .setWidthAndHeight(getYMenuButtonWidth(), getYMenuButtonHeight())
                .setMarginOrientation(PositionBuilder.MARGIN_RIGHT,PositionBuilder.MARGIN_BOTTOM)
                .setIsXYCenter(true,false)
                .setXYMargin(getYMenuToParentXMargin(),getYMenuToParentYMargin())
                .finish();
    }

    @Override
    public void setOptionPosition(OptionButton2 optionButton, View menuButton, int index) {
        if (index > 8){
            try {
                throw new Exception("TreeYMenuView的OptionPosition最大数量为8，超过将会发生错误");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        float x = xTimes[index];
        float y = yTimes[index];
        OptionPositionBuilder OptionPositionBuilder = new OptionPositionBuilder(optionButton,menuButton);
        OptionPositionBuilder
                .isAlignMenuButton(false)
                .setWidthAndHeight(getYOptionButtonWidth(), getYOptionButtonHeight())
                .setMarginOrientation(PositionBuilder.MARGIN_LEFT,PositionBuilder.MARGIN_TOP)
                .setXYMargin(
                        (int)(x * getYOptionXMargin() + getYMenuButton().getLeft())
                        ,(int)(y * getYOptionYMargin() + getYMenuButton().getTop())
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
        }else {
            int oldIndex = (index - 3) / 2;
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

        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation translateAnimation= new TranslateAnimation(
                0
                ,getYMenuButton().getX() - optionButton.getX()
                ,0
                ,getYMenuButton().getY() - optionButton.getY()
        );
        translateAnimation.setDuration(getOptionSD_AnimationDuration());
        AlphaAnimation alphaAnimation = new AlphaAnimation(1,0);
        alphaAnimation.setDuration(getOptionSD_AnimationDuration());
//        animationSet.addAnimation(YAnimationUtils.reverseAlphaAnimation(alphaAnimation));
        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(alphaAnimation);
//        return YAnimationUtils.reverseAnimation(optionButton.getShowAnimation());
        animationSet.setStartOffset(60*(getOptionPositionCount() - index));
        return animationSet;
    }
}
