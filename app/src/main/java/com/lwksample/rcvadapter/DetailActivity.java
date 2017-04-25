package com.lwksample.rcvadapter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lwkandroid.rcvadapter.RcvMultiAdapter;
import com.lwkandroid.rcvadapter.holder.RcvHolder;
import com.lwkandroid.rcvadapter.listener.RcvItemViewClickListener;
import com.lwkandroid.rcvadapter.listener.RcvItemViewLongClickListener;
import com.lwkandroid.rcvadapter.listener.RcvLoadMoreListener;
import com.lwkandroid.rcvadapter.utils.RcvGridDecoration;
import com.lwkandroid.rcvadapter.utils.RcvLinearDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * 效果界面
 */
public class DetailActivity extends AppCompatActivity
{
    private RecyclerView mRecyclerView;
    private RcvMultiAdapter<TestData> mAdapter;
    private List<TestData> mDatas = new ArrayList<>();
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mHandler = new Handler(getMainLooper());

        Intent intent = getIntent();
        int layMgrFlag = intent.getIntExtra(ParamsFlag.INTENT_LAYOUT_MANAGER, ParamsFlag.LAYOUT_MANAGER_LINEAR);
        int viewTypeFlag = intent.getIntExtra(ParamsFlag.INTENT_LAYOUT_VIEW_TYPE, ParamsFlag.VIEW_TYPE_SINGLE);
        boolean header = intent.getBooleanExtra(ParamsFlag.INTENT_LAYOUT_HEAD, false);
        boolean footer = intent.getBooleanExtra(ParamsFlag.INTENT_LAYOUT_FOOT, false);
        boolean empty = intent.getBooleanExtra(ParamsFlag.INTENT_LAYOUT_EMPTY, false);
        boolean loadMore = intent.getBooleanExtra(ParamsFlag.INTENT_LAYOUT_LOADMORE, false);
        boolean anim = intent.getBooleanExtra(ParamsFlag.INTENT_LAYOUT_ANIM, false);

        mRecyclerView = (RecyclerView) findViewById(R.id.rcv_detail);

        //选择LayoutManager
        switch (layMgrFlag)
        {
            case ParamsFlag.LAYOUT_MANAGER_LINEAR:
                mRecyclerView.setLayoutManager(new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.VERTICAL, false));
                mRecyclerView.addItemDecoration(new RcvLinearDecoration(DetailActivity.this, LinearLayoutManager.VERTICAL));
                break;
            case ParamsFlag.LAYOUT_MANAGER_GRID:
                mRecyclerView.setLayoutManager(new GridLayoutManager(DetailActivity.this, 3));
                mRecyclerView.addItemDecoration(new RcvGridDecoration(DetailActivity.this));
                break;
            case ParamsFlag.LAYOUT_MANAGER_STAGGER:
                mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                mRecyclerView.addItemDecoration(new RcvGridDecoration(DetailActivity.this));
                break;
        }

        //选择不同ViewType的适配器
        if (viewTypeFlag == ParamsFlag.VIEW_TYPE_SINGLE)
        {
            mAdapter = new TestSingleAdapter(this, mDatas);
        } else
        {
            mAdapter = new TestMultiAdapter(this, mDatas);
        }

        //添加HeaderView、FooterView、LoadMore
        //要先设置RecyclerView的LayoutManager!!!!
        if (header)
        {
            View headerView = getLayoutInflater().inflate(R.layout.layout_headview, (ViewGroup) findViewById(android.R.id.content), false);
            mAdapter.addHeaderView(headerView);
        }
        if (footer)
        {
            View footerView = getLayoutInflater().inflate(R.layout.layout_footview, (ViewGroup) findViewById(android.R.id.content), false);
            mAdapter.addFooterView(footerView);
        }
        if (loadMore)
        {
            mAdapter.enableLoadMore(true, new RcvLoadMoreListener()
            {
                @Override
                public void onLoadMoreRequest()
                {
                    loadMoreDatas();
                }
            });
        }
        //设置item显示动画
        mAdapter.enableItemShowingAnim(anim);

        //如果需要展示空数据占位View，就先不给模拟数据
        if (empty)
        {
            View emptyView = getEmptyView();
            mAdapter.setEmptyView(emptyView);
        } else
        {
            for (int i = 0; i < 30; i++)
            {
                mDatas.add(new TestData("模拟数据" + i));
            }
            mAdapter.refreshDatas(mDatas);
        }

        //item的点击事件
        mAdapter.setOnItemClickListener(new RcvItemViewClickListener<TestData>()
        {
            @Override
            public void onItemViewClicked(int viewType, View view, RcvHolder holder, TestData testData, int position)
            {
                Toast.makeText(DetailActivity.this, "Click position=" + position + " data=" + testData, Toast.LENGTH_SHORT).show();
            }
        });
        mAdapter.setOnItemLongClickListener(new RcvItemViewLongClickListener<TestData>()
        {
            @Override
            public void onItemViewLongClicked(int viewType, View view, RcvHolder holder, TestData testData, int position)
            {
                Toast.makeText(DetailActivity.this, "LongClick position=" + position + " data=" + testData, Toast.LENGTH_SHORT).show();
            }
        });

        mRecyclerView.setAdapter(mAdapter);
    }

    //获取空数据占位View
    private View getEmptyView()
    {
        View emptyView = getLayoutInflater().inflate(R.layout.layout_emptyview, (ViewGroup) findViewById(android.R.id.content), false);
        emptyView.findViewById(R.id.tv_empty).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mDatas.clear();
                for (int i = 0; i < 30; i++)
                {
                    mDatas.add(new TestData("模拟数据" + i));
                }
                mAdapter.refreshDatas(mDatas);
            }
        });
        return emptyView;
    }

    private void loadMoreDatas()
    {
        mHandler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                List<TestData> newDatas = new ArrayList<>();
                for (int i = 0; i < 10; i++)
                {
                    newDatas.add(new TestData("新增数据"));
                }
                mAdapter.notifyLoadMoreSuccess(newDatas, false);
            }
        }, 1000);
    }
}
