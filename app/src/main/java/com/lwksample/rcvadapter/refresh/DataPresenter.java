package com.lwksample.rcvadapter.refresh;

import java.util.List;

/**
 * Created by LWK
 * TODO Presenter层
 */

public class DataPresenter implements RefreshConstract.Presenter
{
    private RefreshConstract.View mView;
    private RefreshConstract.Model mModel;

    public DataPresenter(RefreshConstract.View view)
    {
        this.mView = view;
        this.mModel = new DataModel();
    }

    @Override
    public void requestData(final boolean isRrefresh, int page, final int num)
    {
        mModel.requestData(page, num, new CallBack<List<String>>()
        {
            @Override
            void onSuccess(List<String> strings)
            {
                //加载更多需要自行判断是否还有更多数据
                //正常情况下，返回的数据条目足够一页加载量num，即可视为后续还可以翻页
                if (isRrefresh)
                    mView.onRefreshSuccess(strings, strings != null && strings.size() == num);
                else
                    mView.onLoadMoreSuccess(strings, strings != null && strings.size() == num);
            }

            @Override
            void onFail()
            {
                if (isRrefresh)
                    mView.onRefreshFail();
                else
                    mView.onLoadMoreFail();
            }
        });
    }
}
