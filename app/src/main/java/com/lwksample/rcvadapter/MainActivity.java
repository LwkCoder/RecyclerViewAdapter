package com.lwksample.rcvadapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;

import com.lwkandroid.widget.comactionbar.ComActionBar;
import com.lwksample.rcvadapter.refresh.RefreshActivity;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{
    private RadioGroup mRgLayMgr;
    private RadioGroup mRgViewType;
    private CheckBox mCkHeader;
    private CheckBox mCkFooter;
    private CheckBox mCkEmpty;
    private CheckBox mCkLoadMore;
    private CheckBox mCkAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ComActionBar actionBar = (ComActionBar) findViewById(R.id.cab_main);
        actionBar.setRightClickListener01(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //跳转到StickyDemo
                startActivity(new Intent(MainActivity.this, StickyActivity.class));
            }
        });
        actionBar.setRightClickListener02(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //跳转到RefreshDemo
                startActivity(new Intent(MainActivity.this, RefreshActivity.class));
            }
        });

        mRgLayMgr = (RadioGroup) findViewById(R.id.rg_main_laymgr);
        mRgViewType = (RadioGroup) findViewById(R.id.rg_main_viewtype);
        mCkHeader = (CheckBox) findViewById(R.id.ck_main_header);
        mCkFooter = (CheckBox) findViewById(R.id.ck_main_footer);
        mCkEmpty = (CheckBox) findViewById(R.id.ck_main_empty);
        mCkLoadMore = (CheckBox) findViewById(R.id.ck_main_loadMore);
        mCkAnim = (CheckBox) findViewById(R.id.ck_main_anim);

        findViewById(R.id.btn_main_go).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                gogogo();
            }
        });
    }

    private int getLayoutManagerFlag()
    {
        int id = mRgLayMgr.getCheckedRadioButtonId();
        if (id == R.id.rb_main_01)
            return ParamsFlag.LAYOUT_MANAGER_LINEAR;
        else if (id == R.id.rb_main_02)
            return ParamsFlag.LAYOUT_MANAGER_GRID;
        else
            return ParamsFlag.LAYOUT_MANAGER_STAGGER;
    }

    private int getViewTypeFlag()
    {
        int id = mRgViewType.getCheckedRadioButtonId();
        if (id == R.id.rb_main_04)
            return ParamsFlag.VIEW_TYPE_SINGLE;
        else if (id == R.id.rb_main_05)
            return ParamsFlag.VIEW_TYPE_MULTI;
        else
            return ParamsFlag.VIEW_TYPE_SECTION;
    }

    private void gogogo()
    {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(ParamsFlag.INTENT_LAYOUT_MANAGER, getLayoutManagerFlag());
        intent.putExtra(ParamsFlag.INTENT_LAYOUT_VIEW_TYPE, getViewTypeFlag());
        intent.putExtra(ParamsFlag.INTENT_LAYOUT_HEAD, mCkHeader.isChecked());
        intent.putExtra(ParamsFlag.INTENT_LAYOUT_FOOT, mCkFooter.isChecked());
        intent.putExtra(ParamsFlag.INTENT_LAYOUT_EMPTY, mCkEmpty.isChecked());
        intent.putExtra(ParamsFlag.INTENT_LAYOUT_LOADMORE, mCkLoadMore.isChecked());
        intent.putExtra(ParamsFlag.INTENT_LAYOUT_ANIM, mCkAnim.isChecked());
        startActivity(intent);
    }
}
