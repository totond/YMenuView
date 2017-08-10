package com.yanzhikai.ymenuview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Yanzhikai
 * 一个可以弹出收回菜单栏的自定义View，带有动画效果
 */

public class YMenuView extends RelativeLayout {
    public final static String TAG = "ymenuview";

    private Context mContext;
    private Button mYMenuButton;

    private int drawableIds[] = {R.drawable.zero,R.drawable.one, R.drawable.two, R.drawable.three,
            R.drawable.four, R.drawable.five,R.drawable.six,R.drawable.seven};
    private ArrayList<OptionButton> optionButtonList;
//    private ArrayList<OnShowDisappearListener> listenerList;
    private int optionPositionCount = 8;
    private int optionLines = 1;
    private int[] banArray = {};
    private int mYMenuButtonWidth = 80, mYMenuButtonHeight = 80;
    private int mYOptionButtonWidth = 80, mYOptionButtonHeight = 80;
    private int mYMenuButtonRightMargin = 50, mYMenuButtonBottomMargin = 50;
    private int mYOptionVerticalMargin = 15, mYOptionHorizontalMargin = 15;
    private int mYOptionToMenuBottomMargin = 160, mYOptionToMenuRightMargin = 50;
    private @DrawableRes int mMenuButtonBackGroundId = R.drawable.setting;
    private @DrawableRes int mOptionsBackGroundId = R.drawable.null_drawable;
    private boolean isShowMenu = false;
    private Animation menuOpenAnimation, menuCloseAnimation;
    private Animation.AnimationListener animationListener;
    private int mOptionSD_AnimationMode = OptionButton.FROM_BUTTON_TOP;
    private int mOptionSD_AnimationDuration = 600;
    private OnOptionsClickListener mOnOptionsClickListener;
    private ViewTreeObserver.OnPreDrawListener onPreDrawListener;

    public YMenuView(Context context) {
        super(context);
        init(context);
    }

    public YMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
        init(context);
    }

    public YMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        init(context);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.YMenuView, 0, 0);
        mYMenuButtonWidth = typedArray.getDimensionPixelSize(R.styleable.YMenuView_menuButtonWidth, mYMenuButtonWidth);
        mYMenuButtonHeight = typedArray.getDimensionPixelSize(R.styleable.YMenuView_menuButtonHeight, mYMenuButtonHeight);
        mYOptionButtonWidth = typedArray.getDimensionPixelSize(R.styleable.YMenuView_optionButtonWidth, mYOptionButtonWidth);
        mYOptionButtonHeight = typedArray.getDimensionPixelSize(R.styleable.YMenuView_optionButtonHeight, mYOptionButtonHeight);
        mYMenuButtonRightMargin = typedArray.getDimensionPixelSize(R.styleable.YMenuView_menuButtonRightMargin, mYMenuButtonRightMargin);
        mYMenuButtonBottomMargin = typedArray.getDimensionPixelSize(R.styleable.YMenuView_menuButtonBottomMargin, mYMenuButtonBottomMargin);
        optionPositionCount = typedArray.getInteger(R.styleable.YMenuView_optionPositionCounts, optionPositionCount);
        optionLines = typedArray.getInteger(R.styleable.YMenuView_optionLines,optionLines);
        mYOptionToMenuBottomMargin = typedArray.getDimensionPixelSize(R.styleable.YMenuView_optionToMenuBottomMargin, mYOptionToMenuBottomMargin);
        mYOptionToMenuRightMargin = typedArray.getDimensionPixelSize(R.styleable.YMenuView_optionToMenuRightMargin, mYOptionToMenuRightMargin);
        mYOptionVerticalMargin = typedArray.getDimensionPixelSize(R.styleable.YMenuView_optionVerticalMargin, mYOptionVerticalMargin);
        mYOptionHorizontalMargin = typedArray.getDimensionPixelSize(R.styleable.YMenuView_optionHorizontalMargin, mYOptionHorizontalMargin);
        mMenuButtonBackGroundId = typedArray.getResourceId(R.styleable.YMenuView_menuButtonBackGround, mMenuButtonBackGroundId);
        mOptionsBackGroundId = typedArray.getResourceId(R.styleable.YMenuView_optionsBackGround,R.drawable.null_drawable);
        mOptionSD_AnimationMode = typedArray.getInt(R.styleable.YMenuView_sd_animMode,mOptionSD_AnimationMode);
        mOptionSD_AnimationDuration = typedArray.getInt(R.styleable.YMenuView_sd_duration,mOptionSD_AnimationDuration);
    }

    private void init(Context context) {
        mContext = context;


        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        onPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                setMenuButton();
                try {
                    setOptionButtons();
                    setOptionBackGrounds(mOptionsBackGroundId);
                    setOptionsImages(drawableIds);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                getViewTreeObserver().removeOnPreDrawListener(this);
                return false;
            }
        };
        viewTreeObserver.addOnPreDrawListener(onPreDrawListener);
        initMenuAnim();
        initBan();
    }

    //初始化MenuButton的点击动画
    private void initMenuAnim() {
        menuOpenAnimation = AnimationUtils.loadAnimation(mContext, R.anim.rotate_open);
        menuCloseAnimation = AnimationUtils.loadAnimation(mContext, R.anim.rotate_close);
        animationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mYMenuButton.setClickable(false);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mYMenuButton.setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
        menuOpenAnimation.setDuration(mOptionSD_AnimationDuration);
        menuCloseAnimation.setDuration(mOptionSD_AnimationDuration);
        menuOpenAnimation.setAnimationListener(animationListener);
        menuCloseAnimation.setAnimationListener(animationListener);
    }


    private void initBan() {
        Arrays.sort(banArray);
    }

    private void setMenuButton() {
        mYMenuButton = new Button(mContext);
        //设置MenuButton的大小位置
        LayoutParams layoutParams = new LayoutParams(mYMenuButtonWidth, mYMenuButtonHeight);
        layoutParams.setMarginEnd(mYMenuButtonRightMargin);
        layoutParams.bottomMargin = mYMenuButtonBottomMargin;
        layoutParams.addRule(ALIGN_PARENT_RIGHT);
        layoutParams.addRule(ALIGN_PARENT_BOTTOM);
        //生成ID
        mYMenuButton.setId(generateViewId());

        mYMenuButton.setLayoutParams(layoutParams);
        mYMenuButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isShowMenu) {
                    showMenu();
                    if (menuOpenAnimation != null) {
                        mYMenuButton.startAnimation(menuOpenAnimation);
                    }
                } else {
                    closeMenu();
                    if (menuCloseAnimation != null) {
                        mYMenuButton.startAnimation(menuCloseAnimation);
                    }
                }
            }
        });
        mYMenuButton.setBackgroundResource(mMenuButtonBackGroundId);
        addView(mYMenuButton);
    }

    //设置选项按钮
    private void setOptionButtons() throws Exception {
        optionButtonList = new ArrayList<>(optionPositionCount);
//        listenerList = new ArrayList<>(optionPositionCount);


        boolean isBan = true;
        for (int i = 0,n = 0; i < optionPositionCount; i++) {
            if (isBan && banArray.length > 0) {
                //Ban判断
                if (i > banArray[n] || banArray[n] > optionPositionCount - 1) {
                    throw new Exception("Ban数组设置不合理，含有负数、重复数字或者超出范围");
                } else if (i == banArray[n]) {
                    if (n < banArray.length - 1) {
                        n++;
                    }else {
                        isBan = false;
                    }
                    continue;
                }
            }

            OptionButton button = new OptionButton(mContext);
            button.setSD_Animation(mOptionSD_AnimationMode);
            button.setDuration(mOptionSD_AnimationDuration);
            int btnId = generateViewId();
            button.setId(btnId);


            RelativeLayout.LayoutParams layoutParams = new LayoutParams(mYOptionButtonWidth, mYOptionButtonHeight);

            //计算OptionButton的位置
            int position = i % optionLines;
            layoutParams.setMarginEnd(mYOptionToMenuRightMargin
                    + mYOptionHorizontalMargin * position
                    + mYOptionButtonWidth * position);
            layoutParams.bottomMargin = mYOptionToMenuBottomMargin
                    + (mYOptionButtonHeight + mYOptionVerticalMargin) * (i / optionLines);
            layoutParams.addRule(ALIGN_PARENT_BOTTOM);
            layoutParams.addRule(ALIGN_PARENT_RIGHT);


            button.setLayoutParams(layoutParams);

            addView(button);

            optionButtonList.add(button);
//            listenerList.add(button);
        }
    }



    //设置选项按钮的background
    public void setOptionBackGrounds(@DrawableRes Integer drawableId){
        for (int i = 0; i < optionButtonList.size(); i++) {
            if (drawableId == null){
                optionButtonList.get(i).setBackground(null);
            }else {
                optionButtonList.get(i).setBackgroundResource(drawableId);
            }

        }

    }

    private void setOptionsImages(int... drawableIds) throws Exception {
        this.drawableIds = drawableIds;
        if (optionPositionCount > drawableIds.length + banArray.length) {
            throw new Exception("Drawable资源数量不足");
        }

        for (int i = 0; i < optionButtonList.size(); i++) {
            optionButtonList.get(i).setOnClickListener(new MyOnClickListener(i));
            if (drawableIds == null){
                optionButtonList.get(i).setImageDrawable(null);
            }else {
                optionButtonList.get(i).setImageResource(drawableIds[i]);
            }

        }
    }

    public void showMenu() {
        if (!isShowMenu) {
            for (OptionButton button : optionButtonList) {
                button.onShow();
            }
            isShowMenu = true;
        }
    }

    public void closeMenu() {
        if (isShowMenu) {
            for (OptionButton button : optionButtonList) {
                button.onClose();
            }
            isShowMenu = false;
        }
    }

    public void disappearMenu() {
        if (isShowMenu) {
            for (OptionButton button : optionButtonList) {
                button.onDisappear();
            }
            isShowMenu = false;
        }
    }


    //清除所有View，用于之后刷新
    private void cleanMenu(){
        removeAllViews();
        if (optionButtonList != null) {
            optionButtonList.clear();
        }
        isShowMenu = false;
        if (onPreDrawListener != null) {
            getViewTreeObserver().removeOnPreDrawListener(onPreDrawListener);
        }
    }

    /*
     * 对整个YMenuView进行重新初始化，用于在做完一些设定之后刷新
     */
    public void refresh(){
        cleanMenu();
        init(mContext);
    }

    public Button getYMenuButton() {
        return mYMenuButton;
    }

    public int getOptionLines() {
        return optionLines;
    }

    public int[] getDrawableIds() {
        return drawableIds;
    }

    public int getMenuButtonBackGroundId() {
        return mMenuButtonBackGroundId;
    }

    public int getOptionsBackGroundId() {
        return mOptionsBackGroundId;
    }

    public int getOptionSD_AnimationDuration() {
        return mOptionSD_AnimationDuration;
    }

    public int getOptionSD_AnimationMode() {
        return mOptionSD_AnimationMode;
    }

    public int getYMenuButtonBottomMargin() {
        return mYMenuButtonBottomMargin;
    }

    public int getYMenuButtonRightMargin() {
        return mYMenuButtonRightMargin;
    }

    public int getYMenuButtonWidth() {
        return mYMenuButtonWidth;
    }

    public int getYMenuButtonHeight() {
        return mYMenuButtonHeight;
    }

    public int getYOptionButtonWidth() {
        return mYOptionButtonWidth;
    }

    public int getYOptionButtonHeight() {
        return mYOptionButtonHeight;
    }

    public int getYOptionHorizontalMargin() {
        return mYOptionHorizontalMargin;
    }

    public int getYOptionVerticalMargin() {
        return mYOptionVerticalMargin;
    }

    public int getYOptionToMenuBottomMargin() {
        return mYOptionToMenuBottomMargin;
    }

    public int getYOptionToMenuRightMargin() {
        return mYOptionToMenuRightMargin;
    }

    public ArrayList<OptionButton> getOptionButtonList() {
        return optionButtonList;
    }


    public void setOnOptionsClickListener(OnOptionsClickListener onOptionsClickListener) {
        this.mOnOptionsClickListener = onOptionsClickListener;
    }

    private class MyOnClickListener implements OnClickListener {
        private int index;

        public MyOnClickListener(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View v) {
            if (mOnOptionsClickListener != null) {
                mOnOptionsClickListener.onOptionsClick(index);
            }
        }
    }


    /*
     * 下面的set方法需要在View还没有初始化的时候调用，例如Activity的onCreate方法里
     * 如果不在View还没初始化的时候调用，请使用完set这些方法之后调用refresh()方法刷新
     */

    //设置OptionButton的Drawable资源
    public void setOptionDrawableIds(int... drawableIds) {
        this.drawableIds = drawableIds;
    }

    //设置MenuButton弹出菜单选项时候MenuButton自身的动画，默认为顺时针旋转180度，为空则是关闭动画
    public void setMenuOpenAnimation(Animation menuOpenAnimation) {
        menuOpenAnimation.setAnimationListener(animationListener);
        this.menuOpenAnimation = menuOpenAnimation;

    }

    //设置MenuButton收回菜单选项时候MenuButton自身的动画，默认为逆时针旋转180度，为空则是关闭动画
    public void setMenuCloseAnimation(Animation menuCloseAnimation) {
        menuCloseAnimation.setAnimationListener(animationListener);
        this.menuCloseAnimation = menuCloseAnimation;
    }

    //设置禁止放置选项的位置序号，注意不能输入负数、重复数字或者大于等于optionPositionCounts的数，会报错。
    public void setBanArray(int... banArray) {
        this.banArray = banArray;
    }

    //设置“选项”占格个数
    public void setOptionPositionCount(int optionPositionCount) {
        this.optionPositionCount = optionPositionCount;
    }

    public void setOptionLines(int optionLines) {
        this.optionLines = optionLines;
    }


    public void setYMenuButtonWidth(int mYMenuButtonWidth) {
        this.mYMenuButtonWidth = mYMenuButtonWidth;
    }

    public void setYMenuButtonHeight(int mYMenuButtonHeight) {
        this.mYMenuButtonHeight = mYMenuButtonHeight;
    }

    public void setYMenuButtonBottomMargin(int mYMenuButtonBottomMargin) {
        this.mYMenuButtonBottomMargin = mYMenuButtonBottomMargin;
    }

    public void setYMenuButtonRightMargin(int mYMenuButtonRightMargin) {
        this.mYMenuButtonRightMargin = mYMenuButtonRightMargin;
    }

    public void setYOptionButtonWidth(int mYOptionButtonWidth) {
        this.mYOptionButtonWidth = mYOptionButtonWidth;
    }

    public void setYOptionButtonHeight(int mYOptionButtonHeight) {
        this.mYOptionButtonHeight = mYOptionButtonHeight;
    }

    public void setYOptionToMenuBottomMargin(int mYOptionToMenuBottomMargin) {
        this.mYOptionToMenuBottomMargin = mYOptionToMenuBottomMargin;
    }

    public void setYOptionToMenuRightMargin(int mYOptionToMenuRightMargin) {
        this.mYOptionToMenuRightMargin = mYOptionToMenuRightMargin;
    }

    public void setYOptionHorizontalMargin(int mYOptionHorizontalMargin) {
        this.mYOptionHorizontalMargin = mYOptionHorizontalMargin;
    }

    public void setmYOptionVerticalMargin(int mYOptionVerticalMargin) {
        this.mYOptionVerticalMargin = mYOptionVerticalMargin;
    }

    //使用OptionButton里面的静态变量，如OptionButton.FROM_BUTTON_LEFT
    public void setOptionSD_AnimationMode(int optionSD_AnimationMode) {
        this.mOptionSD_AnimationMode = optionSD_AnimationMode;
    }

    public void setOptionSD_AnimationDuration(int optionSD_AnimationDuration) {
        this.mOptionSD_AnimationDuration = optionSD_AnimationDuration;
    }

    public void setMenuButtonBackGroundId(int menuButtonBackGroundId) {
        this.mMenuButtonBackGroundId = menuButtonBackGroundId;
    }

    public void setOptionsBackGroundId(int optionsBackGroundId) {
        this.mOptionsBackGroundId = optionsBackGroundId;
    }




    public interface OnOptionsClickListener {
        public void onOptionsClick(int index);
    }

    protected interface OnShowDisappearListener {
        public void onShow();

        public void onClose();

        public void onDisappear();
    }
}
