package com.yanzhikai.ymenuview;

import android.content.Context;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;

import com.yanzhikai.ymenuview.YMenuSettings.YMenuSetting;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author yanzhikai
 * Description: 这个是菜单弹出的选项按钮，在这里主要对其进行进出动画的设置。
 */

public class OptionButton2 extends android.support.v7.widget.AppCompatImageView implements YMenuView2.OnShowDisappearListener{
    private Animation showAnimation,disappearAnimation;
    public static final int FROM_BUTTON_LEFT = 0 , FROM_BUTTON_TOP = 1,FROM_RIGHT = 2,FROM_BOTTOM = 3;
    private @SD_Animation int mSD_Animation = FROM_BUTTON_LEFT;
    private int mDuration = 600;
    private int mIndex;

    private YMenuSetting mSetting;

    @IntDef({FROM_BUTTON_LEFT, FROM_BUTTON_TOP,FROM_RIGHT,FROM_BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public  @interface SD_Animation {}

    public OptionButton2(Context context,YMenuSetting yMenuSetting,int index) {
        super(context);
        mSetting = yMenuSetting;
        mIndex = index;
        init();
    }

    public OptionButton2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OptionButton2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        setClickable(true);

        //在获取到宽高参数之后再进行初始化
        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (getX() != 0 && getY() != 0 && getWidth() != 0 && getHeight() != 0) {
                    setShowAndDisappear();
                    //设置完后立刻注销，不然会不断回调，浪费很多资源
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

            }
        });

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        super.onLayout(changed, left, top, right, bottom);
    }

    /*
     * 此方法需要在View初始化之前使用
     */
    public void setSD_Animation(@SD_Animation int sd_animation){
        mSD_Animation = sd_animation;
    }

    /*
     * 此方法需要在View初始化之前使用
     */
    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    private void setShowAndDisappear() {
        setShowAnimation(mDuration);
        setDisappearAnimation(mDuration);
        //在这里才设置Gone很重要，让View可以一开始就触发onGlobalLayout()进行初始化
        setVisibility(GONE);
    }

    public void setShowAnimation(int duration) {
        //获取父ViewGroup的对象，用于获取宽高参数
//        YMenuView2 parent = (YMenuView2) getParent();
//        showAnimation = new AnimationSet(true);
//        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
//        alphaAnimation.setDuration(duration);
//        TranslateAnimation translateAnimation = new TranslateAnimation(0,0,0,0);
//        switch (mSD_Animation){
//            //从MenuButton的左边移入
//            case FROM_BUTTON_LEFT:
//                translateAnimation= new TranslateAnimation(parent.getYMenuButton().getX() - getRight(),0
//                        ,0,0);
//                translateAnimation.setDuration(duration);
//                break;
//            case FROM_RIGHT:
//                //从右边缘移入
//                translateAnimation= new TranslateAnimation((parent.getWidth() - getX()),0,0,0);
//                translateAnimation.setDuration(duration);
////                showAnimation.setInterpolator(new OvershootInterpolator(1.3f));
//                break;
//            case FROM_BUTTON_TOP:
//                //从MenuButton的上边缘移入
//                translateAnimation= new TranslateAnimation(0,0,
//                        parent.getYMenuButton().getY() - getBottom(),0);
//                translateAnimation.setDuration(duration);
//                break;
//            case FROM_BOTTOM:
//                //从下边缘移入
//                translateAnimation = new TranslateAnimation(0,0,parent.getHeight() - getY(),0);
//                translateAnimation.setDuration(duration);
//        }
//
//        showAnimation.addAnimation(translateAnimation);
//        showAnimation.addAnimation(alphaAnimation);
        showAnimation = mSetting.setOptionShowAnimation(this,duration,mIndex);
    }

    public void setDisappearAnimation(int duration) {
        //获取父ViewGroup的对象，用于获取宽高参数
//        YMenuView2 parent = (YMenuView2) getParent();
//        disappearAnimation = new AnimationSet(true);
//        AlphaAnimation alphaAnimation = new AlphaAnimation(1,0);
//        alphaAnimation.setDuration(duration);
//        TranslateAnimation translateAnimation = new TranslateAnimation(0,0,0,0);
//        switch (mSD_Animation) {
//            case FROM_BUTTON_LEFT:
//                //从MenuButton的左边移入
//                translateAnimation= new TranslateAnimation(0,parent.getYMenuButton().getX() - getRight()
//                        ,0,0);
//                translateAnimation.setDuration(duration);
//                break;
//            case FROM_RIGHT:
//                //从右边缘移出
//                translateAnimation = new TranslateAnimation(0, (parent.getWidth()- getX()),
//                        0, 0);
//                translateAnimation.setDuration(duration);
//                break;
//            case FROM_BUTTON_TOP:
//                //从MenuButton的上边移入
//                translateAnimation = new TranslateAnimation(0, 0,
//                        0, parent.getYMenuButton().getY() - getBottom());
//                translateAnimation.setDuration(duration);
//                break;
//            case FROM_BOTTOM:
//                //从下边缘移出
//                translateAnimation = new TranslateAnimation(0,0,0,parent.getHeight() - getY());
//                translateAnimation.setDuration(duration);
//        }
//
//        disappearAnimation.addAnimation(translateAnimation);
//        disappearAnimation.addAnimation(alphaAnimation);
        disappearAnimation = mSetting.setOptionDisappearAnimation(this,duration,mIndex);
        disappearAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public int getmSD_Animation() {
        return mSD_Animation;
    }

    public Animation getShowAnimation() {
        return showAnimation;
    }

    public Animation getDisappearAnimation() {
        return disappearAnimation;
    }

    @Override
    public void onShow() {
        setVisibility(VISIBLE);
        if (showAnimation != null) {
            startAnimation(showAnimation);
        }
    }

    @Override
    public void onClose() {
        if (disappearAnimation != null) {
            startAnimation(disappearAnimation);
        }
    }

    @Override
    public void onDisappear(){
        setVisibility(GONE);
    }
}
