package com.yanzhikai.ymenuview.YMenuSettings;

import android.view.View;
import android.view.animation.Animation;

import com.yanzhikai.ymenuview.OptionButton2;
import com.yanzhikai.ymenuview.YMenuView2;

/**
 * Created by yany on 2017/9/12.
 */

public abstract class YMenuSetting {

    protected YMenuView2 yMenuView;

    public YMenuSetting (YMenuView2 yMenuView){
        this.yMenuView = yMenuView;
    }

    public abstract  void setMenuPosition(View menuButton);

    public abstract void setOptionPosition(OptionButton2 optionButton, View menuButton, int index);

    public abstract Animation setOptionShowAnimation(OptionButton2 optionButton,int duration,int index);
    public abstract Animation setOptionDisappearAnimation(OptionButton2 optionButton,int duration,int index);
}
