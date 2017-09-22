# YMenuView
　　欢迎 Star or Fork！有什么意见和建议可以在Issue上提出。

![](https://i.imgur.com/GK8b2P0.gif)
（前面是之前YMenuView1.x的效果，后面是新加的）

 > 传送门
 > [YMenuView1.x分析](http://blog.csdn.net/totond/article/details/77364137)
 > [YMenuView2.x分析](http://blog.csdn.net/totond/article/details/78059196)
 > [自定义YMenu教程](https://github.com/totond/YMenuView/blob/master/自定义YMenu教程.md)

## 简介
　　这是一个类似Path菜单的自定义View，这是经过大改版之后的YMenuView2.x，之前[1.x版本的README](https://github.com/totond/YMenuView/blob/master/README_1.x.md)。2.x版本可以自定义MenuButton和OptionButton的布局和显示消失动画，效果可参考上图，下面来开始看看如何自定义炫酷的YMenu吧。


## 使用

### Gradle

```
    compile 'com.yanzhikaijky:YMenuView:2.0.0'
```

### 抽象类YMenu
　　2.x版本后由之前单一的YMenuView把一些通用逻辑提取出来形成了抽象父类YMenu，开放出4个抽象方法来让用户自定义MenuButton和OptionButton的布局和显示消失动画，而YMenuView的功能则没有变化，增加了3个YMenu的子类，实现了不同的效果。

![](https://i.imgur.com/5ze9jNT.png)

### 属性

 > 和1.x版本相比，属性名字有所改变


|**属性名称**|**意义**|**类型**|**默认值**|
|--|--|:--:|:--:|
|menuButtonWidth      | 菜单键宽度     | dimension| 80px|
|menuButtonHeight      | 菜单键高度    | dimension| 80px|
|optionButtonWidth      | 选项键宽度     | dimension| 80px|
|optionButtonHeight      | 选项键高度     | dimension| 80px|
|~~menuButtonRightMargin~~  menuToParentXMargin  | 菜单键距离YMenuView边缘距离     | dimension| 50px|
|~~menuButtonBottomMargin~~  menuToParentYMargin  | 菜单键距离YMenuView边缘距离       | dimension| 50px|
|~~optionToMenuRightMargin~~  optionToParentXMargin | 选项键距离YMenuView边缘距离     | dimension| 35px|
|~~optionToMenuBottomMargin~~  optionToParentYMargin  | 选项键距离YMenuView边缘距离     | dimension| 160px|
|~~optionHorizontalMargin~~  optionXMargin | 选项键之间水平间距    | dimension| 15px|
|~~optionVerticalMargin~~   optionYMargin | 选项键之间垂直间距    | dimension| 15px|
|menuButtonBackGround      | 菜单键背景Drawable    | reference|@drawable/setting |
|optionsBackGround      | 选项键背景Drawable    | reference|@drawable/null_drawable|
|optionPositionCounts      | “选项”占格个数     | integer| 8|
|optionColumns      | “选项”占格列数     | integer| 1|
|isShowMenu      | 一开始时是否展开菜单     | boolean| false|
|sd_duration      | 进出动画耗时     | integer| 600|
|sd_animMode      | 进出动画选择     | enum| FROM_BUTTON_TOP|

由于YMenuView2.0重构之后实现了继承YMenu可以实现各种布局，所以下图只是参考YMenu的一个子类——YMenuView的属性，MenuButton和OptionButton都是依靠右下边缘。
![](https://i.imgur.com/GIQRp3t.png)

**对应的sd_animMode模式有（这是YMenuView特有的）：**

|sd_animMode|描述|
|--|--|
|FROM_BUTTON_LEFT|选项从菜单键左边缘飞入|
|FROM_BUTTON_TOP|选项从菜单键上边缘飞入|
|FROM_RIGHT|选项从View左边缘飞入|
|FROM_BOTTOM|选项从View左边缘飞入|

**除了上面的XML属性的set、get方法，还有下面的一些重要方法：**

```
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

    //获取实际的OptionButton数量
    public int getOptionButtonCount() 

    //设置MenuButton弹出菜单选项时候MenuButton自身的动画，默认为顺时针旋转180度，为空则是关闭动画
    public void setMenuOpenAnimation(Animation menuOpenAnimation) 

    //设置MenuButton收回菜单选项时候MenuButton自身的动画，默认为逆时针旋转180度，为空则是关闭动画
    public void setMenuCloseAnimation(Animation menuCloseAnimation) 

    /*
     * 下面的set方法需要在View还没有初始化的时候调用，例如Activity的onCreate方法里
     * 如果不在View还没初始化的时候调用，请使用完set这些方法之后调用refresh()方法刷新
     */

    //设置OptionButton的Drawable资源
    public void setOptionDrawableIds(int... drawableIds) 

    //设置禁止放置选项的位置序号，注意不能输入负数、重复数字或者大于等于optionPositionCounts的数，会报错。
    public void setBanArray(int... banArray) 
```

 　　**PS: **banArray里面设置的位置序号就是告诉YMenuView不要在那个位置上放置OptionButton，所以这里有一条规则就是：`optionPositionCounts >= banArray.length + drawableIds.length`

### 点击监听
　　实现YMenuView.OnOptionsClickListener接口，重写里面的onOptionsClick()方法即可：

```
public class MainActivity extends AppCompatActivity implements YMenuView.OnOptionsClickListener{
    private YMenuView mYMenuView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mYMenuView = (YMenuView) findViewById(R.id.ymv);
        mYMenuView.setOnOptionsClickListener(this);
    }


    @Override
    public void onOptionsClick(int index) {
        switch (index){
            case 0:
                makeToast("0");
                break;
            case 1:
                makeToast("1");
                break;
            case 2:
                makeToast("2");
                break;
            case 3:
                makeToast("3");
                break;
            case 4:
                makeToast("4");
                break;
            case 5:
                makeToast("5");
                break;
        }
    }


    private void makeToast(String str){
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }
}
```

### YMenu其他子类
#### Circle8YMenu
![](https://i.imgur.com/TKPOKZq.gif)
　　这是一个OptionButton围绕着MenuButton的布局，Option最大数量为8个，MenuButton的位置位于ViewGroup正中间，如果想改变的话可以继承Circle8YMenu，单单重写`setMenuPosition()`方法就可以了，这个是以`optionXMargin`为圆的半径。

#### TreeYMenu
![](https://i.imgur.com/niud6YI.gif)
　　这是一个OptionButton分布成分叉树的布局，Option最大数量为9个，MenuButton的位置位于ViewGroup中下方，支持了`menuToParentYMargin`属性。

#### SquareYMenu
![](https://i.imgur.com/ucD4RQv.gif)
　　这是一个OptionButton和MenuButton组成正方形的布局，Option最大数量为8个，MenuButton为位置依靠右下，支持了`menuToParentYMargin`和`menuToParentXMargin`属性。

### 自定义YMenu
　　这里才是YMenuView2.x的重头戏，但是由于篇幅过长，可以跳转到[自定义YMenu教程](https://github.com/totond/YMenuView/blob/master/自定义YMenu教程.md)查看。


### demo演示

### ViewGroup的宽高
　　由于YMenuView是用ViewGroup来实现的，所以如果选项OptionButton的位置超出YMenuView的范围的话，会出现不能收回的情况。demo里面使用的是match_parent，实际中使用可以把YMenuView放到其他视图的上层。

#### YMenu的子类实现演示
　　Demo里面的activity_main.xml有效果图上各种效果的YMenu实现定义，查看的时候只要反注释一下，就可以运行了，xml里面的格式如下：

```
    <!--右下角纵向8个-->
    <!--<com.yanzhikai.ymenuview.YMenuView-->
        <!--android:layout_width="match_parent"-->
        <!--android:layout_height="match_parent"-->
        <!--android:id="@+id/ymv"-->
        <!--app:optionPositionCounts="8"-->
        <!--app:optionColumns="1"-->
        <!--app:menuButtonBackGround="@drawable/setting"-->
        <!--app:optionsBackGround="@drawable/background_option_button" />-->

    <!--右下角3竖8个-->
    ...

    <!--右下角横向5个-->
    ...

    <!--Circle8-->
    ...

    <!--Tree-->
    ...

    <!--Square-->
    ...
```


#### Ban列表式
　　YMenuView1.x就有有Ban功能，能把一些位置设为不放置OptionButton，那么YMenuView2.0也支持这个，例如：

```
        //对Circle8YMenu
        mYMenu.setBanArray(0,2,4,6);
```
![](https://i.imgur.com/PVfbx8D.gif)

```
        //对TreeYMenu
        mYMenu.setBanArray(2,8,7);
```
![](https://i.imgur.com/96aVxwk.gif)

```
        //对SquareYMenu
        mYMenu.setBanArray(3,4,7,5,6);
```
![](https://i.imgur.com/WnloJhv.gif)
　　但是动画延时还是会算上不被填充的位置，这暂时无法避免，所以想要更好的体验效果的话就继承YMenu重写方法吧。

## 更新
- **version 2.0.0*:2017/09/21 全新版本更新：可以实现自定义YMenu，增加3个YMenu类型，原YMenuView现在也是YMenu的一个子类。

## 后续
　　可能会实现自定义动画，还有给这些OptionButton加上轨迹等。

## 开源协议
　　YMenuView遵循Apache 2.0开源协议。

## 关于作者
 > id：炎之铠

 > 炎之铠的邮箱：yanzhikai_yjk@qq.com

 > CSDN：http://blog.csdn.net/totond
 
 > YMenuView原理分析博客：
 > YMenuView1.x分析：http://blog.csdn.net/totond/article/details/77364137
 > YMenuView2.x分析：http://blog.csdn.net/totond/article/details/78059196