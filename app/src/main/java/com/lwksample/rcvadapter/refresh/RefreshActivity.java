package com.lwksample.rcvadapter.refresh;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lwkandroid.rcvadapter.RcvSingleAdapter;
import com.lwkandroid.rcvadapter.holder.RcvHolder;
import com.lwkandroid.rcvadapter.listener.RcvLoadMoreListener;
import com.lwkandroid.rcvadapter.ui.RcvDefLoadMoreView;
import com.lwkandroid.rcvadapter.utils.RcvLinearDecoration;
import com.lwksample.rcvadapter.R;

import java.util.List;

public class RefreshActivity extends AppCompatActivity implements
        SwipeRefreshLayout.OnRefreshListener,
        RcvLoadMoreListener,
        RefreshConstract.View
{
    RefreshConstract.Presenter mPresenter;
    SwipeRefreshLayout mRefreshLayout;
    RecyclerView mRecyclerView;
    Adapter mAdapter;
    RcvDefLoadMoreView mLoadMoreView;

    //代表上次操作的页码
    private int mLastPage = 0;
    //代表当前页码
    private int mCurrentPage = 0;
    //每页加载数据量
    private final int PAGE_SIZE =20;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);
        mPresenter = new DataPresenter(this);

        mRefreshLayout = findViewById(R.id.refresh_layout);
        mRecyclerView = findViewById(R.id.rcv_simple);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(RefreshActivity.this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(RcvLinearDecoration.createDefaultVertical(this));
        mAdapter = new Adapter(this, null);
        mAdapter.setEmptyView(R.layout.layout_emptyview02);
        mRecyclerView.setAdapter(mAdapter);

        mLoadMoreView = new RcvDefLoadMoreView.Builder()
                .setBgColor(Color.GREEN)
                .setTextColor(Color.RED)
                .build(this);
        mRefreshLayout.setOnRefreshListener(this);

        //初始化数据
        mRefreshLayout.setRefreshing(true);
        this.onRefresh();
    }

    @Override
    public void onRefresh()
    {
        //触发刷新的方法
        mAdapter.enableLoadMore(false);
        mLastPage = mCurrentPage;
        mCurrentPage = 0;
        mPresenter.requestData(true, mCurrentPage, PAGE_SIZE);
    }

    @Override
    public void onRefreshSuccess(List<String> dataList, boolean hasMoreData)
    {
        mRefreshLayout.setRefreshing(false);
        //刷新成功后的方法
        mAdapter.refreshDatas(dataList);
        //开启加载更多
        mAdapter.enableLoadMore(true, mLoadMoreView, this);
        //如果后续没有更多数据，提示用户
        if (!hasMoreData)
            mAdapter.notifyLoadMoreHasNoMoreData();
        //如果当前页一条数据都没有，直接关闭加载更多功能
        if (dataList == null || dataList.size() == 0)
            mAdapter.enableLoadMore(false);
    }

    @Override
    public void onRefreshFail()
    {
        mRefreshLayout.setRefreshing(false);
        //刷新失败还原页码
        mCurrentPage = mLastPage;
    }

    @Override
    public void onLoadMoreRequest()
    {
        //触发加载下一页的方法
        mLastPage = mCurrentPage;
        mCurrentPage++;
        mPresenter.requestData(false, mCurrentPage, PAGE_SIZE);
    }

    @Override
    public void onLoadMoreSuccess(List<String> dataList, boolean hasMoreData)
    {
        //加载下一页成功
        mAdapter.notifyLoadMoreSuccess(dataList, hasMoreData);
    }

    @Override
    public void onLoadMoreFail()
    {
        //加载下一页失败
        //还原页码
        mCurrentPage = mLastPage;
        //提示用户
        mAdapter.notifyLoadMoreFail();
    }

    private class Adapter extends RcvSingleAdapter<String>
    {
        public Adapter(Context context, List<String> datas)
        {
            super(context, android.R.layout.simple_list_item_1, datas);
        }

        @Override
        public void onBindView(RcvHolder holder, String itemData, int position)
        {
            holder.setTvText(android.R.id.text1, itemData);
        }
    }
}
