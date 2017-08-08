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
 * @Description 一个可以弹出收回菜单栏的自定义View，带有动画效果
 */

public class YMenuView extends RelativeLayout {
    public final static String TAG = "ymenuview";

    private Context mContext;
    private Button mYMenuButton;

    private int drawableIds[] = {R.drawable.zero,R.drawable.one, R.drawable.two, R.drawable.three,
            R.drawable.four, R.drawable.five,R.drawable.six,R.drawable.seven};
    private ArrayList<OptionButton> optionButtonList;
    private ArrayList<OnShowDisappearListener> listenerList;
    private int optionPositionCount = 8;
    private int optionLines = 1;
    private int[] banArray = {};
    private int mYMenuButtonWidth = 80, mYMenuButtonHeight = 80;
    private int mYOptionButtonWidth = 80, mYOptionButtonHeight = 80;
    private int mYMenuButtonRightMargin = 50, mYMenuButtonBottomMargin = 50;
    private int mYOptionVerticalMargin = 15, mYOptionHorizontalMargin = 15;
    private int mYOptionToMenuBottomMargin = 160, mYOptionToMenuRightMargin = 35;
    private @DrawableRes int mMenuButtonBackGroundId = R.drawable.setting;
    private @DrawableRes Integer mOptionsBackGroundId = R.drawable.background_option_button;
    private boolean isShowMenu = false;
    private Animation menuOpenAnimation, menuCloseAnimation;
    private Animation.AnimationListener animationListener;
    private int mOptionSD_AnimationMode = OptionButton.FROM_BUTTON_TOP;
    private int mOptionSD_AnimationDuration = 600;
    private OnOptionsClickListener mOnOptionsClickListener;

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
        mMenuButtonBackGroundId = typedArray.getResourceId(R.styleable.YMenuView_menuButtonBackGround, R.drawable.null_drawable);
        mOptionsBackGroundId = typedArray.getResourceId(R.styleable.YMenuView_optionsBackGround,R.drawable.null_drawable);
        mOptionSD_AnimationMode = typedArray.getInt(R.styleable.YMenuView_sd_animMode,mOptionSD_AnimationMode);
        mOptionSD_AnimationDuration = typedArray.getInt(R.styleable.YMenuView_sd_duration,mOptionSD_AnimationDuration);
    }

    private void init(Context context) {
        mContext = context;

        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
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
        });
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
        listenerList = new ArrayList<>(optionPositionCount);


        for (int i = 0,n = 0; i < optionPositionCount; i++) {
            if (banArray.length > 0) {
                //Ban判断
                if (i > banArray[n]) {
                    throw new Exception("Ban数组设置不合理，含有负数或者超出范围");
                } else if (i == banArray[n]) {
                    n++;
                    continue;
                }
            }

            OptionButton button = new OptionButton(mContext);
            button.setSD_Animation(mOptionSD_AnimationMode);
            button.setDuration(mOptionSD_AnimationDuration);
            int btnId = generateViewId();
            button.setId(btnId);
            optionButtonList.add(button);

            RelativeLayout.LayoutParams layoutParams = new LayoutParams(mYOptionButtonWidth, mYOptionButtonHeight);

            //计算OptionButton的位置
            int position = i % optionLines;
            layoutParams.setMarginEnd(mYOptionToMenuRightMargin
                    + mYOptionHorizontalMargin * (position + 1)
                    + mYOptionButtonWidth * position);
            layoutParams.bottomMargin = mYOptionToMenuBottomMargin
                    + (mYOptionButtonHeight + mYOptionVerticalMargin) * (i / optionLines);
            layoutParams.addRule(ALIGN_PARENT_BOTTOM);
            layoutParams.addRule(ALIGN_PARENT_RIGHT);


            button.setLayoutParams(layoutParams);

            addView(button);

            listenerList.add(button);
        }
    }



    //设置选项按钮的background
    public void setOptionBackGrounds(@DrawableRes Integer drawableId){
        for (int i = 0; i < optionButtonList.size(); i++) {
            if (drawableId == null){
                optionButtonList.get(i).setBackground(null);
            }else {
                optionButtonList.get(i).setBackground(getResources().getDrawable(R.drawable.background_option_button));
            }

        }

    }

    public void setOptionsImages(int... drawableIds) throws Exception {
        this.drawableIds = drawableIds;
        if (optionPositionCount > drawableIds.length + banArray.length) {
            throw new Exception("Drawable资源数量不足");
        }

        for (int i = 0; i < optionButtonList.size(); i++) {
            optionButtonList.get(i).setOnClickListener(new MyOnClickListener(i));
            if (drawableIds == null){
                Log.d(TAG, "setOptionsImages: ");
                optionButtonList.get(i).setImageDrawable(null);
            }else {
                optionButtonList.get(i).setImageResource(drawableIds[i]);
            }

        }
    }

    public void showMenu() {
        if (!isShowMenu) {
            for (OnShowDisappearListener listener : listenerList) {
                listener.onShow();
            }
            isShowMenu = true;
        }
    }

    public void closeMenu() {
//        for (Button button : optionButtonList){
//            button.setVisibility(INVISIBLE);
//            Log.d(TAG, "closeMenu: " + button.getId());
//        }
        if (isShowMenu) {
            for (OnShowDisappearListener listener : listenerList) {
                listener.onClose();
            }
            isShowMenu = false;
        }
    }

    public void disappearMenu() {
        if (isShowMenu) {
            for (OnShowDisappearListener listener : listenerList) {
                listener.onDisappear();
            }
            isShowMenu = false;
        }
    }

    public Button getYMenuButton() {
        return mYMenuButton;
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

    public void setMenuOpenAnimation(Animation menuOpenAnimation) {
        menuOpenAnimation.setAnimationListener(animationListener);
        this.menuOpenAnimation = menuOpenAnimation;

    }

    public void setMenuCloseAnimation(Animation menuCloseAnimation) {
        menuCloseAnimation.setAnimationListener(animationListener);
        this.menuCloseAnimation = menuCloseAnimation;
    }

    public void setOptionLines(int optionLines) {
        this.optionLines = optionLines;
    }

    public void setBanArray(int... banArray) {
        this.banArray = banArray;
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
