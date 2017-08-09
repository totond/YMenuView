# YMenuView
　　欢迎 Star or Fork！有什么意见和建议可以在Issue上提出。

## 简介
　　效果如下图，这是一个类似Path菜单的自定义View，暂时是可以设定4种菜单进出方式，目前只能在右下方展现（因为其他方向代码差不多，重复性的工作太多，暂时没空弄），然后具体原理后面会在博客上写出。
![](http://i.imgur.com/gDmX0mM.gif)

## 使用

### Gradle

```
    compile 'com.yanzhikaijky:YMenuView:1.0.0'
```

### 属性
![](http://i.imgur.com/9efDIGX.png)

|**属性名称**|**意义**|**类型**|**默认值**|
|--|--|:--:|:--:|
|menuButtonWidth      | 菜单键宽度     | dimension| 80px|
|menuButtonHeight      | 菜单键高度    | dimension| 80px|
|optionButtonWidth      | 选项键宽度     | dimension| 80px|
|optionButtonHeight      | 选项键高度     | dimension| 80px|
|menuButtonRightMargin      | 菜单键距离View右边缘距离     | dimension| 50px|
|menuButtonBottomMargin      | 菜单键距离View下边缘距离       | dimension| 50px|
|optionToMenuRightMargin      | 选项键距离View右边缘距离     | dimension| 35px|
|optionToMenuBottomMargin      | 选项键距离View下边缘距离     | dimension| 160px|
|optionHorizontalMargin      | 选项键之间水平间距    | dimension| 15px|
|optionVerticalMargin      | 选项键之间垂直间距    | dimension| 15px|
|menuButtonBackGround      | 菜单键背景Drawable    | reference|@drawable/setting |
|optionsBackGround      | 选项键背景Drawable    | reference|@drawable/null_drawable|
|optionPositionCounts      | “选项”占格个数     | integer| 8|
|optionLines      | “选项”占格行数     | integer| 1|
|sd_duration      | 进出动画耗时     | integer| 600|
|sd_animMode      | 进出动画选择     | enum| FROM_BUTTON_TOP|

**对应的sd_animMode模式有：**

|sd_animMode|描述|
|--|--|
|FROM_BUTTON_LEFT|选项从菜单键左边缘飞入|
|FROM_BUTTON_TOP|选项从菜单键上边缘飞入|
|FROM_RIGHT|选项从View左边缘飞入|
|FROM_BOTTOM|选项从View左边缘飞入|

**除了上面的XML属性的set、get方法，还有下面的一些重要方法：**

```
    /*
     * 下面的set方法需要在View还没有初始化的时候调用，例如Activity的onCreate方法里
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

### demo演示
#### 竖放式
**　　XML:**

```
    <com.yanzhikai.ymenuview.YMenuView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/ymv"
        app:optionPositionCounts="8"
        app:menuButtonBackGround="@drawable/setting"
        app:optionsBackGround="@drawable/background_option_button" />
```
**　　效果:**
![](http://i.imgur.com/ns7fcnE.gif)

#### 横放式
**　　XML:**

```
<com.yanzhikai.ymenuview.YMenuView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/ymv"
        app:optionToMenuRightMargin="80dp"
        app:optionToMenuBottomMargin="25dp"
        app:optionPositionCounts="5"
        app:optionLines="5"
        app:sd_animMode="FROM_BUTTON_LEFT"
        app:menuButtonBackGround="@drawable/setting"
        app:optionsBackGround="@drawable/background_option_button" />
```
**　　效果:**
![](http://i.imgur.com/4gSGuyi.gif)

#### Ban列表式
**　　XML:**

```
    <com.yanzhikai.ymenuview.YMenuView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/ymv"
        app:optionToMenuRightMargin="50dp"
        app:optionPositionCounts="8"
        app:optionLines="3"
        app:sd_animMode="FROM_RIGHT"
        app:menuButtonBackGround="@drawable/setting"
        app:optionsBackGround="@drawable/background_option_button" />
```

**　　Java代码:**

```
        mYMenuView = (YMenuView) findViewById(R.id.ymv);
        mYMenuView.setBanArray(0,2,6);
```

**　　效果:**
![](http://i.imgur.com/V9wTXJz.gif)
　　可以看出0、2、6号位都没有放置OptionButton。

## 后续
　　可能会考虑有空把它做成多个位置的，不只是右下，还有加入更多的进出动画，目前正在写源码解析的博客。

## 开源协议
　　YMenuView遵循Apache 2.0开源协议。

## 关于作者
 > id：炎之铠

 > 炎之铠的邮箱：yanzhikai_yjk@qq.com

 > CSDN：http://blog.csdn.net/totond