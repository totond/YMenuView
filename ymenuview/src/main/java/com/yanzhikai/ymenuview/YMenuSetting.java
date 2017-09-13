package com.yanzhikai.ymenuview;

import android.view.View;
import android.view.animation.Animation;

/**
 * Created by yany on 2017/9/12.
 */

public abstract class YMenuSetting {

    protected YMenuView2 yMenuView;

    public YMenuSetting (YMenuView2 yMenuView){
        this.yMenuView = yMenuView;
    }

    public abstract void setOptionPosition(OptionButton2 optionButton, View menuButton, int index);

    public abstract  void setMenuPosition(View menuButton);

    public abstract Animation setOptionShowAnimation(OptionButton2 optionButton,int duration);
    public abstract Animation setOptionDisappearAnimation(OptionButton2 optionButton,int duration);
}
