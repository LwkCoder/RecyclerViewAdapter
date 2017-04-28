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
import com.lwkandroid.rcvadapter.bean.RcvSectionWrapper;
import com.lwkandroid.rcvadapter.holder.RcvHolder;
import com.lwkandroid.rcvadapter.listener.RcvItemViewClickListener;
import com.lwkandroid.rcvadapter.listener.RcvItemViewLongClickListener;
import com.lwkandroid.rcvadapter.listener.RcvLoadMoreListener;
import com.lwkandroid.rcvadapter.utils.RcvGridDecoration;
import com.lwkandroid.rcvadapter.utils.RcvLinearDecoration;

/**
 * 效果界面
 */
public class DetailActivity extends AppCompatActivity
{
    private int viewTypeFlag;
    private RecyclerView mRecyclerView;
    private RcvMultiAdapter mAdapter;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mHandler = new Handler(getMainLooper());

        Intent intent = getIntent();
        int layMgrFlag = intent.getIntExtra(ParamsFlag.INTENT_LAYOUT_MANAGER, ParamsFlag.LAYOUT_MANAGER_LINEAR);
        viewTypeFlag = intent.getIntExtra(ParamsFlag.INTENT_LAYOUT_VIEW_TYPE, ParamsFlag.VIEW_TYPE_SINGLE);
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
            mAdapter = new TestSingleAdapter(this, null);
        } else if (viewTypeFlag == ParamsFlag.VIEW_TYPE_MULTI)
        {
            mAdapter = new TestMultiAdapter(this, null);
        } else if (viewTypeFlag == ParamsFlag.VIEW_TYPE_SECTION)
        {
            mAdapter = new TestSectionAdapter(this, null);
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
            if (viewTypeFlag == ParamsFlag.VIEW_TYPE_SECTION)
                mAdapter.refreshDatas(DataModel.getSomeSectionData(30));
            else
                mAdapter.refreshDatas(DataModel.getSomeTestData("模拟数据", 30));
        }

        //item的点击事件
        if (viewTypeFlag == ParamsFlag.VIEW_TYPE_SECTION)
        {
            //注意点击监听的泛型！！！！
            mAdapter.setOnItemClickListener(new RcvItemViewClickListener<RcvSectionWrapper<TestSection, TestData>>()
            {
                @Override
                public void onItemViewClicked(RcvHolder holder, RcvSectionWrapper<TestSection, TestData> wrapper, int position)
                {
                    Toast.makeText(DetailActivity.this, "Click position=" + position + " wrapper=" + wrapper, Toast.LENGTH_SHORT).show();
                }
            });
            mAdapter.setOnItemLongClickListener(new RcvItemViewLongClickListener<RcvSectionWrapper<TestSection, TestData>>()
            {
                @Override
                public void onItemViewLongClicked(RcvHolder holder, RcvSectionWrapper<TestSection, TestData> wrapper, int position)
                {
                    Toast.makeText(DetailActivity.this, "LongClick position=" + position + " wrapper=" + wrapper, Toast.LENGTH_SHORT).show();
                }
            });
        } else
        {
            mAdapter.setOnItemClickListener(new RcvItemViewClickListener<TestData>()
            {
                @Override
                public void onItemViewClicked(RcvHolder holder, TestData testData, int position)
                {
                    Toast.makeText(DetailActivity.this, "Click position=" + position + " data=" + testData, Toast.LENGTH_SHORT).show();
                }
            });
            mAdapter.setOnItemLongClickListener(new RcvItemViewLongClickListener<TestData>()
            {
                @Override
                public void onItemViewLongClicked(RcvHolder holder, TestData testData, int position)
                {
                    Toast.makeText(DetailActivity.this, "LongClick position=" + position + " data=" + testData, Toast.LENGTH_SHORT).show();
                }
            });
        }

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
                if (viewTypeFlag == ParamsFlag.VIEW_TYPE_SECTION)
                    mAdapter.refreshDatas(DataModel.getSomeSectionData(30));
                else
                    mAdapter.refreshDatas(DataModel.getSomeTestData("模拟数据", 30));
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
                if (viewTypeFlag == ParamsFlag.VIEW_TYPE_SECTION)
                {
                    mAdapter.notifyLoadMoreSuccess(DataModel.getSomeSectionData(5), false);
                } else
                {
                    mAdapter.notifyLoadMoreSuccess(DataModel.getSomeTestData("新增数据", 5), false);
                }
            }
        }, 1000);
    }

}
