# RecyclerViewAdapter
<br />
Github的README排版混乱，想了解该框架的可参考以下博客说明：<br />
CSDN：http://blog.csdn.net/lwk520136/article/details/70787798<br />
简书：http://www.jianshu.com/p/c86a39f4e811
<br />
###引用方式
```
compile 'com.lwkandroid:recyclerviewadapter:1.0.0'
```

<br />
###基础功能
 - 快速实现适配器，支持多种ViewType模式
 - 支持添加HeaderView、FooterView、EmptyView
 - 支持滑到底部加载更多
 - 支持每条Item显示的动画

<br />
###使用方式
**1. 当Item样式一样时，只需继承`RcvSingleAdapter<T>`即可，示例**：
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
**2. 当Item样式不一样时，即存在多种`ViewType`类型的Item，需要将每种`ViewType`的Item单独实现，再关联到`RcvMultiAdapter<T>`中，示例**：
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
**3.优雅的添加HeaderView、FooterView、EmptyView，只需要在RecyclerView设置LayoutManager后调用相关方法即可**：
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
```
//最简单的方式，使用默认的样式
mAdapter.enableLoadMore(true, new RcvLoadMoreListener()
{
    @Override
    public void onLoadMoreRequest()
    {
        //TODO 在这里实现加载更多

        /*加载完成后可调用以下方法快速回调给适配器*/
        //加载失败
        mAdapter.notifyLoadMoreFail();
        //加载成功，第二个参数代表是否还有更多数据，如果为false和下面的方法效果一样
        mAdapter.notifyLoadMoreSuccess(newDatas, false);
        //往后没有更多数据，不会再触发加载更多
        mAdapter.notifyLoadMoreHasNoMoreData();
    }
});

//自定义样式
mAdapter.enableLoadMore(true, ? extends RcvBaseLoadMoreView,new RcvLoadMoreListener()
{
    @Override
    public void onLoadMoreRequest()
    {
	    //TODO 在这里实现加载更多

        /*加载完成后可调用以下方法快速回调给适配器*/
        //加载失败
        mAdapter.notifyLoadMoreFail();
        //加载成功，第二个参数代表是否还有更多数据，如果为false和下面的方法效果一样
        mAdapter.notifyLoadMoreSuccess(newDatas, false);
        //往后没有更多数据，不会再触发加载更多
        mAdapter.notifyLoadMoreHasNoMoreData();
    }
});
```
**注：**
① 默认的样式实现是类`RcvDefLoadMoreView`
② 如需自定义样式，只需继承`RcvBaseLoadMoreView`，只要重写各状态UI的实现，无须关心状态切换，可参考`RcvDefLoadMoreView`内的实现方式。
<br />
**5.设置Item显示动画，先直接上代码：**
```
//使用默认的动画（Alpha动画）
mAdapter.enableItemShowingAnim(true);

//使用自定义动画
mAdapter.enableItemShowingAnim(true, ? extends RcvBaseAnimation);
```
**注：**
①默认动画的实现是类`RcvAlphaInAnim`
②自定义样式需要继承`RcvBaseAnimation`，可参考`RcvAlphaInAnim`内部实现。
<br />
**6.设置Item点击监听：**
```
//设置OnItemClickListener
mAdapter.setOnItemClickListener(new RcvItemViewClickListener<TestData>()
        {
            @Override
            public void onItemViewClicked(int viewType, View view, RcvHolder holder, TestData testData, int position)
            {
                //onClick回调
            }
        });

//设置OnItemLongClickListener
mAdapter.setOnItemLongClickListener(new RcvItemViewLongClickListener<TestData>()
        {
            @Override
            public void onItemViewLongClicked(int viewType, View view, RcvHolder holder, TestData testData, int position)
            {
                //onLongClick回调
            }
        });
```
<br />
**上面6点就是大部分基础功能的使用方法了，想了解更多方法请看源码。**
<br />
###待实现功能

 - 实现嵌套Section
 - 实现类似StickyHead
<br />
###开源参考
1. https://github.com/hongyangAndroid/baseAdapter
2. https://github.com/CymChad/BaseRecyclerViewAdapterHelper
