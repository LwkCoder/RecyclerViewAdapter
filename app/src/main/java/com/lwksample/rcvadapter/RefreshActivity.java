package com.lwksample.rcvadapter;

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

import java.util.ArrayList;
import java.util.List;

public class RefreshActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, RcvLoadMoreListener
{
    SwipeRefreshLayout mRefreshLayout;
    RecyclerView mRecyclerView;
    Adapter mAdapter;
    RcvDefLoadMoreView mLoadMoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);

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
        mAdapter.enableLoadMore(false);
        mRefreshLayout.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                List<String> datas = new ArrayList<>();
                for (int i = 0; i < 15; i++)
                {
                    datas.add("DATA");
                }

                mAdapter.refreshDatas(datas);
                mAdapter.enableLoadMore(true, mLoadMoreView, RefreshActivity.this);
                mRefreshLayout.setRefreshing(false);
            }
        }, 1500);
    }

    @Override
    public void onLoadMoreRequest()
    {
        mLoadMoreView.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                List<String> datas = new ArrayList<>();
                for (int i = 0; i < 15; i++)
                {
                    datas.add("MORE_DATA");
                }

                mAdapter.addDatas(datas);
                mAdapter.notifyLoadMoreSuccess(datas, false);
            }
        }, 1500);
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
