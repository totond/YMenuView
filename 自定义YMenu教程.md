# 自定义YMenu教程

## 介绍
　　YMenu是所有YMenu的父类，通过继承它，重写里面的四个抽象方法，可以实现各种各样的YMenu。
　　

## 实现流程
　　实现自定义YMenu的话，总体流程如下：
 - 继承YMenu或者它的子类
 - 重写构造方法（这个是自定义View必须的）
 - 重写四个抽象方法


### 继承YMenu或者它的子类
　　继承YMenu的话就必须实现它的4个抽象方法，继承它的子类的话只要重写控制自己想改变内容的方法就行了（如Circle8YMenu的MenuButton的位置固定位于ViewGroup正中间，如果想改变的话可以继承Circle8YMenu，单单重写`setMenuPosition()`方法就可以了）。

### 重写构造方法
　　这是每个继承View的子类都要做的，如果没有特殊需求的话ALT+ENTER自动生成就好了（如下面`SquareYMenu`的构造方法）：

```
    public SquareYMenu(Context context) {
        super(context);
    }

    public SquareYMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareYMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
```

### 重写四个抽象方法
　　这四个抽象方法分别决定不同的功能，实现了它们就能做出一个完整的YMenu。

```
    /**
     * 设置MenuButton的位置,重写该方法进行自定义设置
     *
     * @param menuButton 传入传入MenuButton，此时它的宽高位置属性还未设置，需要在此方法设置。
     */
    public abstract void setMenuPosition(View menuButton);

    /**
     * 设置OptionButton的位置,重写该方法进行自定义设置
     *
     * @param optionButton 传入OptionButton，此时它的宽高位置属性还未设置，需要在此方法设置。
     * @param menuButton   传入MenuButton，此时它已经初始化完毕，可以利用。
     * @param index        传入的是该OptionButton的索引，用于区分不同OptionButton。
     */
    public abstract void setOptionPosition(OptionButton optionButton, View menuButton, int index);

    /**
     * 设置OptionButton的显示动画,重写该方法进行自定义设置
     *
     * @param optionButton 传入了该动画所属的OptionButton，此时它的宽高位置属性已初始化完毕，可以利用。
     * @param index        传入的是该OptionButton的索引，用于区分不同OptionButton。
     * @return             返回的是创建好的动画                    
     */
    public abstract Animation createOptionShowAnimation(OptionButton optionButton, int index);


    /**
     * 设置OptionButton的消失动画,重写该方法进行自定义设置
     *
     * @param optionButton 传入了该动画所属的OptionButton，此时它的宽高位置属性已初始化完毕，可以利用。
     * @param index        传入的是该OptionButton的索引，用于区分不同OptionButton。
     * @return             返回的是创建好的动画
     */
    public abstract Animation createOptionDisappearAnimation(OptionButton optionButton, int index);

```
　　　　这4个抽象方法是有执行顺序的，所以后面的方法能用到前面设置好的对象参数：
![](https://i.imgur.com/3ttowbC.png)

## 实例演示
　　上面的流程并不复杂，下面通过介绍YMenu子类中实现比较复杂的TreeYMenu的实现来演示一下如何实现自定义YMenu：
![](https://i.imgur.com/0lmsS7q.gif)
### setMenuPosition()方法
　　此方法的实现决定了MenuButton的位置，TreeYMenu的MenuButton位置默认是在中下，所以需要我们队传入的MenuButton做处理，以前我们采取的是这种方法：

```
    //设置MenuButton的位置，这是设置在屏幕中下方
    @Override
    public void setMenuPosition(View menuButton) {
        LayoutParams layoutParams = new LayoutParams(getYMenuButtonWidth(), getYMenuButtonHeight());
        layoutParams.bottomMargin = getYMenuToParentYMargin();
        layoutParams.addRule(ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(CENTER_HORIZONTAL);
        menuButton.setLayoutParams(layoutParams);
    }
```

　　然而我觉得这样逻辑不是很清晰，所以用Builder模式来封装了这个操作，让MenuButton的定位操作逻辑更简单一些：

```
    //设置MenuButton的位置，这是设置在屏幕中下方
    @Override
    public void setMenuPosition(View menuButton) {
        new MenuPositionBuilder(menuButton)
                //设置宽高
                .setWidthAndHeight(getYMenuButtonWidth(), getYMenuButtonHeight())
                //设置参考方向
                .setMarginOrientation(PositionBuilder.MARGIN_RIGHT,PositionBuilder.MARGIN_BOTTOM)
                //设置是否在XY方向处于中心，这个优先于setXYMargin()方法和setMarginOrientation()方法
                .setIsXYCenter(true,false)
                //设置XY方向的参考，如果设置了MARGIN_LEFT和MARGIN_TOP，那么XMargin和YMargin就是与参照物左边界和上边界的距离
                .setXYMargin(getYMenuToParentXMargin(),getYMenuToParentYMargin())
                 //进行最后的配置操作
                .finish();
    }
```
　　上面这两种方法是等价的。

### setOptionPosition()方法
　　此方法的实现决定了OptionButton的位置，

```
    //设置OptionButton的位置，这里是把9个Option设置为树状布局
    @Override
    public void setOptionPosition(OptionButton optionButton, View menuButton, int index) {
        if (index > 8){
            try {
                throw new Exception("TreeYMenuView的OptionPosition最大数量为9，超过将会发生错误");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        int centerX = menuButton.getLeft() + menuButton.getWidth()/2;
        int centerY = menuButton.getTop() + menuButton.getHeight()/2;
        int halfOptionWidth = getYOptionButtonWidth()/2;
        int halfOptionHeight = getYOptionButtonHeight()/2;

        //利用乘积因子来决定不同位置
        float x = xTimes[index];
        float y = yTimes[index];
        LayoutParams layoutParams = new LayoutParams(getYOptionButtonWidth(), getYOptionButtonHeight());
        layoutParams.addRule(ALIGN_LEFT);
        layoutParams.addRule(ALIGN_TOP);
        layoutParams.leftMargin = (int)(centerX + x * getYOptionXMargin() - halfOptionWidth);
        layoutParams.topMargin = (int)(centerY + y * getYOptionYMargin() - halfOptionHeight);
        optionButton.setLayoutParams(layoutParams);
}
```

　　和MenuButton一样，这个方法也有一个专属的OptionPositionBuilder，下面使用它来设置：

```
    //设置OptionButton的位置，这里是把9个Option设置为树状布局
    @Override
    public void setOptionPosition(OptionButton optionButton, View menuButton, int index) {
        if (index > 8){
            try {
                throw new Exception("TreeYMenuView的OptionPosition最大数量为9，超过将会发生错误");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        int centerX = menuButton.getLeft() + menuButton.getWidth()/2;
        int centerY = menuButton.getTop() + menuButton.getHeight()/2;
        int halfOptionWidth = getYOptionButtonWidth()/2;
        int halfOptionHeight = getYOptionButtonHeight()/2;

        //利用乘积因子来决定不同位置
        float x = xTimes[index];
        float y = yTimes[index];
        new OptionPositionBuilder(optionButton,menuButton)
                .isAlignMenuButton(false,false)
                .setWidthAndHeight(getYOptionButtonWidth(), getYOptionButtonHeight())
                .setMarginOrientation(PositionBuilder.MARGIN_LEFT,PositionBuilder.MARGIN_TOP)
                .setXYMargin(
                        (int)(centerX + x * getYOptionXMargin() - halfOptionWidth)
                        ,(int)(centerY + y * getYOptionYMargin() - halfOptionHeight)
                )
                .finish();
    }
```
　　这里用到了乘积因子，它们是xy方向距离对应的比例值：以MenuButton的中心作为参考点，以optionXMargin和optionYMargin作为单位长度，xy乘积因子分别乘以单位长度就是OptionButton到参考点的x,y方向距离，根据这个距离和MenuButton的位置信息，就可以得出OptionButton的布局了：

```
    //9个Option位置的x、y乘积因子
    private static final float[] xTimes = {-1,1,0,-2,-2,2,2,-1,1};
    private static final float[] yTimes = {-1,-1,-2,0,-2,0,-2,-3,-3};
```
 > 如index为0的OptionButton，它的x=-1，y=-1，以ViewGroup的左上边缘为参考，所以它最后就处在MenuButton的左上方了

### createOptionShowAnimation()和createOptionDisappearAnimation()方法
　　此方法用于创造OptionButton出现动画，并且把它返回。这里动画思路是：前三个从MenuButton里冒出来，后面的6个分别两两从这前三个位置里冒出来，还设置了延时，让前三个出现完了再让后面的出现，实现分叉树的生成效果。

```
    //设置OptionButton的显示动画，这里是为前三个先从MenuButton冒出，后面的分别从这三个冒出
    @Override
    public Animation createOptionShowAnimation(OptionButton optionButton, int index) {
        float fromX,fromY;
        AnimationSet animationSet = new AnimationSet(true);
        if (index < 3){
            fromX = getYMenuButton().getX() - optionButton.getX();
            fromY = getYMenuButton().getY() - optionButton.getY();
        }else {
            int oldIndex = (index - 3) / 2;
            fromX = getOptionButtonList().get(oldIndex).getX() - optionButton.getX();
            fromY = getOptionButtonList().get(oldIndex).getY() - optionButton.getY();
            //设置冒出动画延时
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
        animationSet.setInterpolator(new LinearInterpolator());
        return animationSet;
    }
```

### createOptionDisappearAnimation()方法
　　此方法用于创造OptionButton消失动画，并且把它返回。实现思路比较简单粗暴:直接让OptionButton从原来的位置移动到MenuButton位置上消失，不过每一个OptionButton的延时都不一样。

```
    //设置OptionButton的消失动画，这里设置的是直接从当前位置移动到MenuButton位置消失
    @Override
    public Animation createOptionDisappearAnimation(OptionButton optionButton, int index) {

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

        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(alphaAnimation);
        //设置动画延时
        animationSet.setStartOffset(60*(getOptionPositionCount() - index));
        return animationSet;
    }
```

## 最后
　　所以实现自定义YMenu最核心就是这四个抽象方法的实现，继承YMenu实现它们、或者继承子类重写里面的几个，就能够按照你的想法实现炫酷的菜单按钮啦。
　　