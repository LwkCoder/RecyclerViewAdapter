# RecyclerViewAdapter

想了解该框架的可参考以下博客说明：<br />
CSDN：http://blog.csdn.net/lwk520136/article/details/70787798<br />
简书：http://www.jianshu.com/p/c86a39f4e811

### 引用方式
【最新版本号以[这里](https://github.com/Vanish136/RecyclerViewAdapter/releases)为准】

```
#last-version请查看上面的最新版本号

#AndroidStudio3.0以下
//在自己项目里添加RecyclerView的引用
compile 'com.android.support:recyclerview-v7:your-version'
//添加该库的引用，并去除自带的RecyclerView引用，防止版本冲突
compile("com.lwkandroid:recyclerviewadapter:last-version") {
    exclude(group: 'com.android.support', module: 'recyclerview-v7')
}

#AndroidStudio3.0以上
//在自己项目里添加RecyclerView的引用
implementation 'com.android.support:recyclerview-v7:your-version'
//添加该库的引用，并去除自带的RecyclerView引用，防止版本冲突
implementation("com.lwkandroid:recyclerviewadapter:last-version") {
    exclude(group: 'com.android.support', module: 'recyclerview-v7')
}
```

### 基础功能
 - 快速实现适配器，支持多种ViewType模式
 - 支持添加HeaderView、FooterView、EmptyView
 - 支持滑到底部加载更多
 - 支持每条Item显示的动画
 - 支持嵌套Section（1.1.0版本新增）
 - 支持悬浮标签StickyLayout（1.2.0版本新增）

### 效果图
![](https://github.com/Vanish136/RecyclerViewAdapter/raw/master/pictures/pic01.png) <br />
![](https://github.com/Vanish136/RecyclerViewAdapter/raw/master/pictures/pic02.png) <br />
![](https://github.com/Vanish136/RecyclerViewAdapter/raw/master/pictures/pic03.png) <br />
![](https://github.com/Vanish136/RecyclerViewAdapter/raw/master/pictures/pic04.png) <br />
![](https://github.com/Vanish136/RecyclerViewAdapter/raw/master/pictures/pic05.gif) <br />

### 使用方式

**1. 当Item样式一样时，只需继承`RcvSingleAdapter<T>`即可，示例：**

```
public class TestSingleAdapter extends RcvSingleAdapter<TestData>
{
    public TestSingleAdapter(Context context, List<TestData> datas)
    {
        super(context, android.R.layout.simple_list_item_1, datas);
    }

    @Override
    public void onBindView(RcvHolder holder, TestData itemData, int position)
    {
	    //在这里绑定UI和数据，RcvHolder中提供了部分快速设置数据的方法，详情请看源码
        holder.setTvText(android.R.id.text1, itemData.getContent());
    }
}
```
<br/>

**2. 当Item样式不一样时，即存在多种`ViewType`类型的Item，需要将每种`ViewType`的Item单独实现，再关联到`RcvMultiAdapter<T>`中，示例：**

```
//第一步：每种Item分别继承RcvBaseItemView<T>
public class LeftItemView extends RcvBaseItemView<TestData>
{
    @Override
    public int getItemViewLayoutId()
    {
	    //这里返回该Item的布局id
        return R.layout.layout_item_left;
    }

    @Override
    public boolean isForViewType(TestData item, int position)
    {
	    //这里判断何时引用该Item
        return position % 2 == 0;
    }

    @Override
    public void onBindView(RcvHolder holder, TestData testData, int position)
    {
	    //在这里绑定UI和数据，RcvHolder中提供了部分快速设置数据的方法，详情请看源码
        holder.setTvText(R.id.tv_left, testData.getContent());
    }
}

//第二步：将所有Item关联到适配器中
public class TestMultiAdapter extends RcvMultiAdapter<TestData>
{
    public TestMultiAdapter(Context context, List<TestData> datas)
    {
        super(context, datas);
        //只需在构造方法里将所有Item关联进来,无论多少种ViewType都轻轻松松搞定
        addItemView(new LeftItemView());
        addItemView(new RightItemView());
    }
}
```
<br />

**3.优雅的添加HeaderView、FooterView、EmptyView，只需要在RecyclerView设置LayoutManager后调用相关方法即可：**

```
//要先设置LayoutManager
mRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

//添加HeaderView(若干个)
mAdapter.addHeaderView(headerView01,headerView02,headerView03...);

//添加FooterView(若干个)
mAdapter.addFooterView(footerView01,footerView02,footerView03...);

//添加EmptyView（只能设置一个）
//设置了EmptyView后，当数据量为0的时候会显示EmptyView
mAdapter.setEmptyView(emptyView);
或者
mAdapter.setEmptyView(layoutId);
```
<br />

**4.设置滑动到底部自动加载更多，先上示例代码吧：**

**自1.4.0版本开始删除了之前的调用方式**
```
//先设置加载样式，可继承RcvBaseLoadMoreView实现自定义样式
RcvDefLoadMoreView loadMoreView = new RcvDefLoadMoreView.Builder()
                .setBgColor(Color.GREEN)
                .setTextColor(Color.RED)
                .build(this);
mAdapter.setLoadMoreLayout(loadMoreView);
//再开启并设置监听
mAdapter.enableLoadMore(new RcvLoadMoreListener()
        {
            @Override
            public void onLoadMoreRequest()
            {
               //TODO 在这里实现加载更多
            }
        });
//禁止加载更多,通常用在配合下拉刷新的过程中
mAdapter.disableLoadMore();
```
**注：** <br />
① 默认的样式实现是类`RcvDefLoadMoreView` <br />
② 如需自定义样式，只需继承`RcvBaseLoadMoreView`，只要重写各状态UI的实现，无须关心状态切换，可参考`RcvDefLoadMoreView`内的实现方式。
<br />

**5.设置Item显示动画，先直接上代码：**

```
//使用默认的动画（Alpha动画）
mAdapter.enableItemShowingAnim(true);

//使用自定义动画
mAdapter.enableItemShowingAnim(true, ? extends RcvBaseAnimation);
```

**注：** <br />
①默认动画的实现是类`RcvAlphaInAnim` <br />
②自定义样式需要继承`RcvBaseAnimation`，可参考`RcvAlphaInAnim`内部实现。
<br />

**6.设置Item点击监听：**

```
    //设置OnItemClickListener
    mAdapter.setOnItemClickListener(new RcvItemViewClickListener<TestData>()
        {
            @Override
            public void onItemViewClicked(RcvHolder holder, TestData testData, int position)
            {
                //onClick回调
            }
        });

    //设置OnItemLongClickListener
    mAdapter.setOnItemLongClickListener(new RcvItemViewLongClickListener<TestData>()
        {
            @Override
            public void onItemViewLongClicked(RcvHolder holder, TestData testData, int position)
            {
                //onLongClick回调
            }
        });
```
<br />

**7. 添加分割线，直接上代码：**

**1.2.9版本针对分割线进行了重写，原有方法不变，新增支持自定义颜色和部分快速创建的方法：**
```
#适用于LinearLayoutManager
//创建默认竖直排列的分割线
RcvLinearDecoration.createDefaultVertical(Context context);
//创建自定义色值默认竖直排列的分割线
RcvLinearDecoration.createDefaultVertical(int color);
//创建默认水平排列的分割线
RcvLinearDecoration.createDefaultHorizontal(Context context);
//创建自定义色值默认水平排列的分割线
RcvLinearDecoration.createDefaultHorizontal(int color);
//构造方法：默认Drawable分割线
new RcvLinearDecoration(Context context, int orientation);
//构造方法：自定义Drawable分割线
new RcvLinearDecoration(Context context, Drawable drawable, int orientation);
//构造方法：自定义Drawable分割线
new RcvLinearDecoration(Context context, @DrawableRes int drawableResId, int orientation);
//构造方法：自定义Color分割线（宽度或者高度默认1px）
new RcvLinearDecoration(@ColorInt int color, int orientation);
//构造方法：自定义Color分割线
new RcvLinearDecoration(@ColorInt int color, int size, int orientation);

#适用于GridLayoutManager、StaggeredGridLayoutManager
//创建默认分割线
RcvGridDecoration.createDefault(Context context);
//创建自定义色值默认分割线
RcvGridDecoration.createDefault(int color);
//构造方法：默认Drawable的分割线
new RcvGridDecoration(Context context);
//构造方法：自定义Drawable的分割线
new RcvGridDecoration(Context context, Drawable drawable);
//构造方法：自定义Drawable的分割线
new RcvGridDecoration(Context context, @DrawableRes int drawableResId);
//构造方法：自定义Color的分割线（默认分割线宽高均为1px）
new RcvGridDecoration(@ColorInt int color);
//构造方法：自定义Color的分割线
new RcvGridDecoration(@ColorInt int color, int width, int height);
```

**注：** <br />
①是直接设置给RecyclerView的，不是设置给适配器的，不要看错哦<br />
②支持自定义drawable当分割线
<br />

**8.嵌套Section，稍微复杂一点，配合代码讲解：**

**1.4.0版本开始删除以前的使用方法，采用下面的方式**

带有Section功能的适配器为`RcvSectionMultiLabelAdapter`和`RcvSectionSingleLabelAdapter`,需要指定两个泛型，第一个代表`Section`，第二个代表普通数据`Data`，
两者都支持多种Data类型的子布局，唯一不同的是，`RcvSectionMultiLabelAdapter`还支持多种Section类型的子布局，但不可以和`RcvStickyLayout`联动，而`RcvSectionSingleLabelAdapter`
仅支持一种Section类型的子布局，但是可以和`RcvStickyLayout`联动。**需要注意的是，传给适配器的数据均需要自行预处理，用`RcvSectionWrapper`封装后才可传入适配器。**

```
#只有一种Section类型，配合多种Data类型的适配器
public class TestSectionAdapter extends RcvSectionSingleLabelAdapter<TestSection, TestData>
{
    public TestSectionAdapter(Context context, List<RcvSectionWrapper<TestSection, TestData>> datas)
    {
        super(context, datas);
    }

    @Override
    protected RcvBaseItemView<RcvSectionWrapper<TestSection, TestData>>[] createDataItemViews()
    {
        return new RcvBaseItemView[]{new DataItemView01(), new DataItemView02()};
    }

    @Override
    public int getSectionLabelLayoutId()
    {
        return R.layout.layout_section_label;
    }

    @Override
    public void onBindSectionLabelView(RcvHolder holder, TestSection section, int position)
    {
        holder.setTvText(R.id.tv_section_label, section.getSection());
    }

    //第一种Data ItemView
    private class DataItemView01 extends RcvBaseItemView<RcvSectionWrapper<TestSection, TestData>>
    {
        @Override
        public int getItemViewLayoutId()
        {
            return R.layout.adapter_item_long;
        }

        @Override
        public boolean isForViewType(RcvSectionWrapper<TestSection, TestData> item, int position)
        {
            return !item.isSection() && item.getData().getType() == 0;
        }

        @Override
        public void onBindView(RcvHolder holder, RcvSectionWrapper<TestSection, TestData> wrapper, int position)
        {
            TextView textView = holder.findView(R.id.tv_item_long);
            textView.setBackgroundColor(Color.GREEN);
            textView.setText("第一种数据类型：" + wrapper.getData().getContent());
        }
    }

    //第二种Data ItemView
    private class DataItemView02 extends RcvBaseItemView<RcvSectionWrapper<TestSection, TestData>>
    {
        @Override
        public int getItemViewLayoutId()
        {
            return R.layout.adapter_item_short;
        }

        @Override
        public boolean isForViewType(RcvSectionWrapper<TestSection, TestData> item, int position)
        {
            return !item.isSection() && item.getData().getType() != 0;
        }

        @Override
        public void onBindView(RcvHolder holder, RcvSectionWrapper<TestSection, TestData> wrapper, int position)
        {
            TextView textView = holder.findView(R.id.tv_item_short);
            textView.setBackgroundColor(Color.RED);
            textView.setText("第二种数据类型：" + wrapper.getData().getContent());
        }
    }
}

#多种Section类型，配合多种Data类型的适配器
public class TestSectionMultiLabelAdapter extends RcvSectionMultiLabelAdapter<TestSection, TestData>
{
    public TestSectionMultiLabelAdapter(Context context, List<RcvSectionWrapper<TestSection, TestData>> datas)
    {
        super(context, datas);
    }

    @Override
    protected RcvBaseItemView<RcvSectionWrapper<TestSection, TestData>>[] createLabelItemViews()
    {
        return new RcvBaseItemView[]{new LabelItemView01(), new LabelItemView02()};
    }

    @Override
    protected RcvBaseItemView<RcvSectionWrapper<TestSection, TestData>>[] createDataItemViews()
    {
        return new RcvBaseItemView[]{new DataItemView01(), new DataItemView02()};
    }


    //第一种Label ItemView
    private class LabelItemView01 extends RcvBaseItemView<RcvSectionWrapper<TestSection, TestData>>
    {
        @Override
        public int getItemViewLayoutId()
        {
            return R.layout.layout_section_label;
        }

        @Override
        public boolean isForViewType(RcvSectionWrapper<TestSection, TestData> item, int position)
        {
            return item.isSection() && item.getSection().getType() == 0;
        }

        @Override
        public void onBindView(RcvHolder holder, RcvSectionWrapper<TestSection, TestData> wrapper, int position)
        {
            holder.setTvText(R.id.tv_section_label, wrapper.getSection().getSection());
        }
    }

    //第二种Label ItemView
    private class LabelItemView02 extends RcvBaseItemView<RcvSectionWrapper<TestSection, TestData>>
    {
        @Override
        public int getItemViewLayoutId()
        {
            return R.layout.layout_section_label02;
        }

        @Override
        public boolean isForViewType(RcvSectionWrapper<TestSection, TestData> item, int position)
        {
            return item.isSection() && item.getSection().getType() != 0;
        }

        @Override
        public void onBindView(RcvHolder holder, RcvSectionWrapper<TestSection, TestData> wrapper, int position)
        {
            holder.setTvText(R.id.tv_section_label, wrapper.getSection().getSection());
        }
    }

    //第一种Data ItemView
    private class DataItemView01 extends RcvBaseItemView<RcvSectionWrapper<TestSection, TestData>>
    {
        @Override
        public int getItemViewLayoutId()
        {
            return R.layout.adapter_item_long;
        }

        @Override
        public boolean isForViewType(RcvSectionWrapper<TestSection, TestData> item, int position)
        {
            return !item.isSection() && item.getData().getType() == 0;
        }

        @Override
        public void onBindView(RcvHolder holder, RcvSectionWrapper<TestSection, TestData> wrapper, int position)
        {
            TextView textView = holder.findView(R.id.tv_item_long);
            textView.setBackgroundColor(Color.GREEN);
            textView.setText("第一种数据类型：" + wrapper.getData().getContent());
        }
    }

    //第二种Data ItemView
    private class DataItemView02 extends RcvBaseItemView<RcvSectionWrapper<TestSection, TestData>>
    {
        @Override
        public int getItemViewLayoutId()
        {
            return R.layout.adapter_item_short;
        }

        @Override
        public boolean isForViewType(RcvSectionWrapper<TestSection, TestData> item, int position)
        {
            return !item.isSection() && item.getData().getType() != 0;
        }

        @Override
        public void onBindView(RcvHolder holder, RcvSectionWrapper<TestSection, TestData> wrapper, int position)
        {
            TextView textView = holder.findView(R.id.tv_item_short);
            textView.setBackgroundColor(Color.RED);
            textView.setText("第二种数据类型：" + wrapper.getData().getContent());
        }
    }
}
```

**注：**<br />
①传给适配器的数据集合内实体类必须经过RcvSectionWrapper包装。<br />
②向外公布的方法（例如点击监听）的实体类泛型不能传错。
<br />

**9.悬浮标签StickyLayout**

适配器方面无需改动，直接使用``RcvSectionSingleLabelAdapter``即可，在RecyclerView同级布局下添加``RcvStickyLayout``，然后在代码中关联起来即可：

```
    // xml布局中添加RcvStickyLayout：
    <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <android.support.v7.widget.RecyclerView
            android:id="@+id/rcv_sticky"
            android:layout_width="match_parent"
            android:layout_height="match_parent"/>

        <com.lwkandroid.rcvadapter.ui.RcvStickyLayout
            android:id="@+id/stickyLayout"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"/>
    </FrameLayout>



    //代码中关联RecyclerView
    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rcv_sticky);
    /...省略设置RecyclerView的LayoutMananger和Adapter.../
    RcvStickyLayout stickyLayout = (RcvStickyLayout) findViewById(R.id.stickyLayout);
    stickyLayout.attachToRecyclerView(recyclerView);
```

**上面就是大部分基础功能的使用方法了，想了解更多方法请看源码。**
<br />

### 混淆配置
```
-dontwarn com.lwkandroid.rcvadapter.**
-keep class com.lwkandroid.rcvadapter.**{*;}
```
<br />

### 待实现功能
 - 暂时未想到

### 开源参考
1. https://github.com/hongyangAndroid/baseAdapter
2. https://github.com/CymChad/BaseRecyclerViewAdapterHelper
