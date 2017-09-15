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

public class Circle8YMenuView extends YMenu{
    //8个Option位置的x、y乘积
    private float[] xyTimes = {0,0.707f,1,0.707f,0,-0.707f,-1,-0.707f};

    public Circle8YMenuView(Context context) {
        super(context);
    }

    public Circle8YMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Circle8YMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setMenuPosition(View menuButton) {
        new MenuPositionBuilder(menuButton)
                .setWidthAndHeight(getYMenuButtonWidth(),getYMenuButtonHeight())
                .setMarginOrientation(PositionBuilder.MARGIN_RIGHT,PositionBuilder.MARGIN_BOTTOM)
                .setIsXYCenter(true,true)
                .setXYMargin(getYMenuToParentXMargin(),getYMenuToParentYMargin())
                .finish();
    }

    @Override
    public void setOptionPosition(OptionButton2 optionButton, View menuButton, int index) {
        if (index >= 8){
            try {
                throw new Exception("Circle8YMenuView的OptionPosition最大数量为8，超过将会发生错误");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //计算OptionButton的位置
        int centerX = menuButton.getLeft() + menuButton.getWidth()/2;
        int centerY = menuButton.getTop() + menuButton.getHeight()/2;
        int halfOptionWidth = getYOptionButtonWidth()/2;
        int halfOptionHeight = getYOptionButtonHeight()/2;
        float x = xyTimes[index % 8];
        float y = xyTimes[(index + 6) % 8];

        Log.d("ymenuview", "setOptionPosition:x " + x);

        OptionPositionBuilder OptionPositionBuilder = new OptionPositionBuilder(optionButton,menuButton);
        OptionPositionBuilder
                .isAlignMenuButton(false)
                .setWidthAndHeight(getYOptionButtonWidth(), getYOptionButtonHeight())
                .setMarginOrientation(PositionBuilder.MARGIN_LEFT,PositionBuilder.MARGIN_TOP)
                .setXYMargin(
                        (int)(centerX + x * getYOptionXMargin() - halfOptionWidth)
                        ,(int)(centerY + y * getYOptionYMargin() - halfOptionHeight)
                )
                .finish();
    }

    @Override
    public Animation createOptionShowAnimation(OptionButton2 optionButton, int index) {
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation translateAnimation= new TranslateAnimation(
                getYMenuButton().getX() - optionButton.getX()
                ,0
                ,getYMenuButton().getY() - optionButton.getY()
                ,0);
        translateAnimation.setDuration(getOptionSD_AnimationDuration());
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(getOptionSD_AnimationDuration());
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(translateAnimation);
        animationSet.setStartOffset(60*index);
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
