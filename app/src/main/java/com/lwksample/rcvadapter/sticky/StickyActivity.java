package com.lwksample.rcvadapter.sticky;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.lwk.comactionbar.ComActionBar;
import com.lwkandroid.rcvadapter.ui.RcvStickyLayout;
import com.lwksample.rcvadapter.DataModel;
import com.lwksample.rcvadapter.R;
import com.lwksample.rcvadapter.TestSectionAdapter;

public class StickyActivity extends AppCompatActivity implements View.OnClickListener
{
    private RecyclerView mRecyclerView;
    private TestSectionAdapter mAdapter;
    private RcvStickyLayout mStickyLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticky);

        mRecyclerView = (RecyclerView) findViewById(R.id.rcv_sticky);
        mStickyLayout = (RcvStickyLayout) findViewById(R.id.stickyLayout);
        ComActionBar actionBar = (ComActionBar) findViewById(R.id.cab_sticky);
        actionBar.setRightClickListener01(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(StickyActivity.this));
        //        mRecyclerView.setLayoutManager(new GridLayoutManager(StickyActivity.this, 3));
        //        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mAdapter = new TestSectionAdapter(this, DataModel.getSomeSectionData(80));
        mRecyclerView.setAdapter(mAdapter);

        mStickyLayout.attachToRecyclerView(mRecyclerView);
        mStickyLayout.setOnStickyLayoutClickListener(new RcvStickyLayout.OnStickyLayoutClickedListener()
        {
            @Override
            public void onClicked(View stickyLayout)
            {
                Toast.makeText(StickyActivity.this, "点击StickyLayout", Toast.LENGTH_SHORT).show();
                mRecyclerView.smoothScrollToPosition(mStickyLayout.getCurrentIndicatePosition());
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.fl_comactionbar_right01:
                mAdapter.addDatas(DataModel.getSomeSectionData2(50));
                break;
        }
    }
}
