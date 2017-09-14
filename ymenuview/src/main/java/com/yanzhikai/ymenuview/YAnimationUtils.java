package com.yanzhikai.ymenuview;

import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by yany on 2017/9/14.
 */

public class YAnimationUtils {
    public static final String TAG = "ymenuview";
    public static Animation reverseAnimation(Animation animation) {
        if (animation instanceof TranslateAnimation) {
            return reverseTranslateAnimation((TranslateAnimation) animation);
        }
        if (animation instanceof AlphaAnimation){
            return reverseAlphaAnimation((AlphaAnimation) animation);
        }
        if (animation instanceof AnimationSet){
            return reverseAnimationSet((AnimationSet) animation);
        }
        return null;
    }

    public static TranslateAnimation reverseTranslateAnimation(TranslateAnimation translateAnimation) {
        Class animationClass = translateAnimation.getClass();
        Field[] fields = animationClass.getDeclaredFields();
        float fromXValue = 0, toXValue = 0, fromYValue = 0, toYValue = 0;
        try {
            for (Field field : fields) {
                field.setAccessible(true);

                switch (field.getName()) {
                    case "mFromXValue":
                        fromXValue = (float) field.get(translateAnimation);
                        break;
                    case "mToXValue":
                        toXValue = (float) field.get(translateAnimation);
                        break;
                    case "mFromYValue":
                        fromYValue = (float) field.get(translateAnimation);
                        break;
                    case "mToYValue":
                        toYValue = (float) field.get(translateAnimation);
                        break;
                }

                Log.d("ymenuview", "reverseAnimation: " + "name:" + field.getName() + "\t value = " + field.get(translateAnimation));
//            String type = field.getType().toString();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        TranslateAnimation newTranslateAnimation = new TranslateAnimation(toXValue, fromXValue, toYValue, fromYValue);
        Log.d("ymenuview", "reverseTranslateAnimation: toXValue " + toXValue + "fromXDelta" + fromXValue);
        newTranslateAnimation.setDuration(translateAnimation.getDuration());
        newTranslateAnimation.setInterpolator(translateAnimation.getInterpolator());
        return newTranslateAnimation;
    }

    public static AlphaAnimation reverseAlphaAnimation(AlphaAnimation alphaAnimation) {
        Class animationClass = alphaAnimation.getClass();
        Field[] fields = animationClass.getDeclaredFields();
        float fromAlpha = 0, toAlpha = 0;
        try {
            for (Field field : fields) {
                field.setAccessible(true);

                switch (field.getName()) {
                    case "mFromAlpha":
                        fromAlpha = (float) field.get(alphaAnimation);
                        break;
                    case "mToAlpha":
                        toAlpha = (float) field.get(alphaAnimation);
                        break;
                }

                Log.d("ymenuview", "reverseAnimation: " + "name:" + field.getName() + "\t value = " + field.get(alphaAnimation));
//            String type = field.getType().toString();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        AlphaAnimation newAlphaAnimation = new AlphaAnimation(toAlpha,fromAlpha);
        newAlphaAnimation.setDuration(alphaAnimation.getDuration());
        newAlphaAnimation.setInterpolator(alphaAnimation.getInterpolator());
        return newAlphaAnimation;
    }

    public static AnimationSet reverseAnimationSet(AnimationSet animationSet) {
        ArrayList<Animation> animations = (ArrayList<Animation>) animationSet.getAnimations();
        Class animationClass = animationSet.getClass();
        Field[] fields = animationClass.getDeclaredFields();
        //这里先手动设置true
        AnimationSet newAnimationSet = new AnimationSet(true);
        for (Animation animation: animations){
            newAnimationSet.addAnimation(reverseAnimation(animation));
        }
        return newAnimationSet;
    }
}
